package com.hotel.sistemahotel.modules.turno.repository;

import com.hotel.sistemahotel.modules.turno.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, UUID> {

    // Turno abierto actual de una sucursal
    Optional<Turno> findBySucursalIdAndEstado(UUID sucursalId, String estado);

    // Turno abierto de un usuario específico
    Optional<Turno> findByUsuarioIdAndEstado(UUID usuarioId, String estado);

    // Historial de turnos de una sucursal
    List<Turno> findAllBySucursalIdOrderByInicioDesc(UUID sucursalId);

    // Historial de turnos de un usuario
    List<Turno> findAllByUsuarioIdOrderByInicioDesc(UUID usuarioId);
}