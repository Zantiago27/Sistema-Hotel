package com.hotel.sistemahotel.modules.turno.service;

import com.hotel.sistemahotel.modules.comprobante.repository.PagoRepository;
import com.hotel.sistemahotel.modules.turno.dto.*;
import com.hotel.sistemahotel.modules.turno.entity.Turno;
import com.hotel.sistemahotel.modules.turno.repository.TurnoRepository;
import com.hotel.sistemahotel.modules.trabajador.entity.Usuario;
import com.hotel.sistemahotel.modules.trabajador.repository.UsuarioRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoRepository pagoRepository;

    @Transactional
    public TurnoResponseDto abrir(AbrirTurnoDto dto) {
        UUID sucursalId = TenantContext.getSucursalId();
        UUID usuarioId  = TenantContext.getUsuarioId();

        // Verificar que no haya turno abierto para este usuario
        turnoRepository.findByUsuarioIdAndEstado(usuarioId, "ABIERTO")
                .ifPresent(t -> {
                    throw BusinessException.badRequest(
                            "Ya tienes un turno abierto. Ciérralo antes de abrir uno nuevo");
                });

        Turno turno = Turno.builder()
                .sucursalId(sucursalId)
                .usuarioId(usuarioId)
                .inicio(OffsetDateTime.now())
                .montoApertura(dto.getMontoApertura())
                .estado("ABIERTO")
                .build();

        return toDto(turnoRepository.save(turno));
    }

    @Transactional
    public TurnoResponseDto cerrar(CerrarTurnoDto dto) {
        UUID usuarioId  = TenantContext.getUsuarioId();
        UUID sucursalId = TenantContext.getSucursalId();

        Turno turno = turnoRepository
                .findByUsuarioIdAndEstado(usuarioId, "ABIERTO")
                .orElseThrow(() -> BusinessException.notFound(
                        "No tienes un turno abierto"));

        // Calcular lo que debería haber en caja
        // Adelantos cobrados + pagos finales durante este turno
        BigDecimal montoEsperado = pagoRepository
                .sumMontoByTurnoRango(sucursalId, turno.getInicio(), OffsetDateTime.now());

        turno.setFin(OffsetDateTime.now());
        turno.setMontoCierre(dto.getMontoCierre());
        turno.setEstado("CERRADO");

        return toDto(turnoRepository.save(turno));
    }

    public TurnoResponseDto turnoActual() {
        UUID usuarioId = TenantContext.getUsuarioId();

        return turnoRepository
                .findByUsuarioIdAndEstado(usuarioId, "ABIERTO")
                .map(this::toDto)
                .orElse(null);
    }

    public List<TurnoResponseDto> historial() {
        UUID sucursalId = TenantContext.getSucursalId();
        return turnoRepository
                .findAllBySucursalIdOrderByInicioDesc(sucursalId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TurnoResponseDto> miHistorial() {
        UUID usuarioId = TenantContext.getUsuarioId();
        return turnoRepository
                .findAllByUsuarioIdOrderByInicioDesc(usuarioId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ── Helpers ──────────────────────────────────────────

    private TurnoResponseDto toDto(Turno t, BigDecimal montoEsperado) {
        String nombreUsuario = usuarioRepository.findById(t.getUsuarioId())
                .map(u -> u.getNombre() + " " + u.getApellido())
                .orElse("—");

        BigDecimal esperado    = montoEsperado != null ? montoEsperado : BigDecimal.ZERO;
        BigDecimal diferencia  = BigDecimal.ZERO;

        if (t.getMontoCierre() != null) {
            diferencia = t.getMontoCierre()
                    .subtract(t.getMontoApertura())
                    .subtract(esperado);
        }

        return TurnoResponseDto.builder()
                .id(t.getId())
                .sucursalId(t.getSucursalId())
                .usuarioId(t.getUsuarioId())
                .usuarioNombre(nombreUsuario)
                .inicio(t.getInicio())
                .fin(t.getFin())
                .montoApertura(t.getMontoApertura())
                .montoCierre(t.getMontoCierre())
                .montoEsperado(esperado)
                .diferencia(diferencia)
                .estado(t.getEstado())
                .build();
    }

    // Sobrecarga para cuando no hay monto esperado
    private TurnoResponseDto toDto(Turno t) {
        return toDto(t, BigDecimal.ZERO);
    }
}