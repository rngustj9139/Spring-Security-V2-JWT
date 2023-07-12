package koo.securityv2jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter; // CorsConfig의 CorsFilter를 DI

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다.
            .and()
        .addFilter(corsFilter) // 필터 등록 (인증이 필요하면 이 필터를 이용하고 인증이 필요 없으면 컨트롤러에 @CorsOrigin 어노테이션 붙이기)
        .formLogin().disable() // jwt 서버를 쓰니깐 폼로그인 disable (폼 태그써서 로그인 안할 것이다.)
        .httpBasic().disable() // 기본적인 http 로그인 방식 사용 안함 (매번 헤더의 authorization에 id와 password를 담아 전송하는 방식 이거 쓰면 쿠키와 세션을 이용할 필요가 없음, 이것의 단점은 암호화가 안되는 것이고 암호화를 하려면 https를 이용해야함)
        .authorizeRequests()
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        .anyRequest()
        .permitAll();
    }

}
