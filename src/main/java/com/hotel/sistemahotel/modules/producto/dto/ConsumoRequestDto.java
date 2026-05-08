package com.hotel.sistemahotel.modules.producto.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoRequestDto {

    @NotNull(message = "El producto es obligatorio")
    private UUID productoId;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad = 1;
}