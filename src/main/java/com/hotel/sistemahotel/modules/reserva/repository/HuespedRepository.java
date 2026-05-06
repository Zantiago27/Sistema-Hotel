package com.hotel.sistemahotel.modules.reserva.repository;

import com.hotel.sistemahotel.modules.reserva.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, UUID> {

    List<Huesped> findAllByReservaHabitacionId(UUID reservaHabitacionId);

    void deleteAllByReservaHabitacionId(UUID reservaHabitacionId);
}
