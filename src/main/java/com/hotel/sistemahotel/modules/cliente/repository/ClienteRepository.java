package com.hotel.sistemahotel.modules.cliente.repository;

import com.hotel.sistemahotel.modules.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByEmpresaIdAndTipoDocAndNumDoc(
            UUID empresaId, String tipoDoc, String numDoc);

    Optional<Cliente> findByIdAndEmpresaId(UUID id, UUID empresaId);

    List<Cliente> findAllByEmpresaId(UUID empresaId);

    List<Cliente> findAllByEmpresaIdAndNombreContainingIgnoreCaseOrEmpresaIdAndApellidoContainingIgnoreCaseOrEmpresaIdAndNumDocContaining(
            UUID empresaId1, String nombre,
            UUID empresaId2, String apellido,
            UUID empresaId3, String numDoc);

    boolean existsByEmpresaIdAndTipoDocAndNumDoc(
            UUID empresaId, String tipoDoc, String numDoc);
}