package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.RoleDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.services.RoleService.RoleService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class handles role-related API endpoints for fetching operations.
 * It provides methods to retrieve all roles and fetch a role by its ID,
 */
@Validated
@RestController
@RequestMapping("/api/user/role")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Retrieves all roles from the RoleService and returns them as a list in the HTTP response.
     *
     * @return ResponseEntity containing a SuccessDTO with the list of RoleDTOs and a success message.
     */
    @GetMapping
    public ResponseEntity<SuccessDTO<List<RoleDTO>>> getAllRoles() {
        List<RoleDTO> roles = roleService.getRoles();
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Role Get All",
                        roles
                ), HttpStatus.OK);
    }

    /**
     * Retrieves a role by its ID from the RoleService and returns it in the HTTP response.
     *
     * @param id The ID of the role to retrieve.
     * @return ResponseEntity containing a SuccessDTO with the RoleDTO and a success message.
     * @throws NotFoundException If the role with the specified ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessDTO<RoleDTO>> getRoleById(
            @Positive(message = " Role ID must be positive") @PathVariable Long id
    ) throws NotFoundException {
        RoleDTO roleDTO = roleService.getRoleById(id);
        if (roleDTO != null) {
            return new ResponseEntity<>(
                    new SuccessDTO<>(
                            HttpStatus.OK.value(),
                            "Role Get By Id",
                            roleDTO
                    ), HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find role with ID " + id);
    }
}
