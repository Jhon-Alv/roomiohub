package com.roomiohub.backend.modules.admin.service.sede;

import com.roomiohub.backend.modules.admin.dto.sede.response.FindSedeResponse;
import com.roomiohub.backend.modules.admin.mapper.SedeMapper;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindSedesService {

    private final SedeRepository sedeRepository;
    private final SedeMapper sedeMapper;

    @Transactional(readOnly = true)
    public List<FindSedeResponse> execute() {

        log.info("Obteniendo todas las sedes del tenant actual");

        List<SedeEntity> sedes = sedeRepository.findAllSedes();

        return sedeMapper.toResponseList(sedes);
    }
}
