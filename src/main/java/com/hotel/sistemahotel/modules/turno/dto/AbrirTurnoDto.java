package com.hotel.sistemahotel.modules.turno.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbrirTurnoDto {

    @NotNull(message = "El monto de apertura es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal montoApertura;
}