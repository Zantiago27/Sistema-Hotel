package com.hotel.sistemahotel.modules.habitacion.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoHabitacionDto {

    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidadMax;
}