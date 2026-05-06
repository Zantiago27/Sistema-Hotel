package com.hotel.sistemahotel.modules.trabajador.controller;

import com.hotel.sistemahotel.modules.trabajador.dto.CrearUsuarioDto;
import com.hotel.sistemahotel.modules.trabajador.dto.UsuarioResponseDto;
import com.hotel.sistemahotel.modules.trabajador.service.TrabajadorService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trabajadores")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> crear(
            @Valid @RequestBody CrearUsuarioDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(trabajadorService.crear(dto)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<List<UsuarioResponseDto>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(trabajadorService.listar()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> buscarPorId(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(trabajadorService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CrearUsuarioDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(trabajadorService.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable UUID id) {
        trabajadorService.desactivar(id);
        return ResponseEntity.ok(ApiResponse.ok("Trabajador desactivado", null));
    }
}