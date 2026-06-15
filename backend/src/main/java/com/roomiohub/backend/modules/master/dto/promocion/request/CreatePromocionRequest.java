package com.roomiohub.backend.modules.master.dto.promocion.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePromocionRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
}
