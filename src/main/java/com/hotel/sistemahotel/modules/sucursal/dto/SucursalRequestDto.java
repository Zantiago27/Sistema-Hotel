package com.hotel.sistemahotel.modules.sucursal.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SucursalRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String direccion;
    private String telefono;
}
