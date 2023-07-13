package koo.securityv2jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import koo.securityv2jwt.config.auth.PrincipalDetails;
import koo.securityv2jwt.model.User;
import koo.securityv2jwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 클라이언트가 인증 후 매번 서버에게 보내는 요청에 포함된 jwt가 유효한지 판단
 *
 * 시큐리티는 filter를 가지고 있는데 필터중에 BasicAuthenticationFilter라는게 있음
 * 권한이 있거나 인증완료 상태인 경우만 요청가능한 특정 주소를 요청했을때 위 필터를 무조건 타게 되어있음
 * ex) SecurityConfig의 /api/v1/user/
 **/
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) { // /api/v1/user/test GET으로 요청해보기
        super(authenticationManager);
        this.userRepository = userRepository;
   }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilterInternal(request, response, chain);
        log.info("인증이나 권한이 필요한 주소 요청이 실행됨");

        String jwtHeader = request.getHeader("Authorization");
        log.info("jwtHeader: {}", jwtHeader);

        /**
         *  jwt 토큰을 검증해서 유효한지 확인
        */
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);

            return;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ", ""); // Bearer + 한칸 스페이스 부분을 그냥 공백으로 치환
        String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();

        if (username != null) { // 서명이 정상적으로 된 경우 (Authentication 객체를 만들어준다.)
            User userEntity = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities()); // password 대신에 null 넣기
            SecurityContextHolder.getContext().setAuthentication(authentication); // 강제로 SecurityContextHolder에 Authentication 넣기

            chain.doFilter(request, response);
        }

    }

}
