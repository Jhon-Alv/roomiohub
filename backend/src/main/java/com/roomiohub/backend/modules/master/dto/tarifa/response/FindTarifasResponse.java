package com.roomiohub.backend.modules.master.dto.tarifa.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FindTarifasResponse {
    private UUID id;
    private String categoriaNombre;
    private String planTiempoNombre;
    private Integer planTiempoHoras;
    private BigDecimal precioBase;
    private Boolean activo;
}
