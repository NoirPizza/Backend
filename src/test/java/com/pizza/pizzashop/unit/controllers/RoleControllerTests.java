package com.pizza.pizzashop.unit.controllers;

import com.pizza.pizzashop.controllers.RoleController;
import com.pizza.pizzashop.dtos.RoleDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.services.RoleService.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoleControllerTests {
    @Mock
    private RoleService roleService;
    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoles() {
        List<RoleDTO> roles = new ArrayList<>();
        roles.add(new RoleDTO(1L, "ADMIN"));
        roles.add(new RoleDTO(2L, "USER"));
        when(roleService.getRoles()).thenReturn(roles);

        ResponseEntity<SuccessDTO<List<RoleDTO>>> responseEntity = roleController.getAllRoles();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().getData().size());
        assertEquals("ADMIN", responseEntity.getBody().getData().get(0).getName());
        assertEquals("USER", responseEntity.getBody().getData().get(1).getName());
        verify(roleService, times(1)).getRoles();
    }

    @Test
    void testGetRoleById() throws NotFoundException {
        RoleDTO roleDTO = new RoleDTO(1L, "ADMIN");
        when(roleService.getRoleById(1L)).thenReturn(roleDTO);

        ResponseEntity<SuccessDTO<RoleDTO>> responseEntity = roleController.getRoleById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("ADMIN", responseEntity.getBody().getData().getName());
        verify(roleService, times(1)).getRoleById(1L);
    }

    @Test
    void testGetRoleById_NonExistingRole() {
        when(roleService.getRoleById(999L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> roleController.getRoleById(999L));
        verify(roleService, times(1)).getRoleById(999L);
    }

// TODO: figure out how to test validation of PathVariable

//    @Test
//    public void testGetRoleById_NegativeRoleId() {
//        when(roleService.getRoleById(-1L)).thenReturn(null);
//
//        assertThrows(RequestDataValidationFailedException.class, () -> roleController.getRoleById(-1L));
//        verify(roleService, times(1)).getRoleById(-1L);
//    }
}




