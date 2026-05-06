package com.hotel.sistemahotel.modules.cliente.controller;

import com.hotel.sistemahotel.modules.cliente.dto.ClienteRequestDto;
import com.hotel.sistemahotel.modules.cliente.dto.ClienteResponseDto;
import com.hotel.sistemahotel.modules.cliente.service.ClienteService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClienteResponseDto>> crear(
            @Valid @RequestBody ClienteRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.crear(dto)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ClienteResponseDto>>> listar(
            @RequestParam(required = false) String buscar) {
        if (buscar != null && !buscar.isBlank()) {
            return ResponseEntity.ok(ApiResponse.ok(clienteService.buscar(buscar)));
        }
        return ResponseEntity.ok(ApiResponse.ok(clienteService.listar()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClienteResponseDto>> buscarPorId(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.buscarPorId(id)));
    }

    @GetMapping("/documento")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClienteResponseDto>> buscarPorDocumento(
            @RequestParam String tipoDoc,
            @RequestParam String numDoc) {
        return ResponseEntity.ok(ApiResponse.ok(
                clienteService.buscarPorDocumento(tipoDoc, numDoc)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClienteResponseDto>> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.actualizar(id, dto)));
    }
}