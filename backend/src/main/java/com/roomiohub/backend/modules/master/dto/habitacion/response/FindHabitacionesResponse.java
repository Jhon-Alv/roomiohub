package com.roomiohub.backend.modules.master.dto.habitacion.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FindHabitacionesResponse {
    private UUID id;
    private String numero;
    private String categoriaNombre;
    private String pisoNombre;
    private String estadoActual;
    private Boolean activo;
}
