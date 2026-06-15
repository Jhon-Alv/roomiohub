package com.roomiohub.backend.modules.master.dto.habitacion.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateHabitacionResponse {
    private UUID id;
    private String numero;
    private String sedeNombre;
    private String categoriaNombre;
    private String pisoNombre;
}
