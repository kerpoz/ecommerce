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
import top.kerpoz.ecom_proj.model.UserEntity;
import top.kerpoz.ecom_proj.repository.UserEntityRepository;

import java.util.Optional;

@Service
public class UserEntityService {
    private static final Logger logger = LoggerFactory.getLogger(UserEntityService.class);

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder; // Injected PasswordEncoder
    private final AuthenticationManager authManager; // Injected AuthenticationManager
    private final JwtService jwtService; // Injected JwtService

    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtService jwtService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password
        userEntityRepository.save(user);
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userEntityRepository.findById(id);
    }

    public String verifyUser(UserEntity user) {
        // Validate input
        if ((user.getUsername() == null || user.getUsername().trim().isEmpty()) &&
                (user.getEmail() == null || user.getEmail().trim().isEmpty())) {
            logger.warn("Login attempt without username and email");
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