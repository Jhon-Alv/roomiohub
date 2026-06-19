package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.booking.dto.request.CheckInRequest;
import com.roomiohub.backend.modules.booking.dto.response.CheckInResponse;
import com.roomiohub.backend.modules.booking.dto.response.RoomMapDto;
import com.roomiohub.backend.modules.booking.mapper.RoomMapMapper;
import com.roomiohub.backend.modules.booking.mapper.ReservaMapper;
import com.roomiohub.backend.modules.booking.model.ClienteEntity;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import com.roomiohub.backend.modules.booking.repository.ClienteRepository;
import com.roomiohub.backend.modules.booking.repository.ReservaRepository;
import com.roomiohub.backend.modules.master.api.habitacion.MasterHabitacionPort;
import com.roomiohub.backend.modules.master.api.tarifa.MasterTarifaPort;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.security.identity.UserContextHolder;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CheckInService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final MasterHabitacionPort masterHabitacionPort;
    private final MasterTarifaPort masterTarifaPort;
    private final SseMapService sseMapService;
    private final RoomMapMapper roomMapMapper;
    private final ReservaMapper reservaMapper;

    @Transactional
    public CheckInResponse execute(UUID habitacionId, CheckInRequest request) {

        HabitacionDataDto habitacionSeleccionada = masterHabitacionPort.getHabitacionById(habitacionId);

        if (EstadoHabitacion.DISPONIBLE != habitacionSeleccionada.getEstadoActual()) {
            throw new RestException(HttpStatus.BAD_REQUEST, "La habitación no está disponible para check-in");
        }

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        UUID usuarioId = UserContextHolder.getRequiredUserId();

        ClienteEntity cliente = clienteRepository.findByNumeroDocumentoAndTenantId(request.getNumeroDocumento(), tenantId)
                .orElseGet(() -> {
                    ClienteEntity nuevoCliente = ClienteEntity.builder()
                            .numeroDocumento(request.getNumeroDocumento())
                            .nombres(request.getNombresCliente())
                            .build();
                    return clienteRepository.persist(nuevoCliente);
                });

        Integer horas = masterTarifaPort.getHorasByTarifaId(request.getTarifaId());
        BigDecimal montoSubtotal = masterTarifaPort.getPrecioBaseByTarifaId(request.getTarifaId());
        BigDecimal precioPromocional = null;

        if (request.getPromocionId() != null) {
            precioPromocional = masterTarifaPort.getPrecioPromocional(request.getPromocionId(), request.getTarifaId());
        }

        ReservaEntity reserva = ReservaEntity.builder()
                .sedeId(request.getSedeId())
                .habitacionId(habitacionId)
                .usuarioIngresoId(usuarioId)
                .clienteId(cliente.getId())
                .tarifaId(request.getTarifaId())
                .promocionId(request.getPromocionId())
                .cantidadHuespedes(request.getCantidadHuespedes())
                .metodoPago(request.getMetodoPago())
                .build();

        reserva.iniciarEstadia(horas, montoSubtotal, precioPromocional);

        ReservaEntity savedReserva = reservaRepository.persist(reserva);

        masterHabitacionPort.updateEstadoHabitacion(habitacionId, EstadoHabitacion.OCUPADO);

        habitacionSeleccionada.setEstadoActual(EstadoHabitacion.OCUPADO);
        RoomMapDto roomMapDto = roomMapMapper.toDto(habitacionSeleccionada);
        sseMapService.publishUpdate(request.getSedeId(), roomMapDto);

        return reservaMapper.toCheckInResponse(savedReserva);
    }
}
