package com.hotel.sistemahotel.modules.cliente.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "empresa_id", nullable = false)
    private UUID empresaId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = true, length = 100)
    private String apellido;

    @Column(name = "tipo_doc", nullable = false, length = 10)
    private String tipoDoc;

    @Column(name = "num_doc", nullable = false, length = 20)
    private String numDoc;

    @Column(length = 20)
    private String telefono;

    @Column(length = 150)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}