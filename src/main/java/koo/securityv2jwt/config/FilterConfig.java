package koo.securityv2jwt.config;

import koo.securityv2jwt.filter.MyFilter1;
import koo.securityv2jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 커스텀 필터 적용
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());

        bean.addUrlPatterns("/*"); // 모든 요청에 적용
        bean.setOrder(0); // 우선순위 제일 높음

        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());

        bean.addUrlPatterns("/*"); // 모든 요청에 적용
        bean.setOrder(0); // 우선순위 제일 높음

        return bean;
    }

}
