package com.roomiohub.backend.modules.admin.mapper;

import com.roomiohub.backend.modules.admin.dto.usuario.request.CreateUsuarioRequest;
import com.roomiohub.backend.modules.admin.dto.usuario.request.UpdateUsuarioRequest;
import com.roomiohub.backend.modules.admin.dto.usuario.response.CreateUsuarioResponse;
import com.roomiohub.backend.modules.admin.dto.usuario.response.FindUsuarioResponse;
import com.roomiohub.backend.modules.admin.dto.usuario.response.FindUsuariosResponse;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "rol", source = "request.rol")
    @Mapping(target = "sedeId", source = "request.sedeId")
    @Mapping(target = "flagCambiarPassword", constant = "true")
    @Mapping(target = "password", source = "encodedPassword")
    UsuarioEntity toCreateUsuarioEntity(CreateUsuarioRequest request, String encodedPassword);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "username", source = "entity.username")
    @Mapping(target = "temporalPassword", source = "temporalPassword")
    @Mapping(target = "rol", source = "entity.rol")
    @Mapping(target = "sedeId", source = "entity.sedeId")
    CreateUsuarioResponse toCreateUsuarioResponse(UsuarioEntity entity, String temporalPassword);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "username", source = "entity.username")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "rol", source = "entity.rol")
    @Mapping(target = "activo", source = "entity.activo")
    List<FindUsuariosResponse> toFindUsuariosResponseList(List<UsuarioEntity> entities);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "username", source = "entity.username")
    @Mapping(target = "nombre", source = "entity.nombre")
    @Mapping(target = "rol", source = "entity.rol")
    FindUsuarioResponse toFindUsuarioResponse(UsuarioEntity entity);

    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "rol", source = "request.rol")
    @Mapping(target = "password", source = "request.nuevaPassword", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateUsuarioEntity(@MappingTarget UsuarioEntity entity, UpdateUsuarioRequest request);

}
