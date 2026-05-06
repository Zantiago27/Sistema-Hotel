package com.hotel.sistemahotel.modules.auth.service;

import com.hotel.sistemahotel.modules.auth.dto.LoginRequestDto;
import com.hotel.sistemahotel.modules.auth.dto.LoginResponseDto;
import com.hotel.sistemahotel.modules.trabajador.entity.Usuario;
import com.hotel.sistemahotel.modules.trabajador.repository.UsuarioRepository;
import com.hotel.sistemahotel.security.JwtUtil;
import com.hotel.sistemahotel.shared.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDto login(LoginRequestDto dto) {

        // Buscar usuario por email en cualquier empresa
        // El email es único por empresa, pero para el login
        // buscamos en todas y validamos contraseña
        List<Usuario> candidatos = usuarioRepository
                .findAllByEmailAndIsActiveTrue(dto.getEmail());

        Usuario usuario = candidatos.stream()
                .filter(u -> passwordEncoder.matches(dto.getPassword(), u.getPasswordHash()))
                .findFirst()
                .orElseThrow(() -> BusinessException.badRequest("Credenciales inválidas"));

        String token = jwtUtil.generateToken(
                usuario.getId(),
                usuario.getEmpresaId(),
                usuario.getSucursalId(),
                usuario.getRol()
        );

        return LoginResponseDto.builder()
                .token(token)
                .rol(usuario.getRol())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .usuarioId(usuario.getId())
                .empresaId(usuario.getEmpresaId())
                .sucursalId(usuario.getSucursalId())
                .build();
    }
}