package com.hotel.sistemahotel.modules.producto.controller;

import com.hotel.sistemahotel.modules.producto.dto.ConsumoRequestDto;
import com.hotel.sistemahotel.modules.producto.dto.ConsumoResponseDto;
import com.hotel.sistemahotel.modules.producto.service.ConsumoService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservas/{reservaId}/consumos")
@RequiredArgsConstructor
public class ConsumoController {

    private final ConsumoService consumoService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ConsumoResponseDto>> agregar(
            @PathVariable UUID reservaId,
            @Valid @RequestBody ConsumoRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(consumoService.agregar(reservaId, dto)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ConsumoResponseDto>>> listar(
            @PathVariable UUID reservaId) {
        return ResponseEntity.ok(ApiResponse.ok(consumoService.listarPorReserva(reservaId)));
    }

    @DeleteMapping("/{consumoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable UUID reservaId,
            @PathVariable UUID consumoId) {
        consumoService.eliminar(reservaId, consumoId);
        return ResponseEntity.ok(ApiResponse.ok("Consumo eliminado", null));
    }
}