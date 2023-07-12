package koo.securityv2jwt.config;

import koo.securityv2jwt.filter.MyFilter1;
import koo.securityv2jwt.filter.MyFilter2;
import koo.securityv2jwt.filter.MyFilter3;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 커스텀 필터 적용
 * 스프링 시큐리티의 필터체인의 필터들이 먼저 실행되고 커스텀 필터는 그 이후에 실행된다.
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());

        bean.addUrlPatterns("/*"); // 모든 요청에 적용
        bean.setOrder(0); // 우선순위 제일 높음(가장 먼저 실행됨)

        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter3> filter3() {
        FilterRegistrationBean<MyFilter3> bean = new FilterRegistrationBean<>(new MyFilter3());

        bean.addUrlPatterns("/*"); // 모든 요청에 적용
        bean.setOrder(1);

        return bean;
    }

}
