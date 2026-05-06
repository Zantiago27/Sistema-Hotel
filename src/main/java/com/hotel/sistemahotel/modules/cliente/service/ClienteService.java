package com.hotel.sistemahotel.modules.cliente.service;

import com.hotel.sistemahotel.modules.cliente.dto.ClienteRequestDto;
import com.hotel.sistemahotel.modules.cliente.dto.ClienteResponseDto;
import com.hotel.sistemahotel.modules.cliente.entity.Cliente;
import com.hotel.sistemahotel.modules.cliente.repository.ClienteRepository;
import com.hotel.sistemahotel.shared.TenantContext;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteResponseDto crear(ClienteRequestDto dto) {
        UUID empresaId = TenantContext.getEmpresaId();

        if (clienteRepository.existsByEmpresaIdAndTipoDocAndNumDoc(
                empresaId, dto.getTipoDoc(), dto.getNumDoc())) {
            throw BusinessException.badRequest(
                    "Ya existe un cliente con ese documento en esta empresa");
        }

        Cliente cliente = Cliente.builder()
                .empresaId(empresaId)
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .tipoDoc(dto.getTipoDoc())
                .numDoc(dto.getNumDoc())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .build();

        return toDto(clienteRepository.save(cliente));
    }

    public List<ClienteResponseDto> listar() {
        UUID empresaId = TenantContext.getEmpresaId();
        return clienteRepository.findAllByEmpresaId(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ClienteResponseDto> buscar(String termino) {
        UUID empresaId = TenantContext.getEmpresaId();
        return clienteRepository
                .findAllByEmpresaIdAndNombreContainingIgnoreCaseOrEmpresaIdAndApellidoContainingIgnoreCaseOrEmpresaIdAndNumDocContaining(
                        empresaId, termino,
                        empresaId, termino,
                        empresaId, termino)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ClienteResponseDto buscarPorId(UUID id) {
        return toDto(findCliente(id));
    }

    public ClienteResponseDto buscarPorDocumento(String tipoDoc, String numDoc) {
        UUID empresaId = TenantContext.getEmpresaId();
        Cliente cliente = clienteRepository
                .findByEmpresaIdAndTipoDocAndNumDoc(empresaId, tipoDoc, numDoc)
                .orElseThrow(() -> BusinessException.notFound("Cliente no encontrado"));
        return toDto(cliente);
    }

    public ClienteResponseDto actualizar(UUID id, ClienteRequestDto dto) {
        Cliente cliente = findCliente(id);

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());

        return toDto(clienteRepository.save(cliente));
    }

    private Cliente findCliente(UUID id) {
        UUID empresaId = TenantContext.getEmpresaId();
        return clienteRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> BusinessException.notFound("Cliente no encontrado"));
    }

    private ClienteResponseDto toDto(Cliente c) {
        return ClienteResponseDto.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .apellido(c.getApellido())
                .tipoDoc(c.getTipoDoc())
                .numDoc(c.getNumDoc())
                .telefono(c.getTelefono())
                .email(c.getEmail())
                .createdAt(c.getCreatedAt())
                .build();
    }
}