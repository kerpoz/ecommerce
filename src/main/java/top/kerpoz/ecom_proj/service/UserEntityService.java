package top.kerpoz.ecom_proj.service;

import org.springframework.stereotype.Service;
import top.kerpoz.ecom_proj.model.UserEntity;
import top.kerpoz.ecom_proj.repository.UserRepository;

import java.util.Optional;

@Service
public class UserEntityService {
    private final UserRepository userRepository;

    public UserEntityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserEntity user) {
        userRepository.save(user);
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
