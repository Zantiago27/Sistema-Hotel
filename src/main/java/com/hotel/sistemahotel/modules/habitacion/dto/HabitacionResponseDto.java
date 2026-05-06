package com.hotel.sistemahotel.modules.habitacion.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionResponseDto {

    private UUID id;
    private UUID sucursalId;
    private UUID tipoId;
    private String tipoNombre;
    private String numero;
    private String piso;
    private String estado;
    private BigDecimal precioHora;
    private BigDecimal precioDia;
    private BigDecimal precioNoche;
    private Boolean isActive;
}