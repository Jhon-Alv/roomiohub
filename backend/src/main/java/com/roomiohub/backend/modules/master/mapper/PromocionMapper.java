package com.roomiohub.backend.modules.master.mapper;

import com.roomiohub.backend.modules.master.dto.promocion.request.CreatePromocionRequest;
import com.roomiohub.backend.modules.master.dto.promocion.request.UpdatePromocionRequest;
import com.roomiohub.backend.modules.master.dto.promocion.response.CreatePromocionResponse;
import com.roomiohub.backend.modules.master.dto.promocion.response.FindPromocionResponse;
import com.roomiohub.backend.modules.master.dto.promocion.response.FindPromocionesResponse;
import com.roomiohub.backend.modules.master.model.PromocionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromocionMapper {

    @Mapping(target = "nombre", source = "request.nombre")
    PromocionEntity toCreateEntity(CreatePromocionRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    CreatePromocionResponse toCreateResponse(PromocionEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "activo", source = "entity.activo")
    FindPromocionResponse toFindResponse(PromocionEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "activo", source = "entity.activo")
    List<FindPromocionesResponse> toFindListResponse(List<PromocionEntity> entities);

    @Mapping(target = "nombre", source = "request.nombre")
    void toUpdateEntity(@MappingTarget PromocionEntity entity, UpdatePromocionRequest request);
}
