package koo.securityv2jwt.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class MyFilter1 implements Filter {

    /**
     * MyFilter1은 SecurityConfig에서 스프링 시큐리티 필터 체인에 등록했으므로 제일 먼저 실행이 되는 필터이다.
     * id, password가 들어와서 로그인이 완료되면 토큰을 만들어서 반환됨
     * 이후 매 요청시 헤더의 Authorization에 토큰이 넘어오면 유효한 토큰인지 검증해야함
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("필터1 실행");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getMethod().equals("POST")) {
            /**
             * localhost:8080/token에 POST 요청하기
             * 포스트맨 headers에 Authorization에 cos라고 넣어보기
             * Authorization이 cos가 아니면 컨트롤러 진입 불가능
             */
            log.info("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            log.info("headerAuth: {}", headerAuth);

            if (headerAuth.equals("cos")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
            }
        }
    }

}
