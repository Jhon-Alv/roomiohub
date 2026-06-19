package com.roomiohub.backend.modules.master.mapper;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.dto.habitacion.request.CreateHabitacionRequest;
import com.roomiohub.backend.modules.master.dto.habitacion.request.UpdateHabitacionRequest;
import com.roomiohub.backend.modules.master.dto.habitacion.response.CreateHabitacionResponse;
import com.roomiohub.backend.modules.master.dto.habitacion.response.FindHabitacionResponse;
import com.roomiohub.backend.modules.master.dto.habitacion.response.FindHabitacionesResponse;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HabitacionMapper {

    @Mapping(target = "numero", source = "request.numero")
    HabitacionEntity toCreateEntity(CreateHabitacionRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "numero", source = "entity.numero")
    @Mapping(target = "sedeNombre", source = "entity.sede.nombre")
    @Mapping(target = "categoriaNombre", source = "entity.categoria.nombre")
    @Mapping(target = "pisoNombre", source = "entity.piso.nombre")
    CreateHabitacionResponse toCreateResponse(HabitacionEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "numero", source = "entity.numero")
    @Mapping(target = "sedeId", source = "entity.sede.id")
    @Mapping(target = "sedeNombre", source = "entity.sede.nombre")
    @Mapping(target = "categoriaId", source = "entity.categoria.id")
    @Mapping(target = "categoriaNombre", source = "entity.categoria.nombre")
    @Mapping(target = "pisoId", source = "entity.piso.id")
    @Mapping(target = "pisoNombre", source = "entity.piso.nombre")
    @Mapping(target = "estadoActual", source = "entity.estadoActual")
    @Mapping(target = "activo", source = "entity.activo")
    FindHabitacionResponse toFindResponse(HabitacionEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "numero", source = "entity.numero")
    @Mapping(target = "categoriaNombre", source = "entity.categoria.nombre")
    @Mapping(target = "pisoNombre", source = "entity.piso.nombre")
    @Mapping(target = "estadoActual", source = "entity.estadoActual")
    @Mapping(target = "activo", source = "entity.activo")
    FindHabitacionesResponse toFindListResponseElement(HabitacionEntity entity);

    List<FindHabitacionesResponse> toFindListResponse(List<HabitacionEntity> entities);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "numero", source = "entity.numero")
    @Mapping(target = "categoriaNombre", source = "entity.categoria.nombre")
    @Mapping(target = "pisoNombre", source = "entity.piso.nombre")
    @Mapping(target = "estadoActual", source = "entity.estadoActual")
    HabitacionDataDto toDataDto(HabitacionEntity entity);

    List<HabitacionDataDto> toDataDtoList(List<HabitacionEntity> entities);

    @Mapping(target = "numero", source = "request.numero")
    void toUpdateEntity(@MappingTarget HabitacionEntity entity, UpdateHabitacionRequest request);
}
