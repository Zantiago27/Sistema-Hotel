package com.hotel.sistemahotel.modules.habitacion.controller;

import com.hotel.sistemahotel.modules.habitacion.dto.HabitacionRequestDto;
import com.hotel.sistemahotel.modules.habitacion.dto.HabitacionResponseDto;
import com.hotel.sistemahotel.modules.habitacion.dto.TipoHabitacionDto;
import com.hotel.sistemahotel.modules.habitacion.service.HabitacionService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    // ── Tipos ────────────────────────────────────────────

    @PostMapping("/tipos")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<TipoHabitacionDto>> crearTipo(
            @Valid @RequestBody TipoHabitacionDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.crearTipo(dto)));
    }

    @GetMapping("/tipos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TipoHabitacionDto>>> listarTipos() {
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.listarTipos()));
    }

    // ── Habitaciones ─────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<HabitacionResponseDto>> crear(
            @Valid @RequestBody HabitacionRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.crear(dto)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<HabitacionResponseDto>>> listar(
            @RequestParam(required = false) String estado) {
        if (estado != null) {
            return ResponseEntity.ok(ApiResponse.ok(habitacionService.listarPorEstado(estado)));
        }
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.listar()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<HabitacionResponseDto>> buscarPorId(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<HabitacionResponseDto>> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody HabitacionRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.actualizar(id, dto)));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<HabitacionResponseDto>> cambiarEstado(
            @PathVariable UUID id,
            @RequestParam String estado) {
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.cambiarEstado(id, estado)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable UUID id) {
        habitacionService.desactivar(id);
        return ResponseEntity.ok(ApiResponse.ok("Habitación desactivada", null));
    }

    //PARA TIPO DE HABITACION
    @PutMapping("/tipos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<TipoHabitacionDto>> actualizarTipo(
            @PathVariable UUID id,
            @Valid @RequestBody TipoHabitacionDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(habitacionService.actualizarTipo(id, dto)));
    }

    @DeleteMapping("/tipos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<Void>> desactivarTipo(@PathVariable UUID id) {
        habitacionService.eliminarTipo(id);
        return ResponseEntity.ok(ApiResponse.ok("Tipo eliminado", null));
    }


}