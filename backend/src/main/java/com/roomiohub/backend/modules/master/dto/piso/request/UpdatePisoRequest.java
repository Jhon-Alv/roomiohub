package com.roomiohub.backend.modules.master.dto.piso.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePisoRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
}
