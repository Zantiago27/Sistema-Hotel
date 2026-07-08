package com.hotel.sistemahotel.modules.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO con las credenciales de inicio de sesión.
 *
 * @author victor
 * @version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    /**
     * Correo electrónico del usuario. No puede estar vacío y debe tener formato válido.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    /**
     * Contraseña del usuario. No puede estar vacía.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}