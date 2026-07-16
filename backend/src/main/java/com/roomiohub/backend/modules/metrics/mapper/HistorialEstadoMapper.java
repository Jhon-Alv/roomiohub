package com.roomiohub.backend.modules.metrics.mapper;

import com.roomiohub.backend.modules.master.model.LogEstadoHabitacionEntity;
import com.roomiohub.backend.modules.metrics.dto.historial.response.HistorialEstadoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistorialEstadoMapper {

    @Mapping(target = "habitacionNumero", ignore = true)
    @Mapping(target = "habitacionCategoria", ignore = true)
    HistorialEstadoResponse toResponse(LogEstadoHabitacionEntity entity);
}
