package com.roomiohub.backend.modules.master.dto.habitacion.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeHabitacionStatusRequest {
    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
