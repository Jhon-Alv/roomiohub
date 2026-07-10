package com.roomiohub.backend.modules.master.service.habitacion;

import com.roomiohub.backend.modules.master.dto.habitacion.response.FindHabitacionesResponse;
import com.roomiohub.backend.modules.master.mapper.HabitacionMapper;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.modules.master.repository.HabitacionSpecification;
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
public class FindHabitacionesService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Transactional(readOnly = true)
    public List<FindHabitacionesResponse> execute(String search, Boolean activo, UUID sedeId, UUID categoriaId, UUID pisoId, EstadoHabitacion estadoActual) {
        log.info("Obteniendo lista de habitaciones con filtros: search={}, activo={}, sedeId={}, categoriaId={}, pisoId={}, estadoActual={}",
                search, activo, sedeId, categoriaId, pisoId, estadoActual);

        Specification<HabitacionEntity> spec = HabitacionSpecification.getFiltros(search, activo, sedeId, categoriaId, pisoId, estadoActual);

        List<HabitacionEntity> habitaciones = habitacionRepository.findAll(spec);

        return habitacionMapper.toFindListResponse(habitaciones);
    }
}
