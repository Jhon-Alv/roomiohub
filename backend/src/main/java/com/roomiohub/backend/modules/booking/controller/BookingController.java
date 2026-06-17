package com.roomiohub.backend.modules.booking.controller;

import com.roomiohub.backend.modules.booking.dto.request.CheckInRequestDto;
import com.roomiohub.backend.modules.booking.dto.request.CheckOutRequestDto;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import com.roomiohub.backend.modules.booking.service.CheckInService;
import com.roomiohub.backend.modules.booking.service.CheckOutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booking/operaciones")
@RequiredArgsConstructor
public class BookingController {

    private final CheckInService checkInService;
    private final CheckOutService checkOutService;

    @PostMapping("/checkin/habitacion/{habitacionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<ReservaEntity> realizarCheckIn(
            @PathVariable UUID habitacionId,
            @Valid @RequestBody CheckInRequestDto request
    ) {
        ReservaEntity reserva = checkInService.execute(habitacionId, request);
        return new ResponseEntity<>(reserva, HttpStatus.CREATED);
    }

    @PostMapping("/checkout/habitacion/{habitacionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<ReservaEntity> realizarCheckOut(
            @PathVariable UUID habitacionId,
            @Valid @RequestBody CheckOutRequestDto request
    ) {
        ReservaEntity reserva = checkOutService.execute(habitacionId, request);
        return ResponseEntity.ok(reserva);
    }
}
