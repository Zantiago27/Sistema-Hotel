package com.hotel.sistemahotel.modules.auth.controller;

import com.hotel.sistemahotel.modules.auth.dto.LoginRequestDto;
import com.hotel.sistemahotel.modules.auth.dto.LoginResponseDto;
import com.hotel.sistemahotel.modules.auth.service.AuthService;
import com.hotel.sistemahotel.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    //private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(dto)));
    }

    //enpoint temporal para desarrollo quital al final
    /*@GetMapping("/hash")
    public ResponseEntity<String> generarHash(@RequestParam String password) {
        return ResponseEntity.ok(passwordEncoder.encode(password));
    }*/

}
