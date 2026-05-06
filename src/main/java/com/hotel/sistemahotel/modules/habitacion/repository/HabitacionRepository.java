package com.hotel.sistemahotel.modules.habitacion.repository;

import com.hotel.sistemahotel.modules.habitacion.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, UUID> {

    List<Habitacion> findAllBySucursalIdAndIsActiveTrue(UUID sucursalId);

    List<Habitacion> findAllBySucursalIdAndEstadoAndIsActiveTrue(UUID sucursalId, String estado);

    Optional<Habitacion> findByIdAndSucursalId(UUID id, UUID sucursalId);

    boolean existsByNumeroAndSucursalId(String numero, UUID sucursalId);
}