package com.roomiohub.backend.modules.booking.controller;

import com.roomiohub.backend.modules.booking.service.SseMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booking/map")
@RequiredArgsConstructor
public class RoomMapController {

    private final SseMapService sseMapService;

    @GetMapping(value = "/stream/{sedeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'LIMPIEZA')")
    public SseEmitter streamRoomMap(@PathVariable UUID sedeId) {
        return sseMapService.subscribeToMap(sedeId);
    }

}
