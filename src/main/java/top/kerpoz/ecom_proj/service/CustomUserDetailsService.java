package top.kerpoz.ecom_proj.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.kerpoz.ecom_proj.config.security.UserPrincipal;
import top.kerpoz.ecom_proj.model.UserEntity;
import top.kerpoz.ecom_proj.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // This method is called by Spring Security to authenticate a user by their username or email
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + identifier));
        return new UserPrincipal(userEntity);
    }
}