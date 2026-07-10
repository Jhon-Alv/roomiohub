package com.roomiohub.backend.modules.booking.mapper;

import com.roomiohub.backend.modules.booking.dto.response.RoomMapDto;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "numero", target = "numero")
    @Mapping(source = "categoriaId", target = "categoriaId")
    @Mapping(source = "categoriaNombre", target = "categoria")
    @Mapping(source = "pisoId", target = "pisoId")
    @Mapping(source = "pisoNombre", target = "piso")
    @Mapping(source = "estadoActual", target = "estadoActual")
    RoomMapDto toDto(HabitacionDataDto source);

    List<RoomMapDto> toDtoList(List<HabitacionDataDto> source);
}
