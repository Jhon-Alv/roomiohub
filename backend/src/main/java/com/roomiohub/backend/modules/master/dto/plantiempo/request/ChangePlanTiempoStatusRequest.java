package com.roomiohub.backend.modules.master.dto.plantiempo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePlanTiempoStatusRequest {
    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
