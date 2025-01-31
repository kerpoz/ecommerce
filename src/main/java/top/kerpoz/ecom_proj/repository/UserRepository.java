package top.kerpoz.ecom_proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.kerpoz.ecom_proj.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}