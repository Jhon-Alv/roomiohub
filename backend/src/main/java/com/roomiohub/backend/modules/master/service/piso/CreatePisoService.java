package com.roomiohub.backend.modules.master.service.piso;

import com.roomiohub.backend.modules.master.dto.piso.request.CreatePisoRequest;
import com.roomiohub.backend.modules.master.dto.piso.response.CreatePisoResponse;
import com.roomiohub.backend.modules.master.mapper.PisoMapper;
import com.roomiohub.backend.modules.master.model.PisoEntity;
import com.roomiohub.backend.modules.master.repository.PisoRepository;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePisoService {

    private final PisoRepository pisoRepository;
    private final SedeRepository sedeRepository;
    private final PisoMapper pisoMapper;

    @Transactional
    public CreatePisoResponse execute(CreatePisoRequest request) {
        log.info("Iniciando proceso de creación de piso con nombre: {}", request.getNombre());

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        SedeEntity sede = sedeRepository.findByIdAndTenantId(request.getSedeId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Sede no encontrada o no pertenece al tenant actual"));

        if (pisoRepository.existsByNombreAndSedeId(request.getNombre(), request.getSedeId())) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe un piso con el nombre especificado en esta sede");
        }

        PisoEntity piso = pisoMapper.toCreateEntity(request);
        piso.setSede(sede);
        piso.setTenantId(tenantId);

        PisoEntity savedPiso = pisoRepository.persist(piso);
        log.info("Creación de piso con ID: {}", savedPiso.getId());

        return pisoMapper.toCreateResponse(savedPiso);
    }
}
