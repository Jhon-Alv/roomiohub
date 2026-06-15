package com.roomiohub.backend.modules.master.dto.tarifa.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateTarifaResponse {
    private UUID id;
    private String categoriaNombre;
    private String planTiempoNombre;
    private BigDecimal precioBase;
}
