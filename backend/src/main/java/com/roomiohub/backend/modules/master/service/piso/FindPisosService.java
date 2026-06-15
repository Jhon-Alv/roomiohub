package com.roomiohub.backend.modules.master.service.piso;

import com.roomiohub.backend.modules.master.dto.piso.response.FindPisosResponse;
import com.roomiohub.backend.modules.master.mapper.PisoMapper;
import com.roomiohub.backend.modules.master.model.PisoEntity;
import com.roomiohub.backend.modules.master.repository.PisoRepository;
import com.roomiohub.backend.modules.master.repository.PisoSpecification;
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
public class FindPisosService {

    private final PisoRepository pisoRepository;
    private final PisoMapper pisoMapper;

    @Transactional(readOnly = true)
    public List<FindPisosResponse> execute(String search, Boolean activo, UUID sedeId) {
        log.info("Obteniendo lista de pisos con filtros: search={}, activo={}, sedeId={}", search, activo, sedeId);

        Specification<PisoEntity> spec = PisoSpecification.getFiltros(search, activo, sedeId);

        List<PisoEntity> pisos = pisoRepository.findAll(spec);

        return pisoMapper.toFindListResponse(pisos);
    }
}
