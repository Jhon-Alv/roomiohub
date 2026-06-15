package com.roomiohub.backend.modules.master.dto.categoria.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FindCategoriaResponse {
    private UUID id;
    private String nombre;
    private BigDecimal precioHoraExtra;
    private Boolean activo;
}
