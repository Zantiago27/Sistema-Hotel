package com.hotel.sistemahotel.modules.sucursal.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucursalResponseDto {

    private UUID id;
    private UUID empresaId;
    private String nombre;
    private String direccion;
    private String telefono;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}
