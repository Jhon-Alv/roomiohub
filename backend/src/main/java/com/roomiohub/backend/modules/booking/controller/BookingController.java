package com.roomiohub.backend.modules.booking.controller;

import com.roomiohub.backend.modules.booking.dto.request.CheckInRequest;
import com.roomiohub.backend.modules.booking.dto.request.CheckOutRequest;
import com.roomiohub.backend.modules.booking.dto.response.FindReservaActivaResponse;
import com.roomiohub.backend.modules.booking.dto.response.FindClienteResponse;
import com.roomiohub.backend.modules.booking.dto.response.CheckInResponse;
import com.roomiohub.backend.modules.booking.dto.response.CheckOutResponse;
import com.roomiohub.backend.modules.booking.service.CheckInService;
import com.roomiohub.backend.modules.booking.service.CheckOutService;
import com.roomiohub.backend.modules.booking.service.FindReservaActivaService;
import com.roomiohub.backend.modules.booking.service.FindClienteService;
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
    private final FindReservaActivaService findReservaActivaService;
    private final FindClienteService findClienteService;

    @GetMapping("/clientes/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<FindClienteResponse> searchCliente(@RequestParam String documento) {
        return ResponseEntity.ok(findClienteService.execute(documento));
    }

    @GetMapping("/reserva-activa/habitacion/{habitacionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<FindReservaActivaResponse> findReservaActiva(@PathVariable UUID habitacionId) {
        return ResponseEntity.ok(findReservaActivaService.execute(habitacionId));
    }

    @PostMapping("/checkin/habitacion/{habitacionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<CheckInResponse> realizarCheckIn(
            @PathVariable UUID habitacionId,
            @Valid @RequestBody CheckInRequest request
    ) {
        CheckInResponse response = checkInService.execute(habitacionId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/checkout/habitacion/{habitacionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<CheckOutResponse> realizarCheckOut(
            @PathVariable UUID habitacionId,
            @Valid @RequestBody CheckOutRequest request
    ) {
        CheckOutResponse response = checkOutService.execute(habitacionId, request);
        return ResponseEntity.ok(response);
    }
}
