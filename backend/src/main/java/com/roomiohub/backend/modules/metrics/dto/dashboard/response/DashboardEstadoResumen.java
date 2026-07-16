package com.roomiohub.backend.modules.metrics.dto.dashboard.response;

import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardEstadoResumen {
    private EstadoHabitacion estado;
    private long cantidad;
}
