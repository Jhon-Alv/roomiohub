package com.roomiohub.backend.modules.master.mapper;

import com.roomiohub.backend.modules.master.dto.piso.request.CreatePisoRequest;
import com.roomiohub.backend.modules.master.dto.piso.request.UpdatePisoRequest;
import com.roomiohub.backend.modules.master.dto.piso.response.CreatePisoResponse;
import com.roomiohub.backend.modules.master.dto.piso.response.FindPisoResponse;
import com.roomiohub.backend.modules.master.dto.piso.response.FindPisosResponse;
import com.roomiohub.backend.modules.master.model.PisoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PisoMapper {

    @Mapping(target = "nombre", source = "request.nombre")
    PisoEntity toCreateEntity(CreatePisoRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "sedeId", source = "entity.sede.id")
    @Mapping(target = "sedeNombre", source = "entity.sede.nombre")
    CreatePisoResponse toCreateResponse(PisoEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "sedeId", source = "entity.sede.id")
    @Mapping(target = "sedeNombre", source = "entity.sede.nombre")
    @Mapping(target = "activo", source = "entity.activo")
    FindPisoResponse toFindResponse(PisoEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "activo", source = "entity.activo")
    List<FindPisosResponse> toFindListResponse(List<PisoEntity> entities);

    @Mapping(target = "nombre", source = "request.nombre")
    void toUpdateEntity(@MappingTarget PisoEntity entity, UpdatePisoRequest request);
}
