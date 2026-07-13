package com.roomiohub.backend.modules.master.api.habitacion;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;

import java.util.List;
import java.util.UUID;

public interface IMasterHabitacionPort {

    /**
     * Devuelve todas las habitaciones activas de una sede específica, 
     * con sus categorías y pisos para uso en otros módulos.
     */
    List<HabitacionDataDto> getHabitacionesActivasBySede(UUID tenantId, UUID sedeId);

    HabitacionDataDto getHabitacionById(UUID habitacionId);

    /**
     * Actualiza el estado de la habitación.
     */
    void updateEstadoHabitacion(UUID habitacionId, EstadoHabitacion nuevoEstado, UUID usuarioId, String observacion);
}
