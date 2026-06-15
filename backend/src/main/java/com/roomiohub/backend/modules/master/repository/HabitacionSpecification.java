package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.EstadoHabitacion;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HabitacionSpecification {

    public static Specification<HabitacionEntity> getFiltros(String search, Boolean activo, UUID sedeId, UUID categoriaId, UUID pisoId, EstadoHabitacion estadoActual) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sedeId != null) {
                predicates.add(cb.equal(root.get("sede").get("id"), sedeId));
            }
            if (categoriaId != null) {
                predicates.add(cb.equal(root.get("categoria").get("id"), categoriaId));
            }
            if (pisoId != null) {
                predicates.add(cb.equal(root.get("piso").get("id"), pisoId));
            }
            if (estadoActual != null) {
                predicates.add(cb.equal(root.get("estadoActual"), estadoActual));
            }
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                Predicate numeroPredicate = cb.like(cb.lower(root.get("numero")), pattern);
                predicates.add(numeroPredicate);
            }
            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
