package com.roomiohub.backend.security.identity.repository;

import com.roomiohub.backend.security.identity.model.RolUsuario;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioSpecification {

    public static Specification<UsuarioEntity> getFiltros(UUID sedeId, String search, RolUsuario rol, Boolean activo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sedeId != null) {
                predicates.add(cb.equal(root.get("sedeId"), sedeId));
            }

            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                Predicate nombrePredicate = cb.like(cb.lower(root.get("nombre")), pattern);
                Predicate usernamePredicate = cb.like(cb.lower(root.get("username")), pattern);
                predicates.add(cb.or(nombrePredicate, usernamePredicate));
            }

            if (rol != null) {
                predicates.add(cb.equal(root.get("rol"), rol));
            }

            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
