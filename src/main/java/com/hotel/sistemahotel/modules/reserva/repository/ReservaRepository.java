package com.hotel.sistemahotel.modules.reserva.repository;

import com.hotel.sistemahotel.modules.reserva.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    List<Reserva> findAllBySucursalIdOrderByCreatedAtDesc(UUID sucursalId);

    List<Reserva> findAllBySucursalIdAndEstadoOrderByCreatedAtDesc(
            UUID sucursalId, String estado);

    List<Reserva> findAllBySucursalIdAndClienteIdOrderByCreatedAtDesc(
            UUID sucursalId, UUID clienteId);

    Optional<Reserva> findByIdAndSucursalId(UUID id, UUID sucursalId);

    // Reservas vencidas para el job de alertas
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.sucursalId = :sucursalId
        AND r.estado = 'ACTIVA'
        AND r.fechaFinEstimada < :ahora
    """)
    List<Reserva> findReservasVencidas(
            @Param("sucursalId") UUID sucursalId,
            @Param("ahora") OffsetDateTime ahora);

    // Todas las vencidas (para el scheduler global)
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.estado = 'ACTIVA'
        AND r.fechaFinEstimada < :ahora
    """)
    List<Reserva> findTodasReservasVencidas(@Param("ahora") OffsetDateTime ahora);
}