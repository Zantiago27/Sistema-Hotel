package com.hotel.sistemahotel.modules.comprobante.controller;

import com.hotel.sistemahotel.modules.comprobante.dto.ComprobanteResponseDto;
import com.hotel.sistemahotel.modules.comprobante.dto.EmitirComprobanteDto;
import com.hotel.sistemahotel.modules.comprobante.service.ComprobanteService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comprobantes")
@RequiredArgsConstructor
public class ComprobanteController {

    private final ComprobanteService comprobanteService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ComprobanteResponseDto>> emitir(
            @Valid @RequestBody EmitirComprobanteDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(comprobanteService.emitir(dto)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ComprobanteResponseDto>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(comprobanteService.listar()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ComprobanteResponseDto>> buscarPorId(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(comprobanteService.buscarPorId(id)));
    }

    @GetMapping("/reserva/{reservaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ComprobanteResponseDto>>> porReserva(
            @PathVariable UUID reservaId) {
        return ResponseEntity.ok(ApiResponse.ok(
                comprobanteService.listarPorReserva(reservaId)));
    }

    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasAnyRole('ADMIN_EMPRESA', 'GERENTE')")
    public ResponseEntity<ApiResponse<ComprobanteResponseDto>> anular(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(comprobanteService.anular(id)));
    }
}