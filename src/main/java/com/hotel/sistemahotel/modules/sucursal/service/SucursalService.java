package com.hotel.sistemahotel.modules.sucursal.service;

import com.hotel.sistemahotel.modules.sucursal.dto.SucursalRequestDto;
import com.hotel.sistemahotel.modules.sucursal.dto.SucursalResponseDto;
import com.hotel.sistemahotel.modules.sucursal.entity.Sucursal;
import com.hotel.sistemahotel.modules.sucursal.repository.SucursalRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalResponseDto crear(SucursalRequestDto dto) {
        UUID empresaId = TenantContext.getEmpresaId();

        Sucursal sucursal = Sucursal.builder()
                .empresaId(empresaId)
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .isActive(true)
                .build();

        return toDto(sucursalRepository.save(sucursal));
    }

    public List<SucursalResponseDto> listar() {
        UUID empresaId = TenantContext.getEmpresaId();
        return sucursalRepository.findAllByEmpresaIdAndIsActiveTrue(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SucursalResponseDto buscarPorId(UUID id) {
        return toDto(findSucursal(id));
    }

    public SucursalResponseDto actualizar(UUID id, SucursalRequestDto dto) {
        Sucursal sucursal = findSucursal(id);

        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setTelefono(dto.getTelefono());

        return toDto(sucursalRepository.save(sucursal));
    }

    public void desactivar(UUID id) {
        Sucursal sucursal = findSucursal(id);
        sucursal.setIsActive(false);
        sucursalRepository.save(sucursal);
    }

    private Sucursal findSucursal(UUID id) {
        UUID empresaId = TenantContext.getEmpresaId();
        return sucursalRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> BusinessException.notFound("Sucursal no encontrada"));
    }

    private SucursalResponseDto toDto(Sucursal s) {
        return SucursalResponseDto.builder()
                .id(s.getId())
                .empresaId(s.getEmpresaId())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .telefono(s.getTelefono())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
