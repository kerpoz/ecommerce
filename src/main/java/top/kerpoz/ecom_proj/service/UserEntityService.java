package top.kerpoz.ecom_proj.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.kerpoz.ecom_proj.model.UserEntity;
import top.kerpoz.ecom_proj.repository.UserEntityRepository;

import java.util.Optional;

@Service
public class UserEntityService {
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder; // Injected PasswordEncoder

    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password
        userEntityRepository.save(user);
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userEntityRepository.findById(id);
    }
}
