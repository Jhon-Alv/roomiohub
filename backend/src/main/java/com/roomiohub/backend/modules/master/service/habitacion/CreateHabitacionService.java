package com.roomiohub.backend.modules.master.service.habitacion;

import com.roomiohub.backend.modules.master.dto.habitacion.request.CreateHabitacionRequest;
import com.roomiohub.backend.modules.master.dto.habitacion.response.CreateHabitacionResponse;
import com.roomiohub.backend.modules.master.mapper.HabitacionMapper;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import com.roomiohub.backend.modules.master.model.PisoEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.modules.master.repository.PisoRepository;
import com.roomiohub.backend.modules.master.validation.HabitacionValidationService;
import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
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
public class CreateHabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final SedeRepository sedeRepository;
    private final CategoriaRepository categoriaRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionMapper habitacionMapper;
    private final HabitacionValidationService validationService;

    @Transactional
    public CreateHabitacionResponse execute(CreateHabitacionRequest request) {
        log.info("Iniciando proceso de creación de habitación con número: {}", request.getNumero());

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        validationService.validate(tenantId);

        if (habitacionRepository.existsByNumeroAndSedeId(request.getNumero(), request.getSedeId())) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una habitación con ese número en esta sede");
        }

        SedeEntity sede = sedeRepository.findByIdAndTenantId(request.getSedeId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Sede no encontrada o no pertenece al tenant actual"));

        CategoriaEntity categoria = categoriaRepository.findByIdAndTenantId(request.getCategoriaId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no encontrada o no pertenece al tenant actual"));

        PisoEntity piso = pisoRepository.findByIdAndTenantId(request.getPisoId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Piso no encontrado o no pertenece al tenant actual"));

        HabitacionEntity habitacion = habitacionMapper.toCreateEntity(request);
        habitacion.setSede(sede);
        habitacion.setCategoria(categoria);
        habitacion.setPiso(piso);
        habitacion.setTenantId(tenantId);

        HabitacionEntity savedHabitacion = habitacionRepository.persist(habitacion);
        log.info("Creación de habitación con ID: {}", savedHabitacion.getId());

        return habitacionMapper.toCreateResponse(savedHabitacion);
    }
}
