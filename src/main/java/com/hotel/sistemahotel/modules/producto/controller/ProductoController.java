package com.hotel.sistemahotel.modules.producto.controller;

import com.hotel.sistemahotel.modules.producto.dto.ProductoRequestDto;
import com.hotel.sistemahotel.modules.producto.dto.ProductoResponseDto;
import com.hotel.sistemahotel.modules.producto.service.ProductoService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<ProductoResponseDto>> crear(
            @Valid @RequestBody ProductoRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.crear(dto)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ProductoResponseDto>>> listar(
            @RequestParam(required = false) String categoria) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.listar(categoria)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ProductoResponseDto>> buscarPorId(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<ProductoResponseDto>> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ProductoRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.actualizar(id, dto)));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<ProductoResponseDto>> ajustarStock(
            @PathVariable UUID id,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.ajustarStock(id, cantidad)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable UUID id) {
        productoService.desactivar(id);
        return ResponseEntity.ok(ApiResponse.ok("Producto desactivado", null));
    }
}