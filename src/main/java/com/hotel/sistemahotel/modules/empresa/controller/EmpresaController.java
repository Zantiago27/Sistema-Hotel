package com.hotel.sistemahotel.modules.empresa.controller;

import com.hotel.sistemahotel.modules.empresa.dto.RegistroEmpresaDto;
import com.hotel.sistemahotel.modules.empresa.dto.RegistroEmpresaResponseDto;
import com.hotel.sistemahotel.modules.empresa.service.EmpresaService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<RegistroEmpresaResponseDto>> registro(
            @Valid @RequestBody RegistroEmpresaDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(empresaService.registrar(dto)));
    }
}