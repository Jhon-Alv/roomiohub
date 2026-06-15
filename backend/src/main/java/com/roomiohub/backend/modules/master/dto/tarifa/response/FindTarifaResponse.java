package com.roomiohub.backend.modules.master.dto.tarifa.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FindTarifaResponse {
    private UUID id;
    private UUID categoriaId;
    private String categoriaNombre;
    private UUID planTiempoId;
    private String planTiempoNombre;
    private Integer planTiempoHoras;
    private BigDecimal precioBase;
    private Boolean activo;
}
