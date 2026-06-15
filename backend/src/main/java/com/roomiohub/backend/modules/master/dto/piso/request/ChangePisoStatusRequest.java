package com.roomiohub.backend.modules.master.dto.piso.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePisoStatusRequest {
    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
