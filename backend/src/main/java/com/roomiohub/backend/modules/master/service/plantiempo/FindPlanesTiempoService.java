package com.roomiohub.backend.modules.master.service.plantiempo;

import com.roomiohub.backend.modules.master.dto.plantiempo.response.FindPlanesTiempoResponse;
import com.roomiohub.backend.modules.master.mapper.PlanTiempoMapper;
import com.roomiohub.backend.modules.master.model.PlanTiempoEntity;
import com.roomiohub.backend.modules.master.repository.PlanTiempoRepository;
import com.roomiohub.backend.modules.master.repository.PlanTiempoSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindPlanesTiempoService {

    private final PlanTiempoRepository planTiempoRepository;
    private final PlanTiempoMapper planTiempoMapper;

    @Transactional(readOnly = true)
    public List<FindPlanesTiempoResponse> execute(String search, Boolean activo) {
        log.info("Obteniendo lista de planes de tiempo con filtros: search={}, activo={}", search, activo);

        Specification<PlanTiempoEntity> spec = PlanTiempoSpecification.getFiltros(search, activo);

        List<PlanTiempoEntity> planesTiempo = planTiempoRepository.findAll(spec);

        return planTiempoMapper.toFindListResponse(planesTiempo);
    }
}
