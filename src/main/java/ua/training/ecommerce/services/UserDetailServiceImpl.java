package ua.training.ecommerce.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.ecommerce.cache.RedisCache;
import ua.training.ecommerce.models.User;
import ua.training.ecommerce.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RedisCache cache;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var cacheKey = buildCacheKey(username);
        var user = Optional.ofNullable(cache.getItem(cacheKey, User.class))
                .orElseGet(() -> userRepository.findByEmail(username));
        if (user == null) {
            throw new UsernameNotFoundException("No user found. Username: " + username);
        }
        cache.setItem(cacheKey, user);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("admin"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    private String buildCacheKey(String username) {
        return "user/" + username;
    }
}
