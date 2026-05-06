package com.hotel.sistemahotel.modules.reserva.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "huesped")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reserva_habitacion_id", nullable = false)
    private UUID reservaHabitacionId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(name = "tipo_doc", length = 10)
    private String tipoDoc;

    @Column(name = "num_doc", length = 20)
    private String numDoc;

    @Column(name = "es_titular", nullable = false)
    private Boolean esTitular = false;
}