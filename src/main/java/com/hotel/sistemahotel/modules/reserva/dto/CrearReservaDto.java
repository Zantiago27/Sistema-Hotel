package com.hotel.sistemahotel.modules.reserva.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaDto {

    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;

    @NotBlank(message = "El tipo de alquiler es obligatorio")
    private String tipoAlquiler; // HORA, DIA, NOCHE

    @NotNull(message = "La fecha de inicio es obligatoria")
    private OffsetDateTime fechaInicio;

    @NotNull(message = "La fecha fin estimada es obligatoria")
    private OffsetDateTime fechaFinEstimada;

    private BigDecimal adelanto = BigDecimal.ZERO;

    private String notas;

    @NotEmpty(message = "Debe incluir al menos una habitación")
    private List<ReservaHabitacionRequestDto> habitaciones;
}
