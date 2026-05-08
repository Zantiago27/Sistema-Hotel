package com.hotel.sistemahotel.modules.comprobante.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteDetalleDto {

    private UUID id;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnit;
    private BigDecimal subtotal;
}