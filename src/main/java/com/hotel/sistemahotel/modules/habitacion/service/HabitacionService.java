package com.hotel.sistemahotel.modules.habitacion.service;

import com.hotel.sistemahotel.modules.habitacion.dto.HabitacionRequestDto;
import com.hotel.sistemahotel.modules.habitacion.dto.HabitacionResponseDto;
import com.hotel.sistemahotel.modules.habitacion.dto.TipoHabitacionDto;
import com.hotel.sistemahotel.modules.habitacion.entity.Habitacion;
import com.hotel.sistemahotel.modules.habitacion.entity.TipoHabitacion;
import com.hotel.sistemahotel.modules.habitacion.repository.HabitacionRepository;
import com.hotel.sistemahotel.modules.habitacion.repository.TipoHabitacionRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;

    // ── Tipos de habitación ──────────────────────────────

    public TipoHabitacionDto crearTipo(TipoHabitacionDto dto) {
        UUID sucursalId = TenantContext.getSucursalId();

        if (tipoHabitacionRepository.existsByNombreAndSucursalId(dto.getNombre(), sucursalId)) {
            throw BusinessException.badRequest("Ya existe un tipo con ese nombre");
        }

        TipoHabitacion tipo = TipoHabitacion.builder()
                .sucursalId(sucursalId)
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .capacidadMax(dto.getCapacidadMax() != null ? dto.getCapacidadMax() : 2)
                .build();

        return toTipoDto(tipoHabitacionRepository.save(tipo));
    }

    public List<TipoHabitacionDto> listarTipos() {
        UUID sucursalId = TenantContext.getSucursalId();
        return tipoHabitacionRepository.findAllBySucursalId(sucursalId)
                .stream()
                .map(this::toTipoDto)
                .collect(Collectors.toList());
    }

    // ── Habitaciones ─────────────────────────────────────

    public HabitacionResponseDto crear(HabitacionRequestDto dto) {
        UUID sucursalId = TenantContext.getSucursalId();

        if (habitacionRepository.existsByNumeroAndSucursalId(dto.getNumero(), sucursalId)) {
            throw BusinessException.badRequest("Ya existe la habitación número " + dto.getNumero());
        }

        TipoHabitacion tipo = tipoHabitacionRepository
                .findByIdAndSucursalId(dto.getTipoId(), sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Tipo de habitación no encontrado"));

        Habitacion habitacion = Habitacion.builder()
                .sucursalId(sucursalId)
                .tipoId(tipo.getId())
                .numero(dto.getNumero())
                .piso(dto.getPiso())
                .estado("DISPONIBLE")
                .precioHora(dto.getPrecioHora())
                .precioDia(dto.getPrecioDia())
                .precioNoche(dto.getPrecioNoche())
                .isActive(true)
                .build();

        return toDto(habitacionRepository.save(habitacion), tipo.getNombre());
    }

    public List<HabitacionResponseDto> listar() {
        UUID sucursalId = TenantContext.getSucursalId();
        return habitacionRepository.findAllBySucursalIdAndIsActiveTrue(sucursalId)
                .stream()
                .map(h -> {
                    String tipoNombre = tipoHabitacionRepository.findById(h.getTipoId())
                            .map(TipoHabitacion::getNombre)
                            .orElse("");
                    return toDto(h, tipoNombre);
                })
                .collect(Collectors.toList());
    }

    public List<HabitacionResponseDto> listarPorEstado(String estado) {
        UUID sucursalId = TenantContext.getSucursalId();
        return habitacionRepository
                .findAllBySucursalIdAndEstadoAndIsActiveTrue(sucursalId, estado)
                .stream()
                .map(h -> {
                    String tipoNombre = tipoHabitacionRepository.findById(h.getTipoId())
                            .map(TipoHabitacion::getNombre)
                            .orElse("");
                    return toDto(h, tipoNombre);
                })
                .collect(Collectors.toList());
    }

    public HabitacionResponseDto buscarPorId(UUID id) {
        Habitacion h = findHabitacion(id);
        String tipoNombre = tipoHabitacionRepository.findById(h.getTipoId())
                .map(TipoHabitacion::getNombre)
                .orElse("");
        return toDto(h, tipoNombre);
    }

    public HabitacionResponseDto actualizar(UUID id, HabitacionRequestDto dto) {
        Habitacion habitacion = findHabitacion(id);
        UUID sucursalId = TenantContext.getSucursalId();

        TipoHabitacion tipo = tipoHabitacionRepository
                .findByIdAndSucursalId(dto.getTipoId(), sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Tipo de habitación no encontrado"));

        habitacion.setTipoId(tipo.getId());
        habitacion.setPiso(dto.getPiso());
        habitacion.setPrecioHora(dto.getPrecioHora());
        habitacion.setPrecioDia(dto.getPrecioDia());
        habitacion.setPrecioNoche(dto.getPrecioNoche());

        return toDto(habitacionRepository.save(habitacion), tipo.getNombre());
    }

    public HabitacionResponseDto cambiarEstado(UUID id, String nuevoEstado) {
        Habitacion habitacion = findHabitacion(id);
        habitacion.setEstado(nuevoEstado);
        return toDto(habitacionRepository.save(habitacion),
                tipoHabitacionRepository.findById(habitacion.getTipoId())
                        .map(TipoHabitacion::getNombre).orElse(""));
    }

    public void desactivar(UUID id) {
        Habitacion habitacion = findHabitacion(id);
        habitacion.setIsActive(false);
        habitacionRepository.save(habitacion);
    }

    // ── Helpers ──────────────────────────────────────────

    private Habitacion findHabitacion(UUID id) {
        UUID sucursalId = TenantContext.getSucursalId();
        return habitacionRepository.findByIdAndSucursalId(id, sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Habitación no encontrada"));
    }

    private HabitacionResponseDto toDto(Habitacion h, String tipoNombre) {
        return HabitacionResponseDto.builder()
                .id(h.getId())
                .sucursalId(h.getSucursalId())
                .tipoId(h.getTipoId())
                .tipoNombre(tipoNombre)
                .numero(h.getNumero())
                .piso(h.getPiso())
                .estado(h.getEstado())
                .precioHora(h.getPrecioHora())
                .precioDia(h.getPrecioDia())
                .precioNoche(h.getPrecioNoche())
                .isActive(h.getIsActive())
                .build();
    }

    private TipoHabitacionDto toTipoDto(TipoHabitacion t) {
        return TipoHabitacionDto.builder()
                .id(t.getId())
                .nombre(t.getNombre())
                .descripcion(t.getDescripcion())
                .capacidadMax(t.getCapacidadMax())
                .build();
    }
}