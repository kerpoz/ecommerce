package top.kerpoz.ecom_proj.service;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
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
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

        if (optionalUser.isPresent()) {
            String optionalUserUsername = optionalUser.get().getUsername();
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(optionalUserUsername, user.getPassword())
            );
            return jwtService.generateToken(optionalUserUsername);
        }
        return "Login failed";
    }
}