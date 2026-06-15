package com.roomiohub.backend.modules.master.service.habitacion;

import com.roomiohub.backend.modules.master.dto.habitacion.request.UpdateHabitacionRequest;
import com.roomiohub.backend.modules.master.mapper.HabitacionMapper;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import com.roomiohub.backend.modules.master.model.PisoEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.modules.master.repository.PisoRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateHabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final CategoriaRepository categoriaRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionMapper habitacionMapper;

    @Transactional
    public void execute(UUID id, UpdateHabitacionRequest request) {
        log.info("Iniciando proceso de actualización de habitación con ID: {}", id);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        HabitacionEntity habitacion = habitacionRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Habitación no existe o no tiene acceso"));

        if (!habitacion.getNumero().equals(request.getNumero()) && habitacionRepository.existsByNumeroAndSedeId(request.getNumero(), habitacion.getSede().getId())) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una habitación con ese número en esta sede");
        }

        CategoriaEntity categoria = categoriaRepository.findByIdAndTenantId(request.getCategoriaId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no encontrada o no pertenece al tenant actual"));

        PisoEntity piso = pisoRepository.findByIdAndTenantId(request.getPisoId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Piso no encontrado o no pertenece al tenant actual"));

        habitacionMapper.toUpdateEntity(habitacion, request);
        habitacion.setCategoria(categoria);
        habitacion.setPiso(piso);

        habitacionRepository.merge(habitacion);

        log.info("Habitación con ID: {} actualizada exitosamente", id);
    }
}
