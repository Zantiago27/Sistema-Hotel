package com.hotel.sistemahotel.modules.reserva.repository;

import com.hotel.sistemahotel.modules.reserva.entity.ReservaHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, UUID> {

    List<ReservaHabitacion> findAllByReservaId(UUID reservaId);

    Optional<ReservaHabitacion> findByReservaIdAndHabitacionId(
            UUID reservaId, UUID habitacionId);

    // Verifica si una habitación está ocupada en un rango de fechas
    @Query("""
        SELECT COUNT(rh) > 0 FROM ReservaHabitacion rh
        WHERE rh.habitacionId = :habitacionId
        AND rh.estado IN ('PENDIENTE', 'ACTIVA')
        AND rh.checkinReal < :fin
        AND rh.checkinReal IS NULL
    """)
    boolean estaOcupadaEnRango(
            @Param("habitacionId") UUID habitacionId,
            @Param("fin") OffsetDateTime fin);
}