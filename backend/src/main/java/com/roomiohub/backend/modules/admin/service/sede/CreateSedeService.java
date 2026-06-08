package com.roomiohub.backend.modules.admin.service.sede;

import com.roomiohub.backend.modules.admin.dto.sede.request.CreateSedeRequest;
import com.roomiohub.backend.modules.admin.dto.sede.response.CreateSedeResponse;
import com.roomiohub.backend.modules.admin.mapper.SedeMapper;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
import com.roomiohub.backend.modules.admin.validation.CreateSedeValidationService;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateSedeService {

    private final SedeRepository sedeRepository;
    private final SedeMapper sedeMapper;
    private final CreateSedeValidationService createSedeValidationService;

    @Transactional
    public CreateSedeResponse execute(CreateSedeRequest request) {

        log.info("Creando nueva sede: {}", request.getNombre());

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        createSedeValidationService.validate(tenantId);

        SedeEntity sede = sedeMapper.toEntity(request);
        sede.setTenantId(tenantId);
        SedeEntity sedeGuardado = sedeRepository.persist(sede);

        log.info("Sede creada exitosamente con id: {}", sede.getId());

        return CreateSedeResponse.builder().id(sedeGuardado.getId()).build();
    }
}
