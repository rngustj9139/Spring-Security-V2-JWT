package koo.securityv2jwt.config.auth;

import koo.securityv2jwt.model.User;
import koo.securityv2jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * http://localhost:8080/login 요청은 .formLogin().disable()이므로 동작 안한다.
 * 따라서 우리가 필터를 하나 만들어야함(JwtAuthenticationFilter)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService의 loadUserByUsername 동작");
        User userEntity = userRepository.findByUsername(username);

        return new PrincipalDetails(userEntity);
    }

}
