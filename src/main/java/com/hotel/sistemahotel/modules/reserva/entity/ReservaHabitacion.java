package com.hotel.sistemahotel.modules.reserva.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reserva_habitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reserva_id", nullable = false)
    private UUID reservaId;

    @Column(name = "habitacion_id", nullable = false)
    private UUID habitacionId;

    @Column(name = "precio_aplicado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAplicado;

    @Column(name = "tipo_alquiler", nullable = false, length = 10)
    private String tipoAlquiler;

    @Column(name = "checkin_real")
    private OffsetDateTime checkinReal;

    @Column(name = "checkout_real")
    private OffsetDateTime checkoutReal;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";
}