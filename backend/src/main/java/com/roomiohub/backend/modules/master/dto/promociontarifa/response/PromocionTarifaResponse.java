package com.roomiohub.backend.modules.master.dto.promociontarifa.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PromocionTarifaResponse {
    private UUID id;
    private UUID promocionId;
    private UUID tarifaId;
    private String categoriaNombre;
    private String planTiempoNombre;
    private BigDecimal precioBase;
    private BigDecimal precioPromocion;
    private Boolean activo;
}
