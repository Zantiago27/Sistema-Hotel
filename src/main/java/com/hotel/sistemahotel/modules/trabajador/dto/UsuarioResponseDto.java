package com.hotel.sistemahotel.modules.trabajador.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDto {

    private UUID id;
    private UUID empresaId;
    private UUID sucursalId;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}
