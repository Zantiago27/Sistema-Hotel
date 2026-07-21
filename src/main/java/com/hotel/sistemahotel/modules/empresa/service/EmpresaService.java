package com.hotel.sistemahotel.modules.empresa.service;

import com.hotel.sistemahotel.modules.empresa.dto.RegistroEmpresaDto;
import com.hotel.sistemahotel.modules.empresa.dto.RegistroEmpresaResponseDto;
import com.hotel.sistemahotel.modules.empresa.entity.Empresa;
import com.hotel.sistemahotel.modules.empresa.repository.EmpresaRepository;
import com.hotel.sistemahotel.modules.sucursal.entity.Sucursal;
import com.hotel.sistemahotel.modules.sucursal.repository.SucursalRepository;
import com.hotel.sistemahotel.modules.trabajador.entity.Usuario;
import com.hotel.sistemahotel.modules.trabajador.repository.UsuarioRepository;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegistroEmpresaResponseDto registrar(RegistroEmpresaDto dto) {

        // Validar unicidad
        if (empresaRepository.existsByEmailAdmin(dto.getAdminEmail())) {
            throw BusinessException.badRequest(
                    "Ya existe una empresa registrada con ese email");
        }

        if (empresaRepository.existsByRuc(dto.getRuc())) {
            throw BusinessException.badRequest(
                    "Ya existe una empresa registrada con ese RUC");
        }

        // 1. Crear empresa
        Empresa empresa = Empresa.builder()
                .nombre(dto.getNombreEmpresa())
                .ruc(dto.getRuc())
                .emailAdmin(dto.getAdminEmail())
                .plan("BASICO")
                .isActive(true)
                .build();

        empresa = empresaRepository.save(empresa);

        // 2. Crear sucursal principal
        Sucursal sucursal = Sucursal.builder()
                .empresaId(empresa.getId())
                .nombre(dto.getNombreSucursal())
                .direccion(dto.getDireccionSucursal())
                .telefono(dto.getTelefonoSucursal())
                .isActive(true)
                .build();

        sucursal = sucursalRepository.save(sucursal);

        // 3. Crear usuario administrador
        Usuario admin = Usuario.builder()
                .empresaId(empresa.getId())
                .sucursalId(sucursal.getId())
                .nombre(dto.getAdminNombre())
                .apellido(dto.getAdminApellido())
                .email(dto.getAdminEmail())
                .passwordHash(passwordEncoder.encode(dto.getAdminPassword()))
                .rol("ADMIN_EMPRESA")
                .isActive(true)
                .build();

        admin = usuarioRepository.save(admin);

        return RegistroEmpresaResponseDto.builder()
                .empresaId(empresa.getId())
                .sucursalId(sucursal.getId())
                .usuarioId(admin.getId())
                .nombreEmpresa(empresa.getNombre())
                .nombreSucursal(sucursal.getNombre())
                .adminEmail(admin.getEmail())
                .mensaje("Empresa registrada exitosamente. Ya puedes iniciar sesión.")
                .build();
    }
}