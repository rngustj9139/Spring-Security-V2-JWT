package koo.securityv2jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 스프링 시큐리티는 UsernamePasswordAuthenticationFilter라는 필터를 가지고 있음
 * /login 요청시 username, password 전송하면 (POST)
 * UsernamePasswordAuthenticationFilter이 동작을 한다.
 * 근데 formLigin().disable()이므로 이 필터가 동작을 안하는데
 * SecurityConfig에 등록을 하면된다.
 * 이 필터는 AuthenticationManager로 로그인을 진행시킴
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override // 로그인 시도하고 필터 동작시 이 함수가 실행된다.
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter: 로그인 시도중");

        return super.attemptAuthentication(request, response);
    }

}
