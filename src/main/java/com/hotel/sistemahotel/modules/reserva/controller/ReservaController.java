package com.hotel.sistemahotel.modules.reserva.controller;

import com.hotel.sistemahotel.modules.reserva.dto.CrearReservaDto;
import com.hotel.sistemahotel.modules.reserva.dto.ReservaResponseDto;
import com.hotel.sistemahotel.modules.reserva.service.ReservaService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservaResponseDto>> crear(
            @Valid @RequestBody CrearReservaDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.crear(dto)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ReservaResponseDto>>> listar(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.listar(estado)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservaResponseDto>> detalle(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.getDetalle(id)));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ReservaResponseDto>>> porCliente(
            @PathVariable UUID clienteId) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.listarPorCliente(clienteId)));
    }

    @PostMapping("/{id}/checkin")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservaResponseDto>> checkin(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.hacerCheckin(id)));
    }

    @PostMapping("/{id}/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservaResponseDto>> checkout(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.hacerCheckout(id)));
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservaResponseDto>> cancelar(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.cancelar(id)));
    }
}