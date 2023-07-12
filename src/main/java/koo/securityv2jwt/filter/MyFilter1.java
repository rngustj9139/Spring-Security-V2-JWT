package koo.securityv2jwt.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("필터1 실행");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getMethod().equals("POST")) {
            /**
             * localhost:8080/token에 POST 요청하기
             * 포스트맨 headers에 Authorization에 코스라고 넣어보기
             * Authorization이 코스가 아니면 컨트롤러 진입 불가능
             */
            log.info("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            log.info("headerAuth: {}", headerAuth);

            if (headerAuth.equals("코스")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
            }
        }
    }

}
