package com.hotel.sistemahotel.modules.trabajador.repository;

import com.hotel.sistemahotel.modules.trabajador.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmailAndEmpresaId(String email, UUID empresaId);

    List<Usuario> findAllByEmpresaIdAndIsActiveTrue(UUID empresaId);

    List<Usuario> findAllBySucursalIdAndIsActiveTrue(UUID sucursalId);

    boolean existsByEmailAndEmpresaId(String email, UUID empresaId);

    // buscar por email
    List<Usuario> findAllByEmailAndIsActiveTrue(String email);
}