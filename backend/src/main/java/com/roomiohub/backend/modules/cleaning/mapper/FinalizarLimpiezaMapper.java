package com.roomiohub.backend.modules.cleaning.mapper;

import com.roomiohub.backend.modules.cleaning.dto.response.FinalizarLimpiezaResponse;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = EstadoHabitacion.class)
public interface FinalizarLimpiezaMapper {

    @Mapping(target = "habitacionId", source = "habitacionId")
    @Mapping(target = "estadoAnterior", source = "estadoAnterior")
    @Mapping(target = "estadoNuevo", expression = "java(EstadoHabitacion.DISPONIBLE)")
    FinalizarLimpiezaResponse toResponse(UUID habitacionId, EstadoHabitacion estadoAnterior);
}
