package com.roomiohub.backend.modules.booking.mapper;

import com.roomiohub.backend.modules.booking.dto.response.FindClienteResponse;
import com.roomiohub.backend.modules.booking.model.ClienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {

    @Mapping(target = "encontrado", constant = "true")
    FindClienteResponse toFindResponse(ClienteEntity entity);
}
