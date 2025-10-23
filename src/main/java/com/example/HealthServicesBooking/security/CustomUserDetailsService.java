package com.example.HealthServicesBooking.security;

import com.example.HealthServicesBooking.entity.User;
import com.example.HealthServicesBooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user with roles eagerly to avoid LazyInitializationException
        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Check if account is active
        if (!user.getIsActive()) {
            throw new DisabledException("Account has been disabled. Please contact administrator.");
        }

        return CustomUserDetails.build(user);
    }
}
