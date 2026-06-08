package com.roomiohub.backend.modules.admin.mapper;

import com.roomiohub.backend.modules.admin.dto.sede.request.CreateSedeRequest;
import com.roomiohub.backend.modules.admin.dto.sede.request.UpdateSedeRequest;
import com.roomiohub.backend.modules.admin.dto.sede.response.FindSedeResponse;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SedeMapper {

    SedeEntity toEntity(CreateSedeRequest request);

    FindSedeResponse toResponse(SedeEntity entity);

    List<FindSedeResponse> toResponseList(List<SedeEntity> entities);

    void updateEntity(@MappingTarget SedeEntity entity, UpdateSedeRequest request);
}
