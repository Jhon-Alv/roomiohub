package com.roomiohub.backend.modules.master.service.piso;

import com.roomiohub.backend.modules.master.dto.piso.response.FindPisoResponse;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class FindPisoService {

    private final PisoRepository pisoRepository;
    private final PisoMapper pisoMapper;

    @Transactional(readOnly = true)
    public FindPisoResponse execute(UUID pisoId){
        log.info("Obteniendo piso con ID: {}", pisoId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        PisoEntity piso = pisoRepository.findByIdAndTenantId(pisoId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Piso no encontrado con ID: " + pisoId));

        log.info("Piso encontrado: {} - {}", piso.getId(), piso.getNombre());

        return pisoMapper.toFindResponse(piso);
    }
}
