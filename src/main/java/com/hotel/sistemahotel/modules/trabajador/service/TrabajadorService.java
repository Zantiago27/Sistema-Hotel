package com.hotel.sistemahotel.modules.trabajador.service;

import com.hotel.sistemahotel.modules.trabajador.dto.CrearUsuarioDto;
import com.hotel.sistemahotel.modules.trabajador.dto.UsuarioResponseDto;
import com.hotel.sistemahotel.modules.trabajador.entity.Usuario;
import com.hotel.sistemahotel.modules.trabajador.repository.UsuarioRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrabajadorService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    //creamos un trabajador
    public UsuarioResponseDto crear(CrearUsuarioDto dto) {
        UUID empresaId = TenantContext.getEmpresaId();
        UUID sucursalId = TenantContext.getSucursalId();

        if (usuarioRepository.existsByEmailAndEmpresaId(dto.getEmail(), empresaId)) {
            throw BusinessException.badRequest("Ya existe un usuario con ese email en esta empresa");
        }

        Usuario usuario = Usuario.builder()
                .empresaId(empresaId)
                //.sucursalId(dto.getSucursalId())
                .sucursalId(sucursalId)
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol())
                .isActive(true)
                .build();

        return toDto(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDto> listar() {
        UUID empresaId = TenantContext.getEmpresaId();
        return usuarioRepository.findAllByEmpresaIdAndIsActiveTrue(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto buscarPorId(UUID id) {
        return toDto(findUsuario(id));
    }

    public UsuarioResponseDto actualizar(UUID id, CrearUsuarioDto dto) {
        Usuario usuario = findUsuario(id);

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setRol(dto.getRol());
        usuario.setSucursalId(dto.getSucursalId());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        return toDto(usuarioRepository.save(usuario));
    }

    public void desactivar(UUID id) {
        Usuario usuario = findUsuario(id);
        usuario.setIsActive(false);
        usuarioRepository.save(usuario);
    }

    private Usuario findUsuario(UUID id) {
        UUID empresaId = TenantContext.getEmpresaId();
        return usuarioRepository.findById(id)
                .filter(u -> u.getEmpresaId().equals(empresaId))
                .orElseThrow(() -> BusinessException.notFound("Usuario no encontrado"));
    }

    private UsuarioResponseDto toDto(Usuario u) {
        return modelMapper.map(u, UsuarioResponseDto.class);
    }
}
