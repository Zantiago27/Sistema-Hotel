package com.hotel.sistemahotel.modules.reserva.service;

import com.hotel.sistemahotel.modules.cliente.entity.Cliente;
import com.hotel.sistemahotel.modules.cliente.repository.ClienteRepository;
import com.hotel.sistemahotel.modules.habitacion.entity.Habitacion;
import com.hotel.sistemahotel.modules.habitacion.repository.HabitacionRepository;
import com.hotel.sistemahotel.modules.reserva.dto.*;
import com.hotel.sistemahotel.modules.reserva.entity.*;
import com.hotel.sistemahotel.modules.reserva.repository.*;
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
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaHabitacionRepository reservaHabitacionRepository;
    private final HuespedRepository huespedRepository;
    private final HabitacionRepository habitacionRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public ReservaResponseDto crear(CrearReservaDto dto) {
        UUID sucursalId = TenantContext.getSucursalId();
        UUID usuarioId  = TenantContext.getUsuarioId();
        UUID empresaId  = TenantContext.getEmpresaId();

        // Validar cliente
        Cliente cliente = clienteRepository
                .findByIdAndEmpresaId(dto.getClienteId(), empresaId)
                .orElseThrow(() -> BusinessException.notFound("Cliente no encontrado"));

        // Validar fechas
        if (!dto.getFechaFinEstimada().isAfter(dto.getFechaInicio())) {
            throw BusinessException.badRequest(
                    "La fecha fin debe ser posterior a la fecha inicio");
        }

        // Crear reserva principal
        Reserva reserva = Reserva.builder()
                .sucursalId(sucursalId)
                .clienteId(cliente.getId())
                .usuarioId(usuarioId)
                .tipoAlquiler(dto.getTipoAlquiler())
                .fechaInicio(dto.getFechaInicio())
                .fechaFinEstimada(dto.getFechaFinEstimada())
                .estado("PENDIENTE")
                .adelanto(dto.getAdelanto() != null ? dto.getAdelanto() : BigDecimal.ZERO)
                .notas(dto.getNotas())
                .build();

        reserva = reservaRepository.save(reserva);

        // Procesar habitaciones
        List<ReservaHabitacionResponseDto> habitacionesResponse =
                procesarHabitaciones(reserva, dto.getHabitaciones(), sucursalId);

        // Calcular total
        BigDecimal total = habitacionesResponse.stream()
                .map(ReservaHabitacionResponseDto::getPrecioAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return buildResponse(reserva, cliente, habitacionesResponse, total);
    }

    @Transactional
    public ReservaResponseDto hacerCheckin(UUID reservaId) {
        Reserva reserva = findReserva(reservaId);

        if (!reserva.getEstado().equals("PENDIENTE")) {
            throw BusinessException.badRequest(
                    "Solo se puede hacer check-in a reservas en estado PENDIENTE");
        }

        OffsetDateTime ahora = OffsetDateTime.now();
        reserva.setEstado("ACTIVA");
        reserva.setFechaInicio(ahora);
        reservaRepository.save(reserva);

        // Actualizar habitaciones y cambiar estado físico
        List<ReservaHabitacion> habitaciones =
                reservaHabitacionRepository.findAllByReservaId(reservaId);

        for (ReservaHabitacion rh : habitaciones) {
            rh.setCheckinReal(ahora);
            rh.setEstado("ACTIVA");
            reservaHabitacionRepository.save(rh);

            // Marcar habitación como OCUPADA
            habitacionRepository.findById(rh.getHabitacionId()).ifPresent(h -> {
                h.setEstado("OCUPADA");
                habitacionRepository.save(h);
            });
        }

        return getDetalle(reservaId);
    }

    @Transactional
    public ReservaResponseDto hacerCheckout(UUID reservaId) {
        Reserva reserva = findReserva(reservaId);

        if (!reserva.getEstado().equals("ACTIVA") &&
                !reserva.getEstado().equals("VENCIDA")) {
            throw BusinessException.badRequest(
                    "Solo se puede hacer check-out a reservas ACTIVAS o VENCIDAS");
        }

        OffsetDateTime ahora = OffsetDateTime.now();
        reserva.setEstado("COMPLETADA");
        reservaRepository.save(reserva);

        // Actualizar habitaciones y liberar
        List<ReservaHabitacion> habitaciones =
                reservaHabitacionRepository.findAllByReservaId(reservaId);

        for (ReservaHabitacion rh : habitaciones) {
            rh.setCheckoutReal(ahora);
            rh.setEstado("COMPLETADA");
            reservaHabitacionRepository.save(rh);

            // Marcar habitación como DISPONIBLE
            habitacionRepository.findById(rh.getHabitacionId()).ifPresent(h -> {
                h.setEstado("DISPONIBLE");
                habitacionRepository.save(h);
            });
        }

        return getDetalle(reservaId);
    }

    @Transactional
    public ReservaResponseDto cancelar(UUID reservaId) {
        Reserva reserva = findReserva(reservaId);

        if (reserva.getEstado().equals("COMPLETADA")) {
            throw BusinessException.badRequest("No se puede cancelar una reserva completada");
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);

        // Liberar habitaciones si estaban ocupadas
        List<ReservaHabitacion> habitaciones =
                reservaHabitacionRepository.findAllByReservaId(reservaId);

        for (ReservaHabitacion rh : habitaciones) {
            rh.setEstado("CANCELADA");
            reservaHabitacionRepository.save(rh);

            if (rh.getCheckinReal() != null && rh.getCheckoutReal() == null) {
                habitacionRepository.findById(rh.getHabitacionId()).ifPresent(h -> {
                    h.setEstado("DISPONIBLE");
                    habitacionRepository.save(h);
                });
            }
        }

        return getDetalle(reservaId);
    }

    public List<ReservaResponseDto> listar(String estado) {
        UUID sucursalId = TenantContext.getSucursalId();
        List<Reserva> reservas = estado != null
                ? reservaRepository.findAllBySucursalIdAndEstadoOrderByCreatedAtDesc(
                sucursalId, estado)
                : reservaRepository.findAllBySucursalIdOrderByCreatedAtDesc(sucursalId);

        return reservas.stream()
                .map(r -> getDetalle(r.getId()))
                .collect(Collectors.toList());
    }

    public List<ReservaResponseDto> listarPorCliente(UUID clienteId) {
        UUID sucursalId = TenantContext.getSucursalId();
        return reservaRepository
                .findAllBySucursalIdAndClienteIdOrderByCreatedAtDesc(sucursalId, clienteId)
                .stream()
                .map(r -> getDetalle(r.getId()))
                .collect(Collectors.toList());
    }

    public ReservaResponseDto getDetalle(UUID reservaId) {
        Reserva reserva = findReserva(reservaId);
        UUID empresaId  = TenantContext.getEmpresaId();

        Cliente cliente = clienteRepository
                .findByIdAndEmpresaId(reserva.getClienteId(), empresaId)
                .orElse(null);

        List<ReservaHabitacion> rhs =
                reservaHabitacionRepository.findAllByReservaId(reservaId);

        List<ReservaHabitacionResponseDto> habitacionesDto = rhs.stream()
                .map(rh -> {
                    Habitacion hab = habitacionRepository
                            .findById(rh.getHabitacionId()).orElse(null);

                    List<HuespedDto> huespedes = huespedRepository
                            .findAllByReservaHabitacionId(rh.getId())
                            .stream()
                            .map(h -> HuespedDto.builder()
                                    .id(h.getId())
                                    .nombre(h.getNombre())
                                    .apellido(h.getApellido())
                                    .tipoDoc(h.getTipoDoc())
                                    .numDoc(h.getNumDoc())
                                    .esTitular(h.getEsTitular())
                                    .build())
                            .collect(Collectors.toList());

                    return ReservaHabitacionResponseDto.builder()
                            .id(rh.getId())
                            .habitacionId(rh.getHabitacionId())
                            .habitacionNumero(hab != null ? hab.getNumero() : "")
                            .tipoAlquiler(rh.getTipoAlquiler())
                            .precioAplicado(rh.getPrecioAplicado())
                            .checkinReal(rh.getCheckinReal())
                            .checkoutReal(rh.getCheckoutReal())
                            .estado(rh.getEstado())
                            .huespedes(huespedes)
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal total = habitacionesDto.stream()
                .map(ReservaHabitacionResponseDto::getPrecioAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return buildResponse(reserva, cliente, habitacionesDto, total);
    }

    // ── Helpers ──────────────────────────────────────────

    private List<ReservaHabitacionResponseDto> procesarHabitaciones(
            Reserva reserva,
            List<ReservaHabitacionRequestDto> dtos,
            UUID sucursalId) {

        return dtos.stream().map(dto -> {
            Habitacion habitacion = habitacionRepository
                    .findByIdAndSucursalId(dto.getHabitacionId(), sucursalId)
                    .orElseThrow(() -> BusinessException.notFound(
                            "Habitación no encontrada: " + dto.getHabitacionId()));

            if (!habitacion.getEstado().equals("DISPONIBLE")) {
                throw BusinessException.badRequest(
                        "La habitación " + habitacion.getNumero() + " no está disponible");
            }

            // Seleccionar precio según tipo de alquiler
            BigDecimal precio = getPrecio(habitacion, reserva.getTipoAlquiler());

            ReservaHabitacion rh = ReservaHabitacion.builder()
                    .reservaId(reserva.getId())
                    .habitacionId(habitacion.getId())
                    .precioAplicado(precio)
                    .tipoAlquiler(reserva.getTipoAlquiler())
                    .estado("PENDIENTE")
                    .build();

            rh = reservaHabitacionRepository.save(rh);

            // Marcar habitación como RESERVADA
            habitacion.setEstado("RESERVADA");
            habitacionRepository.save(habitacion);

            // Registrar huéspedes opcionales
            if (dto.getHuespedes() != null && !dto.getHuespedes().isEmpty()) {
                UUID rhId = rh.getId();
                dto.getHuespedes().forEach(hDto -> {
                    Huesped huesped = Huesped.builder()
                            .reservaHabitacionId(rhId)
                            .nombre(hDto.getNombre())
                            .apellido(hDto.getApellido())
                            .tipoDoc(hDto.getTipoDoc())
                            .numDoc(hDto.getNumDoc())
                            .esTitular(hDto.getEsTitular() != null
                                    ? hDto.getEsTitular() : false)
                            .build();
                    huespedRepository.save(huesped);
                });
            }

            List<HuespedDto> huespedes = huespedRepository
                    .findAllByReservaHabitacionId(rh.getId())
                    .stream()
                    .map(h -> HuespedDto.builder()
                            .id(h.getId())
                            .nombre(h.getNombre())
                            .apellido(h.getApellido())
                            .tipoDoc(h.getTipoDoc())
                            .numDoc(h.getNumDoc())
                            .esTitular(h.getEsTitular())
                            .build())
                    .collect(Collectors.toList());

            return ReservaHabitacionResponseDto.builder()
                    .id(rh.getId())
                    .habitacionId(habitacion.getId())
                    .habitacionNumero(habitacion.getNumero())
                    .tipoAlquiler(rh.getTipoAlquiler())
                    .precioAplicado(precio)
                    .estado(rh.getEstado())
                    .huespedes(huespedes)
                    .build();

        }).collect(Collectors.toList());
    }

    private BigDecimal getPrecio(Habitacion h, String tipoAlquiler) {
        return switch (tipoAlquiler) {
            case "HORA"   -> h.getPrecioHora()   != null ? h.getPrecioHora()
                    : BigDecimal.ZERO;
            case "DIA"    -> h.getPrecioDia()    != null ? h.getPrecioDia()
                    : BigDecimal.ZERO;
            case "NOCHE"  -> h.getPrecioNoche()  != null ? h.getPrecioNoche()
                    : BigDecimal.ZERO;
            default -> throw BusinessException.badRequest(
                    "Tipo de alquiler inválido: " + tipoAlquiler);
        };
    }

    private Reserva findReserva(UUID id) {
        UUID sucursalId = TenantContext.getSucursalId();
        return reservaRepository.findByIdAndSucursalId(id, sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Reserva no encontrada"));
    }

    private ReservaResponseDto buildResponse(
            Reserva reserva,
            Cliente cliente,
            List<ReservaHabitacionResponseDto> habitaciones,
            BigDecimal total) {

        return ReservaResponseDto.builder()
                .id(reserva.getId())
                .sucursalId(reserva.getSucursalId())
                .clienteId(reserva.getClienteId())
                .clienteNombre(cliente != null ? cliente.getNombre() : "")
                .clienteApellido(cliente != null ? cliente.getApellido() : "")
                .clienteNumDoc(cliente != null ? cliente.getNumDoc() : "")
                .tipoAlquiler(reserva.getTipoAlquiler())
                .fechaInicio(reserva.getFechaInicio())
                .fechaFinEstimada(reserva.getFechaFinEstimada())
                .estado(reserva.getEstado())
                .adelanto(reserva.getAdelanto())
                .totalHabitaciones(total)
                .notas(reserva.getNotas())
                .createdAt(reserva.getCreatedAt())
                .habitaciones(habitaciones)
                .build();
    }
}