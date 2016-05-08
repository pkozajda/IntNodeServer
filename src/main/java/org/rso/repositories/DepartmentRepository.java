package org.rso.repositories;

import org.rso.entities.Department;
import org.springframework.data.repository.CrudRepository;


public interface DepartmentRepository extends CrudRepository<Department,Long> {
}
