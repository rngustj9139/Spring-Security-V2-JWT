package koo.securityv2jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import koo.securityv2jwt.config.auth.PrincipalDetails;
import koo.securityv2jwt.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * 스프링 시큐리티는 UsernamePasswordAuthenticationFilter라는 필터를 가지고 있음
 * /login 요청시 username, password 전송하면 (POST)
 * UsernamePasswordAuthenticationFilter이 동작을 한다.
 * 근데 formLigin().disable()이므로 이 필터가 동작을 안하는데
 * SecurityConfig에 등록을 하면된다.
 * 이 필터는 AuthenticationManager로 로그인을 진행시킴
 * AuthenticationManager가 실행되면 PrincipalDetailsService의 loadUserByUsername이 실행된다
 * 이후 PrincipalDetails를 세션에 담고(굳이 세션에 담는 이유: SecurityConfig의 인가 관리를 위해서이다.)
 * jwt를 만들어서 응답한다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override // 로그인 시도하고 필터 동작시 이 함수가 실행된다. (localhost:8080/login으로 요청해야한다)
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter: 로그인 시도중");

        try {
//            BufferedReader br = request.getReader();
//            String input = null;
//
//            while ((input = br.readLine()) != null) {
//                log.info("input: {}", input); // 로그인시 username=ssar&assword=1234가 출력됨 json으로 보내면 json형태로 출력된다.
//            }

            // json으로 받는다고 가정하고 파싱
            ObjectMapper om = new ObjectMapper(); // json을 객체로 파싱
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            // 토큰 만들기
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            /**
             * 이떄 아랫줄에서 Authentication에 각각의 유저의 로그인한 정보가 담긴다.
             * PrincipalDetailsService의 loadUserByUsername 함수가 실행되고 정상이면 authentication이 리턴된다.
             * 정상이라는 것은 DB에 있는 username과 password가 일치한다는 것이다.
             */
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            // 리턴하기(SecurityContextHolder속에 authentication을 넣는다 -> 세션을 만든다(SecurityConfig의 인가 때문에))
            return authentication;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * attemptAuthentication실행 후 인증이 성공하면 이 함수가 실행됨
     * 이 함수에서 JWT를 만들어서 사용자에게 응답
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication 함수 실행: 인증 완료됨");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // jwt 토큰 만들기
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 2))) // 1000이 1초
                .withClaim("id", principalDetails.getUser().getId()) // 개인 클레임
                .withClaim("username", principalDetails.getUser().getUsername()) // 개인 클레임
                .sign(Algorithm.HMAC512("cos")); // secret은 cos, RSA방식은 아니고 해시 방식임

        response.addHeader("Authorization", "Bearer " + jwtToken); // Bearer에 한칸 띄어야함
    }

}
