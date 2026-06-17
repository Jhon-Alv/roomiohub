package com.roomiohub.backend.modules.master.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class HabitacionDataDto {
    private UUID id;
    private String numero;
    private String categoriaNombre;
    private String pisoNombre;
    private String estadoActual;
}
