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
public class ReservaResponseDto {

    private UUID id;
    private UUID sucursalId;
    private UUID clienteId;
    private String clienteNombre;
    private String clienteApellido;
    private String clienteNumDoc;
    private String tipoAlquiler;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinEstimada;
    private String estado;
    private BigDecimal adelanto;
    private BigDecimal totalHabitaciones;
    private String notas;
    private OffsetDateTime createdAt;
    private List<ReservaHabitacionResponseDto> habitaciones;
}
