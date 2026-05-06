package com.hotel.sistemahotel.modules.reserva.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaHabitacionRequestDto {

    @NotNull(message = "La habitación es obligatoria")
    private UUID habitacionId;

    // Huéspedes opcionales
    private List<HuespedDto> huespedes;
}