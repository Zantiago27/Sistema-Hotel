package com.hotel.sistemahotel.modules.empresa.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroEmpresaResponseDto {

    private UUID empresaId;
    private UUID sucursalId;
    private UUID usuarioId;
    private String nombreEmpresa;
    private String nombreSucursal;
    private String adminEmail;
    private String mensaje;
}