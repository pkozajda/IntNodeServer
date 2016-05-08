package org.rso.repositories;

import org.rso.entities.Graduate;
import org.springframework.data.repository.CrudRepository;

public interface GraduateRepository extends CrudRepository<Graduate,Long> {
}
