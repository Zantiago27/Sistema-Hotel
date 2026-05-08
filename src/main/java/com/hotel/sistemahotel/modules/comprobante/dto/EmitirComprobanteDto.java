package com.hotel.sistemahotel.modules.comprobante.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmitirComprobanteDto {

    @NotNull(message = "El ID de reserva es obligatorio")
    private java.util.UUID reservaId;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo; // NOTA_VENTA, BOLETA, FACTURA

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago; // EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA

    private java.math.BigDecimal montoPagado;
}