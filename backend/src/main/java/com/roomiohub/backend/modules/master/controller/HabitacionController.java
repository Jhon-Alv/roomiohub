package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.habitacion.request.ChangeHabitacionStatusRequest;
import com.roomiohub.backend.modules.master.dto.habitacion.request.CreateHabitacionRequest;
import com.roomiohub.backend.modules.master.dto.habitacion.request.UpdateHabitacionRequest;
import com.roomiohub.backend.modules.master.dto.habitacion.response.CreateHabitacionResponse;
import com.roomiohub.backend.modules.master.dto.habitacion.response.FindHabitacionResponse;
import com.roomiohub.backend.modules.master.dto.habitacion.response.FindHabitacionesResponse;
import com.roomiohub.backend.modules.master.model.EstadoHabitacion;
import com.roomiohub.backend.modules.master.service.habitacion.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final CreateHabitacionService createHabitacionService;
    private final FindHabitacionesService findHabitacionesService;
    private final FindHabitacionService findHabitacionService;
    private final UpdateHabitacionService updateHabitacionService;
    private final ChangeHabitacionStatusService changeHabitacionStatusService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateHabitacionResponse> createHabitacion(@Valid @RequestBody CreateHabitacionRequest request) {
        CreateHabitacionResponse response = createHabitacionService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FindHabitacionesResponse>> obtenerHabitaciones(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "sedeId") UUID sedeId,
            @RequestParam(value = "categoriaId", required = false) UUID categoriaId,
            @RequestParam(value = "pisoId", required = false) UUID pisoId,
            @RequestParam(value = "estadoActual", required = false) EstadoHabitacion estadoActual
    ) {
        List<FindHabitacionesResponse> response = findHabitacionesService.execute(search, activo, sedeId, categoriaId, pisoId, estadoActual);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FindHabitacionResponse> obtenerHabitacionPorId(@PathVariable UUID id) {
        FindHabitacionResponse response = findHabitacionService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateHabitacion(@PathVariable UUID id, @Valid @RequestBody UpdateHabitacionRequest request) {
        updateHabitacionService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeHabitacionStatus(@PathVariable UUID id, @Valid @RequestBody ChangeHabitacionStatusRequest request) {
        changeHabitacionStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
