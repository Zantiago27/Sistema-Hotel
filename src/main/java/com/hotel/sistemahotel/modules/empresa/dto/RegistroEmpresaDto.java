package com.hotel.sistemahotel.modules.empresa.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEmpresaDto {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
    private String ruc;

    @NotBlank(message = "El nombre de la sede principal es obligatorio")
    private String nombreSucursal;

    private String direccionSucursal;
    private String telefonoSucursal;

    @NotBlank(message = "El nombre del administrador es obligatorio")
    private String adminNombre;

    @NotBlank(message = "El apellido del administrador es obligatorio")
    private String adminApellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String adminEmail;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String adminPassword;
}
