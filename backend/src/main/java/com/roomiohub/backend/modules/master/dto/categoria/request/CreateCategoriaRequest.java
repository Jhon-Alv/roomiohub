package com.roomiohub.backend.modules.master.dto.categoria.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCategoriaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio por hora extra es obligatorio")
    @PositiveOrZero(message = "El precio por hora extra no puede ser negativo")
    private BigDecimal precioHoraExtra;
}
