package com.roomiohub.backend.modules.master.dto.tarifa.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTarifaStatusRequest {
    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
