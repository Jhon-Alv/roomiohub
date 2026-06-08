package com.roomiohub.backend.modules.admin.service.sede;

import com.roomiohub.backend.modules.admin.dto.sede.request.ChangeSedeStatusRequest;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChangeSedeStatusService {

    private final SedeRepository sedeRepository;

    @Transactional
    public void execute(UUID id, ChangeSedeStatusRequest request) {

        log.info("Cambiando el estado de la sede con id: {}", id);

        SedeEntity sede = sedeRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "La sede no existe o no tiene acceso."));

        if (Objects.equals(sede.getActivo(), request.getActivo())) {
            log.info("La sede {} ya se encuentra en estado activo: {}", id, request.getActivo());
            return;
        }

        sede.setActivo(request.getActivo());
        sedeRepository.merge(sede);

        //TODO: Aquí en el futuro se lanzará el evento de cambio de estado

        log.info("El estado de la sede cambió a activo: {}", request.getActivo());
    }
}
