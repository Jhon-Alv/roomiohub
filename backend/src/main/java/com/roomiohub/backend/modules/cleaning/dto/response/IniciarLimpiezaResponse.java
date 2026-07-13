package com.roomiohub.backend.modules.cleaning.dto.response;

import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IniciarLimpiezaResponse {
    private UUID habitacionId;
    private EstadoHabitacion estadoAnterior;
    private EstadoHabitacion estadoNuevo;
}
