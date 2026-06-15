package com.roomiohub.backend.modules.master.service.tarifa;

import com.roomiohub.backend.modules.master.dto.tarifa.response.FindTarifaResponse;
import com.roomiohub.backend.modules.master.mapper.TarifaMapper;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import com.roomiohub.backend.modules.master.repository.TarifaRepository;
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
public class FindTarifaService {

    private final TarifaRepository tarifaRepository;
    private final TarifaMapper tarifaMapper;

    @Transactional(readOnly = true)
    public FindTarifaResponse execute(UUID tarifaId){
        log.info("Obteniendo tarifa con ID: {}", tarifaId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        TarifaEntity tarifa = tarifaRepository.findByIdAndTenantId(tarifaId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Tarifa no encontrada con ID: " + tarifaId));

        return tarifaMapper.toFindResponse(tarifa);
    }
}
