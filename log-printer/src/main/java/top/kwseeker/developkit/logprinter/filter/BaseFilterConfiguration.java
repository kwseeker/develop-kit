package top.kwseeker.developkit.logprinter.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class BaseFilterConfiguration {

    @Bean
    public FilterRegistrationBean requestLogFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestLogFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter requestLogFilter() {
        return new LogFilter();
    }
}
