package com.hotel.sistemahotel.modules.comprobante.repository;

import com.hotel.sistemahotel.modules.comprobante.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<Pago, UUID> {

    List<Pago> findAllByReservaId(UUID reservaId);

    @Query("""
        SELECT COALESCE(SUM(p.monto), 0)
        FROM Pago p
        WHERE p.reservaId = :reservaId
    """)
    BigDecimal sumMontoByReservaId(@Param("reservaId") UUID reservaId);
}