package com.hotel.sistemahotel.modules.habitacion.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionRequestDto {

    @NotBlank(message = "El número de habitación es obligatorio")
    private String numero;

    private String descripcion;

    private String piso;

    @NotNull(message = "El tipo de habitación es obligatorio")
    private UUID tipoId;

    private BigDecimal precioHora;
    private BigDecimal precioDia;
    private BigDecimal precioNoche;
}