package com.roomiohub.backend.modules.auth.controller;

import com.roomiohub.backend.modules.auth.dto.request.ChangePasswordRequest;
import com.roomiohub.backend.modules.auth.dto.request.LoginRequest;
import com.roomiohub.backend.modules.auth.dto.request.RegisterRequest;
import com.roomiohub.backend.modules.auth.dto.response.LoginResponse;
import com.roomiohub.backend.modules.auth.dto.response.RegisterResponse;
import com.roomiohub.backend.modules.auth.service.ChangePasswordService;
import com.roomiohub.backend.modules.auth.service.LoginService;
import com.roomiohub.backend.modules.auth.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final RegisterService registerService;
    private final ChangePasswordService changePasswordService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.execute(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerService.execute(request));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordService.execute(request);
        return ResponseEntity.noContent().build();
    }

}
