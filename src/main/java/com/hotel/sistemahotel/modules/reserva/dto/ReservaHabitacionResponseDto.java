package com.hotel.sistemahotel.modules.reserva.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaHabitacionResponseDto {

    private UUID id;
    private UUID habitacionId;
    private String habitacionNumero;
    private String tipoAlquiler;
    private BigDecimal precioAplicado;
    private OffsetDateTime checkinReal;
    private OffsetDateTime checkoutReal;
    private String estado;
    private List<HuespedDto> huespedes;
}