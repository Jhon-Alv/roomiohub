package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.booking.dto.request.CheckOutRequest;
import com.roomiohub.backend.modules.booking.dto.response.CheckOutResponse;
import com.roomiohub.backend.shared.dto.RoomMapDto;
import com.roomiohub.backend.shared.mapper.RoomMapMapper;
import com.roomiohub.backend.modules.booking.mapper.ReservaMapper;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import com.roomiohub.backend.modules.booking.repository.ReservaRepository;
import com.roomiohub.backend.modules.master.api.habitacion.MasterHabitacionPort;
import com.roomiohub.backend.shared.exceptions.RestException;
import com.roomiohub.backend.security.identity.UserContextHolder;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CheckOutService {

    private final ReservaRepository reservaRepository;
    private final MasterHabitacionPort masterHabitacionPort;
    private final SseMapService sseMapService;
    private final RoomMapMapper roomMapMapper;
    private final ReservaMapper reservaMapper;

    @Transactional
    public CheckOutResponse execute(UUID habitacionId, CheckOutRequest request) {

        UUID sedeId = TenantContextHolder.resolveSedeId(request.getSedeId());
        UUID usuarioId = UserContextHolder.getRequiredUserId();

        ReservaEntity reserva = reservaRepository.findByHabitacionIdAndFechaSalidaRealIsNull(habitacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "No hay una reserva activa para esta habitación"));

        HabitacionDataDto habitacionData = masterHabitacionPort.getHabitacionById(habitacionId);

        if (EstadoHabitacion.OCUPADO != habitacionData.getEstadoActual()) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Solo se puede realizar el check-out cuando la habitación está en estado OCUPADO");
        }

        if (!reserva.getSedeId().equals(sedeId)) {
            throw new RestException(HttpStatus.FORBIDDEN, "La reserva no pertenece a esta sede");
        }

        reserva.finalizarEstadia(request.getMontoPenalidad());

        reservaRepository.update(reserva);

        masterHabitacionPort.updateEstadoHabitacion(habitacionId, EstadoHabitacion.SUCIO, usuarioId, null);

        habitacionData.setEstadoActual(EstadoHabitacion.SUCIO);
        RoomMapDto roomMapDto = roomMapMapper.toDto(habitacionData);
        sseMapService.schedulePublishAfterCommit(sedeId, roomMapDto);

        return reservaMapper.toCheckOutResponse(reserva);
    }
}
