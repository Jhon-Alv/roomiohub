package com.roomiohub.backend.modules.master.service.tarifa;

import com.roomiohub.backend.modules.master.dto.tarifa.response.FindTarifasResponse;
import com.roomiohub.backend.modules.master.mapper.TarifaMapper;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import com.roomiohub.backend.modules.master.repository.TarifaRepository;
import com.roomiohub.backend.modules.master.repository.TarifaSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindTarifasService {

    private final TarifaRepository tarifaRepository;
    private final TarifaMapper tarifaMapper;

    @Transactional(readOnly = true)
    public List<FindTarifasResponse> execute(UUID categoriaId, UUID planTiempoId, Boolean activo) {
        log.info("Obteniendo lista de tarifas con filtros: categoriaId={}, planTiempoId={}, activo={}", categoriaId, planTiempoId, activo);

        Specification<TarifaEntity> spec = TarifaSpecification.getFiltros(categoriaId, planTiempoId, activo);

        List<TarifaEntity> tarifas = tarifaRepository.findAll(spec);

        return tarifaMapper.toFindListResponse(tarifas);
    }
}
