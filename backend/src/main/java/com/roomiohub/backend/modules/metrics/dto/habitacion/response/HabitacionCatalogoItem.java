package com.roomiohub.backend.modules.metrics.dto.habitacion.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionCatalogoItem {
    private UUID id;
    private String numero;
    private String categoria;
}
