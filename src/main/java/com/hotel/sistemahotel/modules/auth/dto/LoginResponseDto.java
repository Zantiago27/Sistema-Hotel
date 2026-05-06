package com.hotel.sistemahotel.modules.auth.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private String token;
    private String rol;
    private String nombre;
    private String apellido;
    private UUID usuarioId;
    private UUID empresaId;
    private UUID sucursalId;
}