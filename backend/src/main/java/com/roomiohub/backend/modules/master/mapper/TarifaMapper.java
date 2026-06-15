package com.roomiohub.backend.modules.master.mapper;

import com.roomiohub.backend.modules.master.dto.tarifa.request.CreateTarifaRequest;
import com.roomiohub.backend.modules.master.dto.tarifa.request.UpdateTarifaRequest;
import com.roomiohub.backend.modules.master.dto.tarifa.response.CreateTarifaResponse;
import com.roomiohub.backend.modules.master.dto.tarifa.response.FindTarifaResponse;
import com.roomiohub.backend.modules.master.dto.tarifa.response.FindTarifasResponse;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TarifaMapper {

    @Mapping(target = "precioBase", source = "request.precioBase")
    TarifaEntity toCreateEntity(CreateTarifaRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "categoriaNombre", source = "entity.categoria.nombre")
    @Mapping(target = "planTiempoNombre", source = "entity.planTiempo.nombre")
    @Mapping(target = "precioBase", source = "entity.precioBase")
    CreateTarifaResponse toCreateResponse(TarifaEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "categoriaId", source = "entity.categoria.id")
    @Mapping(target = "categoriaNombre", source = "entity.categoria.nombre")
    @Mapping(target = "planTiempoId", source = "entity.planTiempo.id")
    @Mapping(target = "planTiempoNombre", source = "entity.planTiempo.nombre")
    @Mapping(target = "planTiempoHoras", source = "entity.planTiempo.horas")
    @Mapping(target = "precioBase", source = "entity.precioBase")
    @Mapping(target = "activo", source = "entity.activo")
    FindTarifaResponse toFindResponse(TarifaEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "categoriaNombre", source = "entity.categoria.nombre")
    @Mapping(target = "planTiempoNombre", source = "entity.planTiempo.nombre")
    @Mapping(target = "planTiempoHoras", source = "entity.planTiempo.horas")
    @Mapping(target = "precioBase", source = "entity.precioBase")
    @Mapping(target = "activo", source = "entity.activo")
    List<FindTarifasResponse> toFindListResponse(List<TarifaEntity> entities);

    @Mapping(target = "precioBase", source = "request.precioBase")
    void toUpdateEntity(@MappingTarget TarifaEntity entity, UpdateTarifaRequest request);
}
