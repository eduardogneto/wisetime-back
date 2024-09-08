package com.wisetime.wisetime.repository.role;

import com.wisetime.wisetime.models.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Método para buscar os cargos com base no organizationId
    List<Role> findByOrganizationId(Long organizationId);
}

