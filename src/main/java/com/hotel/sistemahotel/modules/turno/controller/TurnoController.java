package com.hotel.sistemahotel.modules.turno.controller;

import com.hotel.sistemahotel.modules.turno.dto.*;
import com.hotel.sistemahotel.modules.turno.service.TurnoService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @PostMapping("/abrir")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TurnoResponseDto>> abrir(
            @Valid @RequestBody AbrirTurnoDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.abrir(dto)));
    }

    @PostMapping("/cerrar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TurnoResponseDto>> cerrar(
            @Valid @RequestBody CerrarTurnoDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.cerrar(dto)));
    }

    @GetMapping("/actual")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TurnoResponseDto>> actual() {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.turnoActual()));
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN_EMPRESA')")
    public ResponseEntity<ApiResponse<List<TurnoResponseDto>>> historial() {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.historial()));
    }

    @GetMapping("/mi-historial")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TurnoResponseDto>>> miHistorial() {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.miHistorial()));
    }
}