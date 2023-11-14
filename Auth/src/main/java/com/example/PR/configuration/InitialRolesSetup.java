package com.example.PR.configuration;

import com.example.PR.models.Role;
import com.example.PR.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialRolesSetup implements CommandLineRunner {
    private final RoleRepository roleRepository;
    @Autowired
    public InitialRolesSetup(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }
    private void initializeRoles() {
        createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_SELLER");
        // Добавьте другие роли при необходимости
    }

    private void createRoleIfNotFound(String name) {
        if (roleRepository.findByName(name) == null) {
            Role role = new Role(name, null);
            roleRepository.save(role);
        }
    }
}
