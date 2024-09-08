package com.wisetime.wisetime.service.role;

import com.wisetime.wisetime.models.role.Role;
import com.wisetime.wisetime.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Método para buscar cargos com base no organizationId
    public List<Role> getRolesByOrganization(Long organizationId) {
        return roleRepository.findByOrganizationId(organizationId);
    }
}
