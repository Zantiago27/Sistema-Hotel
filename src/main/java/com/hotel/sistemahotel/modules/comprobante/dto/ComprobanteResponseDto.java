package com.hotel.sistemahotel.modules.comprobante.dto;

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
public class ComprobanteResponseDto {

    private UUID id;
    private UUID reservaId;
    private String tipo;
    private String serie;
    private Integer numero;
    private String estado;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private BigDecimal totalPagado;
    private BigDecimal saldo;
    private OffsetDateTime emitidoAt;
    private List<ComprobanteDetalleDto> detalles;
    private List<PagoResponseDto> pagos;
}