package com.hotel.sistemahotel.modules.comprobante.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponseDto {

    private UUID id;
    private String metodo;
    private BigDecimal monto;
    private String tipo;
    private OffsetDateTime createdAt;
}