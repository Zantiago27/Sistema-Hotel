package com.hotel.sistemahotel.modules.turno.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CerrarTurnoDto {

    @NotNull(message = "El monto de cierre es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal montoCierre;
}