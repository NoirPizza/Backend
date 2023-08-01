package com.pizza.pizzashop.unit.services;

import com.pizza.pizzashop.dtos.RoleDTO;
import com.pizza.pizzashop.entities.Role;
import com.pizza.pizzashop.mappers.RoleMapper;
import com.pizza.pizzashop.repositories.RoleRepository;
import com.pizza.pizzashop.services.RoleService.RoleServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTests {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    RoleMapper roleMapper;
    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoles() {
        Role role1 = new Role(1L, "Admin");
        Role role2 = new Role(2L, "User");
        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        RoleDTO roleDTO1 = new RoleDTO(1L, "Admin");
        RoleDTO roleDTO2 = new RoleDTO(2L, "User");
        when(roleMapper.toDto(role1)).thenReturn(roleDTO1);
        when(roleMapper.toDto(role2)).thenReturn(roleDTO2);

        List<RoleDTO> result = roleService.getRoles();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(roleDTO1, result.get(0));
        assertEquals(roleDTO2, result.get(1));

        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(1)).toDto(role1);
        verify(roleMapper, times(1)).toDto(role2);
    }

    @Test
    void testGetRoleById() {
        Role role = new Role(1L, "Admin");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        RoleDTO roleDTO = new RoleDTO(1L, "Admin");
        when(roleMapper.toDto(role)).thenReturn(roleDTO);

        RoleDTO result = roleService.getRoleById(1L);

        assertNotNull(result);
        assertEquals(roleDTO, result);

        verify(roleRepository, times(1)).findById(1L);
        verify(roleMapper, times(1)).toDto(role);
    }

    @Test
    void testGetRoleById_NonExistingRole() {
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        RoleDTO result = roleService.getRoleById(999L);

        assertNull(result);

        verify(roleRepository, times(1)).findById(999L);
    }
}
