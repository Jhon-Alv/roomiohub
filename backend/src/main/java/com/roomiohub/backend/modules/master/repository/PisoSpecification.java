package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.PisoEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PisoSpecification {

    public static Specification<PisoEntity> getFiltros(String search, Boolean activo, UUID sedeId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sedeId != null) {
                predicates.add(cb.equal(root.get("sede").get("id"), sedeId));
            }

            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                Predicate nombrePredicate = cb.like(cb.lower(root.get("nombre")), pattern);
                predicates.add(nombrePredicate);
            }

            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
