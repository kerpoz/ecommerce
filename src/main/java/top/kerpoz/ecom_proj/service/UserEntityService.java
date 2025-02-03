package top.kerpoz.ecom_proj.service;

import jakarta.transaction.Transactional;
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
        if (user.getUsername() == null && user.getEmail() == null) {
            throw new IllegalArgumentException("Username or email must be provided");
        }

        // Attempt to find the user by username or email
        UserEntity foundUser = userEntityRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        // Authenticate using username and password
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(foundUser.getUsername(), user.getPassword()));

            // If authentication is successful, generate the token
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(foundUser.getUsername());
            }
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect password"); // Specific message for wrong password
        }

        // This line should not be reached, but just in case:
        throw new BadCredentialsException("Login failed");
    }
}