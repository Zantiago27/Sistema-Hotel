package com.hotel.sistemahotel.modules.comprobante.repository;

import com.hotel.sistemahotel.modules.comprobante.entity.ComprobanteDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComprobanteDetalleRepository extends JpaRepository<ComprobanteDetalle, UUID> {

    List<ComprobanteDetalle> findAllByComprobanteId(UUID comprobanteId);
}