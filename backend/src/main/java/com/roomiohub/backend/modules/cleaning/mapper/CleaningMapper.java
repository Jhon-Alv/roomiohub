package com.roomiohub.backend.modules.cleaning.mapper;

import com.roomiohub.backend.modules.cleaning.dto.CleaningRoomDto;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CleaningMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "numero", target = "numero")
    @Mapping(source = "categoriaId", target = "categoriaId")
    @Mapping(source = "categoriaNombre", target = "categoria")
    @Mapping(source = "pisoId", target = "pisoId")
    @Mapping(source = "pisoNombre", target = "piso")
    @Mapping(source = "estadoActual", target = "estadoActual")
    CleaningRoomDto toDto(HabitacionDataDto source);

    List<CleaningRoomDto> toDtoList(List<HabitacionDataDto> source);
}
