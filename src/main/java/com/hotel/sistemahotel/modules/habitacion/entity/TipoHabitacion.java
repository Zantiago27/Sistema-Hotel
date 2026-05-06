package com.hotel.sistemahotel.modules.habitacion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tipo_habitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sucursal_id", nullable = false)
    private UUID sucursalId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "capacidad_max", nullable = false)
    private Integer capacidadMax = 2;
}