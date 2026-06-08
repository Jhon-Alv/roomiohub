package com.roomiohub.backend.modules.admin.service.sede;

import com.roomiohub.backend.modules.admin.dto.sede.request.UpdateSedeRequest;
import com.roomiohub.backend.modules.admin.mapper.SedeMapper;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateSedeService {

    private final SedeRepository sedeRepository;
    private final SedeMapper sedeMapper;

    @Transactional
    public void execute(UUID id, UpdateSedeRequest request) {

        log.info("Actualizando la sede con id: {}", id);

        SedeEntity sede = sedeRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "La sede no existe o no tiene acceso."));
        
        sedeMapper.updateEntity(sede, request);
        sedeRepository.merge(sede);

        log.info("Sede actualizada exitosamente");
    }
}
