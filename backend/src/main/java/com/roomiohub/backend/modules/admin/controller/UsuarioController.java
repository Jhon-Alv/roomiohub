package com.roomiohub.backend.modules.admin.controller;

import com.roomiohub.backend.modules.admin.dto.usuario.request.ChangeUsuarioStatusRequest;
import com.roomiohub.backend.modules.admin.dto.usuario.request.CreateUsuarioRequest;
import com.roomiohub.backend.modules.admin.dto.usuario.request.UpdateUsuarioRequest;
import com.roomiohub.backend.modules.admin.dto.usuario.response.CreateUsuarioResponse;
import com.roomiohub.backend.modules.admin.dto.usuario.response.FindUsuarioResponse;
import com.roomiohub.backend.modules.admin.dto.usuario.response.FindUsuariosResponse;
import com.roomiohub.backend.modules.admin.service.usuario.*;
import com.roomiohub.backend.security.identity.model.RolUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CreateUsuarioService createUsuarioService;
    private final FindUsuariosService findUsuariosService;
    private final FindUsuarioService findUsuarioService;
    private final UpdateUsuarioService updateUsuarioService;
    private final ChangeUsuarioStatusService changeUsuarioStatusService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUsuarioResponse> createUsuario(@Valid @RequestBody CreateUsuarioRequest request) {
        CreateUsuarioResponse response = createUsuarioService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FindUsuariosResponse>> obtenerUsuarios(
            @RequestParam(value = "sedeId", required = false) UUID sedeId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "rol", required = false) RolUsuario rol,
            @RequestParam(value = "activo", required = false) Boolean activo
    ) {
        List<FindUsuariosResponse> response = findUsuariosService.execute(sedeId, search, rol, activo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FindUsuarioResponse> obtenerUsuarioPorId(@PathVariable UUID id){
        FindUsuarioResponse response = findUsuarioService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUsuario(@PathVariable UUID id, @Valid @RequestBody UpdateUsuarioRequest request) {
        updateUsuarioService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeUsuarioStatus(@PathVariable UUID id, @Valid @RequestBody ChangeUsuarioStatusRequest request) {
        changeUsuarioStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }



}
