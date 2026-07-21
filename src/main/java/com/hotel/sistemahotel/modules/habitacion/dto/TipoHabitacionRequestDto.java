package com.hotel.sistemahotel.modules.habitacion.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoHabitacionRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidadMax = 2;
}