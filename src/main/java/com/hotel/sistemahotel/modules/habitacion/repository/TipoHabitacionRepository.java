package com.hotel.sistemahotel.modules.habitacion.repository;

import com.hotel.sistemahotel.modules.habitacion.entity.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, UUID> {

    List<TipoHabitacion> findAllBySucursalId(UUID sucursalId);

    Optional<TipoHabitacion> findByIdAndSucursalId(UUID id, UUID sucursalId);

    boolean existsByNombreAndSucursalId(String nombre, UUID sucursalId);
}