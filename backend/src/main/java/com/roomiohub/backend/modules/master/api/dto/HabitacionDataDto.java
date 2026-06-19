package com.roomiohub.backend.modules.master.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;

@Data
@Builder
public class HabitacionDataDto {
    private UUID id;
    private String numero;
    private String categoriaNombre;
    private String pisoNombre;
    private EstadoHabitacion estadoActual;
}
