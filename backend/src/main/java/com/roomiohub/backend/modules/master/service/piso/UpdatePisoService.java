package com.roomiohub.backend.modules.master.service.piso;

import com.roomiohub.backend.modules.master.dto.piso.request.UpdatePisoRequest;
import com.roomiohub.backend.modules.master.mapper.PisoMapper;
import com.roomiohub.backend.modules.master.model.PisoEntity;
import com.roomiohub.backend.modules.master.repository.PisoRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePisoService {

    private final PisoRepository pisoRepository;
    private final PisoMapper pisoMapper;

    @Transactional
    public void execute(UUID id, UpdatePisoRequest request) {
        log.info("Iniciando proceso de actualización de piso con ID: {}", id);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        PisoEntity piso = pisoRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Piso no existe o no tiene acceso"));

        if (!piso.getNombre().equals(request.getNombre()) && pisoRepository.existsByNombreAndSedeId(request.getNombre(), piso.getSede().getId())) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe un piso con ese nombre en esta sede");
        }

        pisoMapper.toUpdateEntity(piso, request);

        pisoRepository.merge(piso);

        log.info("Piso con ID: {} actualizado exitosamente", id);
    }
}
