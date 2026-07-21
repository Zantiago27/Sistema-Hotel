package com.hotel.sistemahotel.modules.consulta.controller;

import com.hotel.sistemahotel.modules.consulta.dto.DniResponseDto;
import com.hotel.sistemahotel.modules.consulta.dto.RucResponseDto;
import com.hotel.sistemahotel.modules.consulta.service.ConsultaService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consulta")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping("/dni/{dni}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DniResponseDto>> consultarDni(
            @PathVariable String dni) {
        return ResponseEntity.ok(ApiResponse.ok(consultaService.consultarDni(dni)));
    }

    @GetMapping("/ruc/{ruc}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<RucResponseDto>> consultarRuc(
            @PathVariable String ruc) {
        return ResponseEntity.ok(ApiResponse.ok(consultaService.consultarRuc(ruc)));
    }
}