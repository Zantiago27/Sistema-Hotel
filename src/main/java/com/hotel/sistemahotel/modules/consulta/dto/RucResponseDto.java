package com.hotel.sistemahotel.modules.consulta.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RucResponseDto {

    private String ruc;
    private String razonSocial;
    private String estado;
    private String condicion;
    private String direccion;
    private String ubigeo;
    private String distrito;
    private String provincia;
    private String departamento;
}