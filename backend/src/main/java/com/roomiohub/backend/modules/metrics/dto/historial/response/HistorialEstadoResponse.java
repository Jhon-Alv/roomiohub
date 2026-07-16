package com.roomiohub.backend.modules.metrics.dto.historial.response;

import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorialEstadoResponse {
    private UUID id;
    private UUID habitacionId;
    private String habitacionNumero;
    private String habitacionCategoria;
    private EstadoHabitacion estadoAnterior;
    private EstadoHabitacion estadoNuevo;
    private LocalDateTime fechaCambio;
    private String observacion;
}
