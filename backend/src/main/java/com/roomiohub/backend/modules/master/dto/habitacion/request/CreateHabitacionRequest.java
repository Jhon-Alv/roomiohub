package com.roomiohub.backend.modules.master.dto.habitacion.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateHabitacionRequest {
    @NotBlank(message = "El numero es obligatorio")
    private String numero;

    @NotNull(message = "La sede es obligatoria")
    private UUID sedeId;

    @NotNull(message = "La categoria es obligatoria")
    private UUID categoriaId;

    @NotNull(message = "El piso es obligatorio")
    private UUID pisoId;
}
