package com.hotel.sistemahotel.modules.cliente.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDoc;

    @NotBlank(message = "El número de documento es obligatorio")
    private String numDoc;

    private String telefono;
    private String email;
}