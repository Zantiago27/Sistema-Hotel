package com.hotel.sistemahotel.modules.cliente.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDto {

    private UUID id;
    private String nombre;
    private String apellido;
    private String tipoDoc;
    private String numDoc;
    private String telefono;
    private String email;
    private OffsetDateTime createdAt;
}