package com.hotel.sistemahotel.modules.habitacion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "habitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sucursal_id", nullable = false)
    private UUID sucursalId;

    @Column(name = "tipo_id", nullable = false)
    private UUID tipoId;

    @Column(nullable = false, length = 10)
    private String numero;

    @Column(length = 10)
    private String piso;

    @Column(nullable = false, length = 20)
    private String estado = "DISPONIBLE";

    @Column(name = "precio_hora", precision = 10, scale = 2)
    private BigDecimal precioHora;

    @Column(name = "precio_dia", precision = 10, scale = 2)
    private BigDecimal precioDia;

    @Column(name = "precio_noche", precision = 10, scale = 2)
    private BigDecimal precioNoche;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}