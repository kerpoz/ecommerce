package top.kerpoz.ecom_proj.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.kerpoz.ecom_proj.model.entity.Role;
import top.kerpoz.ecom_proj.model.entity.UserEntity;
import top.kerpoz.ecom_proj.model.enums.RoleType;
import top.kerpoz.ecom_proj.repository.RoleRepository;
import top.kerpoz.ecom_proj.repository.UserEntityRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserEntityService {
    private static final Logger logger = LoggerFactory.getLogger(UserEntityService.class);

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder; // Injected PasswordEncoder
    private final AuthenticationManager authManager; // Injected AuthenticationManager
    private final JwtService jwtService; // Injected JwtService
    private final RoleRepository roleRepository;

    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtService jwtService, RoleRepository roleRepository) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void register(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Fetch the default role
        Role defaultRole = roleRepository.findByName(RoleType.ROLE_USER.name())
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // Assign the default role to the user
        user.setRoles(Collections.singleton(defaultRole));

        userEntityRepository.save(user);
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userEntityRepository.findById(id);
    }

    public String verifyUser(UserEntity user) {
        // Validate input
        if ((user.getUsername() == null || user.getUsername().trim().isEmpty()) &&
                (user.getEmail() == null || user.getEmail().trim().isEmpty())) {
            logger.warn("Login attempt failed: both username and email are empty");
            throw new IllegalArgumentException("Username or email must be provided (input was empty)");
        }

        // Attempt to find the user by username or email
        UserEntity foundUser = userEntityRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Failed login attempt for user: {}", user.getUsername());
                    return new BadCredentialsException("Invalid credentials");
                });

        // Authenticate using username and password
        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(foundUser.getUsername(), user.getPassword()));

            // If authentication is successful, generate the token
            logger.info("User {} successfully authenticated", user.getUsername());
            return jwtService.generateToken(foundUser.getUsername());

        } catch (BadCredentialsException e) {
            logger.warn("Invalid password for user: {}", foundUser.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            logger.error("Unexpected error during authentication", e);
            throw new RuntimeException("Authentication failed");
        }
    }
}