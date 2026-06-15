package com.roomiohub.backend.modules.master.service.promociontarifa;

import com.roomiohub.backend.modules.master.dto.promociontarifa.request.BatchCreatePromocionTarifaRequest;
import com.roomiohub.backend.modules.master.dto.promociontarifa.request.PromocionTarifaItemRequest;
import com.roomiohub.backend.modules.master.model.PromocionEntity;
import com.roomiohub.backend.modules.master.model.PromocionTarifaEntity;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import com.roomiohub.backend.modules.master.repository.PromocionRepository;
import com.roomiohub.backend.modules.master.repository.PromocionTarifaRepository;
import com.roomiohub.backend.modules.master.repository.TarifaRepository;
import com.roomiohub.backend.modules.master.validation.PromocionValidationService;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchCreatePromocionTarifaService {

    private final PromocionTarifaRepository promocionTarifaRepository;
    private final PromocionRepository promocionRepository;
    private final TarifaRepository tarifaRepository;
    private final PromocionValidationService validationService;

    @Transactional
    public void execute(UUID promocionId, BatchCreatePromocionTarifaRequest request) {
        log.info("Iniciando batch de creación de tarifas para la promoción: {}", promocionId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        PromocionEntity promocion = promocionRepository.findByIdAndTenantId(promocionId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Promoción no encontrada"));

        List<UUID> tarifaIdsFromRequest = request.getItems().stream()
                .map(PromocionTarifaItemRequest::getTarifaId)
                .toList();

        Set<UUID> existingTarifaIds = promocionTarifaRepository.findByPromocionIdAndTarifaIdIn(promocionId, tarifaIdsFromRequest)
                .stream()
                .map(pt -> pt.getTarifa().getId())
                .collect(Collectors.toSet());

        Map<UUID, TarifaEntity> tarifasMap = tarifaRepository.findAllByIdInAndTenantId(tarifaIdsFromRequest, tenantId)
                .stream()
                .collect(Collectors.toMap(TarifaEntity::getId, t -> t));

        List<PromocionTarifaEntity> newEntities = new ArrayList<>();

        for (PromocionTarifaItemRequest item : request.getItems()) {
            UUID tarifaId = item.getTarifaId();

            if (existingTarifaIds.contains(tarifaId)) {
                log.warn("La tarifa {} ya está asociada a la promoción {}", tarifaId, promocionId);
                continue;
            }

            TarifaEntity tarifa = tarifasMap.get(tarifaId);
            if (tarifa == null) {
                throw new RestException(HttpStatus.NOT_FOUND, "Tarifa no encontrada o no pertenece al tenant: " + tarifaId);
            }

            PromocionTarifaEntity promocionTarifa = PromocionTarifaEntity.builder()
                    .promocion(promocion)
                    .tarifa(tarifa)
                    .precioPromocion(item.getPrecioPromocion())
                    .activo(true)
                    .build();

            newEntities.add(promocionTarifa);
        }

        if (!newEntities.isEmpty()) {
            promocionTarifaRepository.persistAll(newEntities);
            log.info("Se asociaron {} nuevas tarifas a la promoción {}", newEntities.size(), promocionId);
        }
    }
}
