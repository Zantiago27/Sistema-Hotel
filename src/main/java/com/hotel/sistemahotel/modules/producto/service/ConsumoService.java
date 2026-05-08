package com.hotel.sistemahotel.modules.producto.service;

import com.hotel.sistemahotel.modules.producto.dto.ConsumoRequestDto;
import com.hotel.sistemahotel.modules.producto.dto.ConsumoResponseDto;
import com.hotel.sistemahotel.modules.producto.entity.Consumo;
import com.hotel.sistemahotel.modules.producto.entity.Producto;
import com.hotel.sistemahotel.modules.producto.repository.ConsumoRepository;
import com.hotel.sistemahotel.modules.producto.repository.ProductoRepository;
import com.hotel.sistemahotel.modules.reserva.repository.ReservaRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumoService {

    private final ConsumoRepository consumoRepository;
    private final ProductoRepository productoRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    public ConsumoResponseDto agregar(UUID reservaId, ConsumoRequestDto dto) {
        UUID sucursalId = TenantContext.getSucursalId();
        UUID usuarioId  = TenantContext.getUsuarioId();

        // Validar que la reserva existe y está activa
        reservaRepository.findByIdAndSucursalId(reservaId, sucursalId)
                .filter(r -> r.getEstado().equals("ACTIVA") ||
                        r.getEstado().equals("PENDIENTE"))
                .orElseThrow(() -> BusinessException.badRequest(
                        "La reserva no existe o no está activa"));

        // Validar producto
        Producto producto = productoRepository
                .findByIdAndSucursalId(dto.getProductoId(), sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Producto no encontrado"));

        if (producto.getStock() < dto.getCantidad()) {
            throw BusinessException.badRequest(
                    "Stock insuficiente. Disponible: " + producto.getStock());
        }

        // Descontar stock
        producto.setStock(producto.getStock() - dto.getCantidad());
        productoRepository.save(producto);

        // Registrar consumo
        Consumo consumo = Consumo.builder()
                .reservaId(reservaId)
                .productoId(producto.getId())
                .usuarioId(usuarioId)
                .cantidad(dto.getCantidad())
                .precioUnitario(producto.getPrecio())
                .build();

        return toDto(consumoRepository.save(consumo), producto.getNombre());
    }

    public List<ConsumoResponseDto> listarPorReserva(UUID reservaId) {
        UUID sucursalId = TenantContext.getSucursalId();

        reservaRepository.findByIdAndSucursalId(reservaId, sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Reserva no encontrada"));

        return consumoRepository.findAllByReservaId(reservaId)
                .stream()
                .map(c -> {
                    String nombre = productoRepository.findById(c.getProductoId())
                            .map(Producto::getNombre)
                            .orElse("");
                    return toDto(c, nombre);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(UUID reservaId, UUID consumoId) {
        UUID sucursalId = TenantContext.getSucursalId();

        reservaRepository.findByIdAndSucursalId(reservaId, sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Reserva no encontrada"));

        Consumo consumo = consumoRepository.findById(consumoId)
                .filter(c -> c.getReservaId().equals(reservaId))
                .orElseThrow(() -> BusinessException.notFound("Consumo no encontrado"));

        // Devolver stock
        productoRepository.findById(consumo.getProductoId()).ifPresent(p -> {
            p.setStock(p.getStock() + consumo.getCantidad());
            productoRepository.save(p);
        });

        consumoRepository.deleteById(consumoId);
    }

    private ConsumoResponseDto toDto(Consumo c, String productoNombre) {
        BigDecimal subtotal = c.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(c.getCantidad()));
        return ConsumoResponseDto.builder()
                .id(c.getId())
                .reservaId(c.getReservaId())
                .productoId(c.getProductoId())
                .productoNombre(productoNombre)
                .cantidad(c.getCantidad())
                .precioUnitario(c.getPrecioUnitario())
                .subtotal(subtotal)
                .createdAt(c.getCreatedAt())
                .build();
    }
}