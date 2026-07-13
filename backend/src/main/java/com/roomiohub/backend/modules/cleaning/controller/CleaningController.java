package com.roomiohub.backend.modules.cleaning.controller;

import com.roomiohub.backend.modules.cleaning.dto.request.IniciarLimpiezaRequest;
import com.roomiohub.backend.modules.cleaning.dto.request.FinalizarLimpiezaRequest;
import com.roomiohub.backend.modules.cleaning.dto.response.IniciarLimpiezaResponse;
import com.roomiohub.backend.modules.cleaning.dto.response.FinalizarLimpiezaResponse;
import com.roomiohub.backend.modules.cleaning.service.IniciarLimpiezaService;
import com.roomiohub.backend.modules.cleaning.service.FinalizarLimpiezaService;
import com.roomiohub.backend.modules.cleaning.service.SseCleaningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cleaning")
@RequiredArgsConstructor
public class CleaningController {

    private final SseCleaningService sseCleaningService;
    private final IniciarLimpiezaService iniciarLimpiezaService;
    private final FinalizarLimpiezaService finalizarLimpiezaService;

    @GetMapping(value = "/stream/{sedeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'LIMPIEZA')")
    public SseEmitter streamCleaning(@PathVariable UUID sedeId) {
        return sseCleaningService.subscribeToCleaning(sedeId);
    }

    @PostMapping("/habitacion/{habitacionId}/iniciar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'LIMPIEZA')")
    public ResponseEntity<IniciarLimpiezaResponse> iniciarLimpieza(
            @PathVariable UUID habitacionId,
            @Valid @RequestBody IniciarLimpiezaRequest request) {
        return ResponseEntity.ok(iniciarLimpiezaService.execute(habitacionId, request));
    }

    @PostMapping("/habitacion/{habitacionId}/finalizar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'LIMPIEZA')")
    public ResponseEntity<FinalizarLimpiezaResponse> finalizarLimpieza(
            @PathVariable UUID habitacionId,
            @Valid @RequestBody FinalizarLimpiezaRequest request) {
        return ResponseEntity.ok(finalizarLimpiezaService.execute(habitacionId, request));
    }
}
