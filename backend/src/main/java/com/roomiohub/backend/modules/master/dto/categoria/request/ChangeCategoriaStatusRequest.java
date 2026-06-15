package com.roomiohub.backend.modules.master.dto.categoria.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeCategoriaStatusRequest {
    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
