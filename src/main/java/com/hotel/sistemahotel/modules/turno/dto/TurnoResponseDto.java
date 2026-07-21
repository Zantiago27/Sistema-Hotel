package com.hotel.sistemahotel.modules.turno.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoResponseDto {

    private UUID id;
    private UUID sucursalId;
    private UUID usuarioId;
    private String usuarioNombre;
    private OffsetDateTime inicio;
    private OffsetDateTime fin;
    private BigDecimal montoApertura;
    private BigDecimal montoCierre;
    private String estado;

    //para cerrar turno
    private BigDecimal montoEsperado;   // lo que debería haber en caja
    private BigDecimal diferencia;      // montoCierre - montoEsperado
}