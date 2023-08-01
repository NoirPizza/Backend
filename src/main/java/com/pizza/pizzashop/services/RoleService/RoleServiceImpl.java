package com.pizza.pizzashop.services.RoleService;

import com.pizza.pizzashop.dtos.RoleDTO;
import com.pizza.pizzashop.entities.Role;
import com.pizza.pizzashop.mappers.RoleMapper;
import com.pizza.pizzashop.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is an implementation of the RoleService interface that provides methods for fetching roles.
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Retrieves a list of all roles.
     *
     * @return A list of RoleDTO representing all roles.
     */
    @Override
    public List<RoleDTO> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toDto).toList();
    }

    /**
     * Retrieves a role by its unique identifier.
     *
     * @param id The unique identifier of the role to retrieve.
     * @return The RoleDTO representing the role with the specified ID.
     */
    @Override
    public RoleDTO getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(roleMapper::toDto).orElse(null);
    }
}
