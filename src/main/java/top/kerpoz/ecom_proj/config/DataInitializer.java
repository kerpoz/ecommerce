package top.kerpoz.ecom_proj.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import top.kerpoz.ecom_proj.model.entity.Role;
import top.kerpoz.ecom_proj.model.entity.UserEntity;
import top.kerpoz.ecom_proj.model.enums.RoleType;
import top.kerpoz.ecom_proj.repository.RoleRepository;
import top.kerpoz.ecom_proj.repository.UserEntityRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@Profile("!test") // Exclude this configuration from test profile
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void init() {
        List<RoleType> roles = Arrays.asList(RoleType.ROLE_USER, RoleType.ROLE_ADMIN);

        for (RoleType roleType : roles) {
            if (!roleRepository.findByName(roleType.name()).isPresent()) {
                Role role = new Role();
                role.setName(roleType.name());
                roleRepository.save(role);
            }
        }

        if (!userEntityRepository.findByUsername("admin").isPresent()) {
            UserEntity adminUser = new UserEntity();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("adminPassword"));
            adminUser.setEmail("admin@example.com");
            Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN.name())
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            adminUser.setRoles(Collections.singleton(adminRole));
            userEntityRepository.save(adminUser);
        }

        if (!userEntityRepository.findByUsername("user").isPresent()) {
            UserEntity normalUser = new UserEntity();
            normalUser.setUsername("testUsername");
            normalUser.setPassword(passwordEncoder.encode("testPassword"));
            normalUser.setEmail("testUsername@example.com");
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER.name())
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            normalUser.setRoles(Collections.singleton(userRole));
            userEntityRepository.save(normalUser);
        }
    }
}