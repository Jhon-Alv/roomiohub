package com.roomiohub.backend.modules.master.mapper;

import com.roomiohub.backend.modules.master.dto.promociontarifa.response.PromocionTarifaResponse;
import com.roomiohub.backend.modules.master.model.PromocionTarifaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromocionTarifaMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "promocionId", source = "entity.promocion.id")
    @Mapping(target = "tarifaId", source = "entity.tarifa.id")
    @Mapping(target = "categoriaNombre", source = "entity.tarifa.categoria.nombre")
    @Mapping(target = "planTiempoNombre", source = "entity.tarifa.planTiempo.nombre")
    @Mapping(target = "precioBase", source = "entity.tarifa.precioBase")
    @Mapping(target = "precioPromocion", source = "entity.precioPromocional")
    @Mapping(target = "activo", source = "entity.activo")
    PromocionTarifaResponse toResponse(PromocionTarifaEntity entity);

    List<PromocionTarifaResponse> toListResponse(List<PromocionTarifaEntity> entities);
}
