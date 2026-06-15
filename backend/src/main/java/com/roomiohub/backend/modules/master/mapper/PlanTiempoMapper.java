package com.roomiohub.backend.modules.master.mapper;

import com.roomiohub.backend.modules.master.dto.plantiempo.request.CreatePlanTiempoRequest;
import com.roomiohub.backend.modules.master.dto.plantiempo.request.UpdatePlanTiempoRequest;
import com.roomiohub.backend.modules.master.dto.plantiempo.response.CreatePlanTiempoResponse;
import com.roomiohub.backend.modules.master.dto.plantiempo.response.FindPlanTiempoResponse;
import com.roomiohub.backend.modules.master.dto.plantiempo.response.FindPlanesTiempoResponse;
import com.roomiohub.backend.modules.master.model.PlanTiempoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanTiempoMapper {

    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "horas", source = "request.horas")
    PlanTiempoEntity toCreateEntity(CreatePlanTiempoRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "horas", source = "entity.horas")
    CreatePlanTiempoResponse toCreateResponse(PlanTiempoEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "horas", source = "entity.horas")
    @Mapping(target = "activo", source = "entity.activo")
    FindPlanTiempoResponse toFindResponse(PlanTiempoEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "horas", source = "entity.horas")
    @Mapping(target = "activo", source = "entity.activo")
    List<FindPlanesTiempoResponse> toFindListResponse(List<PlanTiempoEntity> entities);

    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "horas", source = "request.horas")
    void toUpdateEntity(@MappingTarget PlanTiempoEntity entity, UpdatePlanTiempoRequest request);
}
