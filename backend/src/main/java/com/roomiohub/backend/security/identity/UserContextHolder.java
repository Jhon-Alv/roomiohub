package com.roomiohub.backend.security.identity;

import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.shared.exceptions.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

public class UserContextHolder {

    private UserContextHolder() {
    }

    public static UUID getRequiredUserId() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        if (!(principal instanceof UsuarioEntity)) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado correctamente");
        }
        return ((UsuarioEntity) principal).getId();
    }
    
    public static UsuarioEntity getRequiredUser() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        if (!(principal instanceof UsuarioEntity)) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado correctamente");
        }
        return (UsuarioEntity) principal;
    }
}
