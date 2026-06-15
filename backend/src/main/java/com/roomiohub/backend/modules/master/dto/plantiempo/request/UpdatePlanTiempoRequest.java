package com.roomiohub.backend.modules.master.dto.plantiempo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdatePlanTiempoRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "Las horas son obligatorias")
    @Positive(message = "Las horas deben ser mayor a 0")
    private Integer horas;
}
