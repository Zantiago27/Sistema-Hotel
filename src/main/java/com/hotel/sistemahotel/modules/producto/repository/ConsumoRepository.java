package com.hotel.sistemahotel.modules.producto.repository;

import com.hotel.sistemahotel.modules.producto.entity.Consumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, UUID> {

    List<Consumo> findAllByReservaId(UUID reservaId);

    void deleteById(UUID id);
}
