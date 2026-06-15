package com.roomiohub.backend.modules.master.mapper;

import com.roomiohub.backend.modules.master.dto.categoria.request.CreateCategoriaRequest;
import com.roomiohub.backend.modules.master.dto.categoria.request.UpdateCategoriaRequest;
import com.roomiohub.backend.modules.master.dto.categoria.response.CreateCategoriaResponse;
import com.roomiohub.backend.modules.master.dto.categoria.response.FindCategoriaResponse;
import com.roomiohub.backend.modules.master.dto.categoria.response.FindCategoriasResponse;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoriaMapper {

    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "precioHoraExtra", source = "request.precioHoraExtra")
    CategoriaEntity toCreateCategoriaEntity(CreateCategoriaRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "precioHoraExtra", source = "entity.precioHoraExtra")
    CreateCategoriaResponse toCreateCategoriaResponse(CategoriaEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "precioHoraExtra", source = "entity.precioHoraExtra")
    @Mapping(target = "activo", source = "entity.activo")
    FindCategoriaResponse toFindCategoriaResponse(CategoriaEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "precioHoraExtra", source = "entity.precioHoraExtra")
    @Mapping(target = "activo", source = "entity.activo")
    List<FindCategoriasResponse> toFindCategoriasResponseList(List<CategoriaEntity> entities);

    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "precioHoraExtra", source = "request.precioHoraExtra")
    void toUpdateCategoriaEntity(@MappingTarget CategoriaEntity entity, UpdateCategoriaRequest request);
}
