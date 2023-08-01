package com.pizza.pizzashop.services.RoleService;

import com.pizza.pizzashop.dtos.RoleDTO;

import java.util.List;

/**
 * This interface provides methods for fetching roles.
 */
public interface RoleService {
    /**
     * Retrieves a list of all roles.
     *
     * @return A list of RoleDTO representing all roles.
     */
    List<RoleDTO> getRoles();

    /**
     * Retrieves a role by its unique identifier.
     *
     * @param id The unique identifier of the role to retrieve.
     * @return The RoleDTO representing the role with the specified ID.
     */
    RoleDTO getRoleById(Long id);
}
