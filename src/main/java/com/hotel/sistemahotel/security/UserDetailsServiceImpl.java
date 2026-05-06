package com.hotel.sistemahotel.security;

import com.hotel.sistemahotel.modules.trabajador.entity.Usuario;
import com.hotel.sistemahotel.modules.trabajador.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService { //carga el usuario desde BD

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String usuarioId) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findById(UUID.fromString(usuarioId))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getId().toString(),
                usuario.getPasswordHash(),
                usuario.getIsActive(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
        );
    }
}