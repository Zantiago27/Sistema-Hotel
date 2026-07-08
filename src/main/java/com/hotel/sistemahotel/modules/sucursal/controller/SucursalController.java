package com.hotel.sistemahotel.modules.sucursal.controller;
import com.hotel.sistemahotel.modules.sucursal.dto.SucursalRequestDto;
import com.hotel.sistemahotel.modules.sucursal.dto.SucursalResponseDto;
import com.hotel.sistemahotel.modules.sucursal.service.SucursalService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<SucursalResponseDto>> crear(
            @Valid @RequestBody SucursalRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(sucursalService.crear(dto)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<SucursalResponseDto>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(sucursalService.listar()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<SucursalResponseDto>> buscarPorId(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(sucursalService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<SucursalResponseDto>> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody SucursalRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(sucursalService.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA')")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable UUID id) {
        sucursalService.desactivar(id);
        return ResponseEntity.ok(ApiResponse.ok("Sucursal desactivada", null));
    }
}
