package koo.securityv2jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 내서버가 응답한 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*"); // 모든 ip에 허용
        config.addAllowedHeader("*"); // 모든 헤더에 허용
        config.addAllowedMethod("*"); // 모든 메서드에 허용

        source.registerCorsConfiguration("/api/**", config); // 해당 주소로 들어오는 모든 요청은 config 필터를 통과해야함

        return new CorsFilter(source);
    }

}
