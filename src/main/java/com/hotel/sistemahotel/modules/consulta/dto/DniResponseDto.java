package com.hotel.sistemahotel.modules.consulta.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DniResponseDto {

    private String dni;
    private String nombres;           // first_name
    private String apellidoPaterno;   // first_last_name
    private String apellidoMaterno;   // second_last_name
    private String nombreCompleto;    // full_name
}