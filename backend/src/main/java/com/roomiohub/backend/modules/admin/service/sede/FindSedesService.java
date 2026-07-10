package com.roomiohub.backend.modules.admin.service.sede;

import com.roomiohub.backend.modules.admin.dto.sede.response.FindSedeResponse;
import com.roomiohub.backend.modules.admin.mapper.SedeMapper;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindSedesService {

    private final SedeRepository sedeRepository;
    private final SedeMapper sedeMapper;

    @Transactional(readOnly = true)
    public List<FindSedeResponse> execute() {

        UUID sedeDelJwt = TenantContextHolder.getSedeId();

        if (sedeDelJwt != null) {
            log.info("Usuario restringido a una sede. Obteniendo sede {}", sedeDelJwt);
            SedeEntity sede = sedeRepository.findById(sedeDelJwt)
                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Sede no encontrada"));
            return List.of(sedeMapper.toResponse(sede));
        }

        log.info("Super-admin: obteniendo todas las sedes del tenant actual");
        List<SedeEntity> sedes = sedeRepository.findAllSedes();
        return sedeMapper.toResponseList(sedes);
    }
}
