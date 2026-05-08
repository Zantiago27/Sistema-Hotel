package com.hotel.sistemahotel.modules.producto.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumoResponseDto {

    private UUID id;
    private UUID reservaId;
    private UUID productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private OffsetDateTime createdAt;
}