package com.roomiohub.backend.modules.master.dto.promocion.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePromocionStatusRequest {
    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
