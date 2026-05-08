package com.hotel.sistemahotel.modules.producto.repository;

import com.hotel.sistemahotel.modules.producto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    List<Producto> findAllBySucursalIdAndIsActiveTrue(UUID sucursalId);

    List<Producto> findAllBySucursalIdAndCategoriaAndIsActiveTrue(
            UUID sucursalId, String categoria);

    Optional<Producto> findByIdAndSucursalId(UUID id, UUID sucursalId);

    boolean existsByNombreAndSucursalId(String nombre, UUID sucursalId);
}
