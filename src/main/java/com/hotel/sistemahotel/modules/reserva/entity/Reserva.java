package com.hotel.sistemahotel.modules.reserva.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sucursal_id", nullable = false)
    private UUID sucursalId;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "tipo_alquiler", nullable = false, length = 10)
    private String tipoAlquiler;

    @Column(name = "fecha_inicio", nullable = false)
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_fin_estimada", nullable = false)
    private OffsetDateTime fechaFinEstimada;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(precision = 10, scale = 2)
    private BigDecimal adelanto = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}
