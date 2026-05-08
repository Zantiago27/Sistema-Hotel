package com.hotel.sistemahotel.modules.producto.service;

import com.hotel.sistemahotel.modules.producto.dto.ProductoRequestDto;
import com.hotel.sistemahotel.modules.producto.dto.ProductoResponseDto;
import com.hotel.sistemahotel.modules.producto.entity.Producto;
import com.hotel.sistemahotel.modules.producto.repository.ProductoRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoResponseDto crear(ProductoRequestDto dto) {
        UUID sucursalId = TenantContext.getSucursalId();

        if (productoRepository.existsByNombreAndSucursalId(dto.getNombre(), sucursalId)) {
            throw BusinessException.badRequest("Ya existe un producto con ese nombre");
        }

        Producto producto = Producto.builder()
                .sucursalId(sucursalId)
                .nombre(dto.getNombre())
                .categoria(dto.getCategoria())
                .precio(dto.getPrecio())
                .stock(dto.getStock() != null ? dto.getStock() : 0)
                .isActive(true)
                .build();

        return toDto(productoRepository.save(producto));
    }

    public List<ProductoResponseDto> listar(String categoria) {
        UUID sucursalId = TenantContext.getSucursalId();

        if (categoria != null && !categoria.isBlank()) {
            return productoRepository
                    .findAllBySucursalIdAndCategoriaAndIsActiveTrue(sucursalId, categoria)
                    .stream().map(this::toDto).collect(Collectors.toList());
        }

        return productoRepository.findAllBySucursalIdAndIsActiveTrue(sucursalId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductoResponseDto buscarPorId(UUID id) {
        return toDto(findProducto(id));
    }

    public ProductoResponseDto actualizar(UUID id, ProductoRequestDto dto) {
        Producto producto = findProducto(id);

        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock() != null ? dto.getStock() : producto.getStock());

        return toDto(productoRepository.save(producto));
    }

    public ProductoResponseDto ajustarStock(UUID id, int cantidad) {
        Producto producto = findProducto(id);
        int nuevoStock = producto.getStock() + cantidad;

        if (nuevoStock < 0) {
            throw BusinessException.badRequest("Stock insuficiente");
        }

        producto.setStock(nuevoStock);
        return toDto(productoRepository.save(producto));
    }

    public void desactivar(UUID id) {
        Producto producto = findProducto(id);
        producto.setIsActive(false);
        productoRepository.save(producto);
    }

    private Producto findProducto(UUID id) {
        UUID sucursalId = TenantContext.getSucursalId();
        return productoRepository.findByIdAndSucursalId(id, sucursalId)
                .orElseThrow(() -> BusinessException.notFound("Producto no encontrado"));
    }

    private ProductoResponseDto toDto(Producto p) {
        return ProductoResponseDto.builder()
                .id(p.getId())
                .sucursalId(p.getSucursalId())
                .nombre(p.getNombre())
                .categoria(p.getCategoria())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .isActive(p.getIsActive())
                .build();
    }
}