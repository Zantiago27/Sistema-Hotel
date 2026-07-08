package com.hotel.sistemahotel.modules.sucursal.repository;

import com.hotel.sistemahotel.modules.sucursal.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal,UUID> {
    Optional<Sucursal> findFirstByEmpresaIdAndIsActiveTrue(UUID empresaId);

    List<Sucursal> findAllByEmpresaIdAndIsActiveTrue(UUID empresaId);

    Optional<Sucursal> findByIdAndEmpresaId(UUID id, UUID empresaId);
}
