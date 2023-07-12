package koo.securityv2jwt.config;

import koo.securityv2jwt.filter.MyFilter1;
import koo.securityv2jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * 쿠키의 단점
 * 1. 동일한 도메인으로부터 온 요청만 허용한다
 * ex) 145.12.672.43 => www.naver.com
 * 2. http only여서 자바스크립트에서 코드로 헤더에 쿠키를 담아 전송하면 서버에서 거부한다 (http only를 false로 풀어서 해결 가능)
 */
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
        .addFilterBefore(new MyFilter1(), SecurityContextPersistenceFilter.class) // 스프링 시큐리티 필터체인에 필터 등록 (스프링 시큐리티 필터체인에 등록된 필드들이 우리가 만들고 적용한 필터보다 먼저 수행된다.)(SecurityContextPersistenceFilter는 스프링 시큐리티 필터 체인의 체인중 제일 먼저 실행되는 필터이다.)
        .formLogin().disable() // jwt 서버를 쓰니깐 폼로그인 disable (폼 태그써서 로그인 안할 것이다.)
        /**
         * 기본적인 http 로그인 방식 사용 안함
         * (매번 헤더의 authorization에 id와 password를 담아 전송하는 방식 이거 쓰면 쿠키와 세션을 이용할 필요가 없음,
         * 이것의 단점은 암호화가 안되는 것이고 암호화를 하려면 https를 이용해야함)
         * 이 대신 헤더의 authorization에 토큰(jwt)을 담는 방식을 이용 => 이것은 httpBasic 방식이 아닌 bearer token 방식임
         */
        .httpBasic().disable()
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
