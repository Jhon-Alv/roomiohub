package com.roomiohub.backend.modules.admin.service.usuario;

import com.roomiohub.backend.modules.admin.dto.usuario.response.FindUsuariosResponse;
import com.roomiohub.backend.modules.admin.mapper.UsuarioMapper;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.roomiohub.backend.security.identity.model.RolUsuario;
import com.roomiohub.backend.security.identity.repository.UsuarioSpecification;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindUsuariosService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public List<FindUsuariosResponse> execute(UUID sedeId, String search, RolUsuario rol, Boolean activo) {

        log.info("Obteniendo lista de usuarios con filtros: sedeId={}, search={}, rol={}, activo={}", sedeId, search, rol, activo);

        Specification<UsuarioEntity> spec = UsuarioSpecification.getFiltros(sedeId, search, rol, activo);

        List<UsuarioEntity> usuarios = usuarioRepository.findAll(spec);

        return usuarioMapper.toFindUsuariosResponseList(usuarios);

    }

}
