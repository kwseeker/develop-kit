package top.kwseeker.developkit.calltrace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kwseeker.developkit.calltrace.async.MdcThreadPoolExecutor;
import top.kwseeker.developkit.calltrace.interceptor.LogInterceptor;


import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(logInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public MdcThreadPoolExecutor getMdcThreadPoolExecutor() {
        return new MdcThreadPoolExecutor(3, 200, 5*60, TimeUnit.SECONDS);
    }

}
