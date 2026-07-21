package com.hotel.sistemahotel.modules.empresa.repository;

import com.hotel.sistemahotel.modules.empresa.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

    boolean existsByEmailAdmin(String emailAdmin);
    boolean existsByRuc(String ruc);
    Optional<Empresa> findByEmailAdmin(String emailAdmin);
}