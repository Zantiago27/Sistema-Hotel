package com.hotel.sistemahotel.modules.comprobante.repository;
import com.hotel.sistemahotel.modules.comprobante.entity.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, UUID> {

    List<Comprobante> findAllBySucursalIdOrderByEmitidoAtDesc(UUID sucursalId);

    List<Comprobante> findAllByReservaId(UUID reservaId);

    Optional<Comprobante> findByIdAndSucursalId(UUID id, UUID sucursalId);

    // Obtiene el último número usado para una serie en una sucursal
    @Query("""
        SELECT COALESCE(MAX(c.numero), 0)
        FROM Comprobante c
        WHERE c.sucursalId = :sucursalId
        AND c.serie = :serie
    """)
    Integer findUltimoNumero(
            @Param("sucursalId") UUID sucursalId,
            @Param("serie") String serie);
}