package com.hotel.sistemahotel.modules.producto.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDto {

    private UUID id;
    private UUID sucursalId;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private Integer stock;
    private Boolean isActive;
}