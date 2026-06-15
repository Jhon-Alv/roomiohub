package com.roomiohub.backend.modules.master.dto.habitacion.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FindHabitacionResponse {
    private UUID id;
    private String numero;
    private UUID sedeId;
    private String sedeNombre;
    private UUID categoriaId;
    private String categoriaNombre;
    private UUID pisoId;
    private String pisoNombre;
    private String estadoActual;
    private Boolean activo;
}
