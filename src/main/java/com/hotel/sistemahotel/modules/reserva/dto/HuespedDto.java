package com.hotel.sistemahotel.modules.reserva.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HuespedDto {

    private UUID id;
    private String nombre;
    private String apellido;
    private String tipoDoc;
    private String numDoc;
    private Boolean esTitular;
}