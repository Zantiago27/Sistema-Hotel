package com.hotel.sistemahotel.modules.comprobante.service;

import com.hotel.sistemahotel.modules.comprobante.dto.ComprobanteDetalleDto;
import com.hotel.sistemahotel.modules.comprobante.dto.ComprobanteResponseDto;
import com.hotel.sistemahotel.modules.comprobante.dto.EmitirComprobanteDto;
import com.hotel.sistemahotel.modules.comprobante.dto.PagoResponseDto;
import com.hotel.sistemahotel.modules.comprobante.entity.Comprobante;
import com.hotel.sistemahotel.modules.comprobante.entity.ComprobanteDetalle;
import com.hotel.sistemahotel.modules.comprobante.entity.Pago;
import com.hotel.sistemahotel.modules.comprobante.repository.ComprobanteDetalleRepository;
import com.hotel.sistemahotel.modules.comprobante.repository.ComprobanteRepository;
import com.hotel.sistemahotel.modules.comprobante.repository.PagoRepository;
import com.hotel.sistemahotel.modules.producto.entity.Consumo;
import com.hotel.sistemahotel.modules.producto.entity.Producto;
import com.hotel.sistemahotel.modules.producto.repository.ConsumoRepository;
import com.hotel.sistemahotel.modules.producto.repository.ProductoRepository;
import com.hotel.sistemahotel.modules.reserva.entity.Reserva;
import com.hotel.sistemahotel.modules.reserva.entity.ReservaHabitacion;
import com.hotel.sistemahotel.modules.reserva.repository.ReservaHabitacionRepository;
import com.hotel.sistemahotel.modules.reserva.repository.ReservaRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComprobanteService {

    private final ComprobanteRepository comprobanteRepository;
    private final ComprobanteDetalleRepository detalleRepository;
    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final ReservaHabitacionRepository reservaHabitacionRepository;
    private final ConsumoRepository consumoRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public ComprobanteResponseDto emitir(EmitirComprobanteDto dto) {
        UUID sucursalId = TenantContext.getSucursalId();
        UUID usuarioId  = TenantContext.getUsuarioId();

        // Validar reserva
        Reserva reserva = reservaRepository
                .findByIdAndSucursalId(dto.getReservaId(), sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Reserva no encontrada"));

        if (!reserva.getEstado().equals("COMPLETADA") &&
                !reserva.getEstado().equals("ACTIVA") &&
                !reserva.getEstado().equals("VENCIDA")) {
            throw BusinessException.badRequest(
                    "Solo se pueden emitir comprobantes de reservas activas o completadas");
        }

        // Construir líneas de detalle
        List<ComprobanteDetalle> detalles = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        // Líneas por habitaciones
        List<ReservaHabitacion> habitaciones =
                reservaHabitacionRepository.findAllByReservaId(reserva.getId());

        for (ReservaHabitacion rh : habitaciones) {
            String descripcion = "Habitación - " + rh.getTipoAlquiler();
            BigDecimal lineaSubtotal = rh.getPrecioAplicado();

            detalles.add(ComprobanteDetalle.builder()
                    .descripcion(descripcion)
                    .cantidad(1)
                    .precioUnit(rh.getPrecioAplicado())
                    .subtotal(lineaSubtotal)
                    .build());

            subtotal = subtotal.add(lineaSubtotal);
        }

        // Líneas por consumos
        List<Consumo> consumos = consumoRepository.findAllByReservaId(reserva.getId());
        for (Consumo c : consumos) {
            String nombreProducto = productoRepository.findById(c.getProductoId())
                    .map(Producto::getNombre)
                    .orElse("Producto");

            BigDecimal lineaSubtotal = c.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(c.getCantidad()));

            detalles.add(ComprobanteDetalle.builder()
                    .descripcion(nombreProducto)
                    .cantidad(c.getCantidad())
                    .precioUnit(c.getPrecioUnitario())
                    .subtotal(lineaSubtotal)
                    .build());

            subtotal = subtotal.add(lineaSubtotal);
        }

        // Serie según tipo
        String serie = getSerie(dto.getTipo());

        // Número correlativo
        Integer ultimoNumero = comprobanteRepository
                .findUltimoNumero(sucursalId, serie);
        Integer nuevoNumero = ultimoNumero + 1;

        // Crear comprobante
        Comprobante comprobante = Comprobante.builder()
                .sucursalId(sucursalId)
                .reservaId(reserva.getId())
                .usuarioId(usuarioId)
                .tipo(dto.getTipo())
                .serie(serie)
                .numero(nuevoNumero)
                .estado("EMITIDO")
                .subtotal(subtotal)
                .igv(BigDecimal.ZERO)
                .total(subtotal)
                .build();

        comprobante = comprobanteRepository.save(comprobante);

        // Guardar detalles
        UUID comprobanteId = comprobante.getId();
        for (ComprobanteDetalle det : detalles) {
            det.setComprobanteId(comprobanteId);
            detalleRepository.save(det);
        }

        // Registrar pago
        BigDecimal montoPagado = dto.getMontoPagado() != null
                ? dto.getMontoPagado() : subtotal;

        Pago pago = Pago.builder()
                .reservaId(reserva.getId())
                .comprobanteId(comprobante.getId())
                .usuarioId(usuarioId)
                .metodo(dto.getMetodoPago())
                .monto(montoPagado)
                .tipo("FINAL")
                .build();

        pagoRepository.save(pago);

        return buildResponse(comprobante);
    }

    public ComprobanteResponseDto anular(UUID id) {
        UUID sucursalId = TenantContext.getSucursalId();

        Comprobante comprobante = comprobanteRepository
                .findByIdAndSucursalId(id, sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Comprobante no encontrado"));

        if (comprobante.getEstado().equals("ANULADO")) {
            throw BusinessException.badRequest("El comprobante ya está anulado");
        }

        comprobante.setEstado("ANULADO");
        comprobanteRepository.save(comprobante);

        return buildResponse(comprobante);
    }

    public List<ComprobanteResponseDto> listar() {
        UUID sucursalId = TenantContext.getSucursalId();
        return comprobanteRepository
                .findAllBySucursalIdOrderByEmitidoAtDesc(sucursalId)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    public List<ComprobanteResponseDto> listarPorReserva(UUID reservaId) {
        return comprobanteRepository.findAllByReservaId(reservaId)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    public ComprobanteResponseDto buscarPorId(UUID id) {
        UUID sucursalId = TenantContext.getSucursalId();
        Comprobante comprobante = comprobanteRepository
                .findByIdAndSucursalId(id, sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Comprobante no encontrado"));
        return buildResponse(comprobante);
    }

    // ── Helpers ──────────────────────────────────────────

    private String getSerie(String tipo) {
        return switch (tipo) {
            case "BOLETA"     -> "B001";
            case "FACTURA"    -> "F001";
            default           -> "NV01"; // NOTA_VENTA
        };
    }

    private ComprobanteResponseDto buildResponse(Comprobante c) {
        List<ComprobanteDetalle> detalles =
                detalleRepository.findAllByComprobanteId(c.getId());

        List<PagoResponseDto> pagos = pagoRepository.findAllByReservaId(c.getReservaId())
                .stream()
                .map(p -> PagoResponseDto.builder()
                        .id(p.getId())
                        .metodo(p.getMetodo())
                        .monto(p.getMonto())
                        .tipo(p.getTipo())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalPagado = pagoRepository.sumMontoByReservaId(c.getReservaId());
        BigDecimal saldo = c.getTotal().subtract(totalPagado);

        return ComprobanteResponseDto.builder()
                .id(c.getId())
                .reservaId(c.getReservaId())
                .tipo(c.getTipo())
                .serie(c.getSerie())
                .numero(c.getNumero())
                .estado(c.getEstado())
                .subtotal(c.getSubtotal())
                .igv(c.getIgv())
                .total(c.getTotal())
                .totalPagado(totalPagado)
                .saldo(saldo)
                .emitidoAt(c.getEmitidoAt())
                .detalles(detalles.stream()
                        .map(d -> ComprobanteDetalleDto.builder()
                                .id(d.getId())
                                .descripcion(d.getDescripcion())
                                .cantidad(d.getCantidad())
                                .precioUnit(d.getPrecioUnit())
                                .subtotal(d.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .pagos(pagos)
                .build();
    }
}