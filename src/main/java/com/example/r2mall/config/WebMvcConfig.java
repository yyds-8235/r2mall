package com.example.r2mall.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.example.r2mall.interceptor.CorsInterceptor;
import com.example.r2mall.interceptor.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

        private final RequestLoggingInterceptor requestLoggingInterceptor;

        private final CorsInterceptor corsInterceptor;

        /**
         * 注册拦截器
         */
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                // 跨域拦截器 - 优先级最高
                registry.addInterceptor(corsInterceptor)
                                .addPathPatterns("/**")
                                .order(1);

                // 请求日志拦截器
                registry.addInterceptor(requestLoggingInterceptor)
                                .addPathPatterns("/**")
                                .excludePathPatterns(
                                                "/swagger-ui/**", // Swagger UI
                                                "/v3/api-docs/**", // API文档
                                                "/swagger-ui.html", // Swagger UI
                                                "/error", // 错误页面
                                                "/favicon.ico" // 网站图标
                                )
                                .order(2);

                // Sa-Token认证拦截器
                registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                                .addPathPatterns("/api/**") // 需要登录认证的路径
                                .excludePathPatterns(
                                                "/api/auth/login", // 登录接口
                                                "/api/auth/user/register", // 用户注册
                                                "/api/auth/merchant/register", // 商家入驻
                                                "/api/user/products/**", // 浏览商品（无需登录）
                                                "/im/**", // WebSocket连接
                                                "/swagger-ui/**", // Swagger UI
                                                "/v3/api-docs/**", // API文档
                                                "/swagger-ui.html", // Swagger UI
                                                "/doc.html", // Knife4j文档
                                                "/error", // 错误页面
                                                "/favicon.ico", // 网站图标
                                                "/index.html", // 首页
                                                "/", // 首页
                                                "/static/**" // 静态资源
                                )
                                .order(3);
        }

        /**
         * 配置跨域
         */
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                                .allowedOriginPatterns("*")
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                .allowedHeaders("*")
                                .allowCredentials(true)
                                .maxAge(3600);
        }

        /**
         * 配置静态资源处理
         */
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Swagger UI 静态资源
                registry.addResourceHandler("/swagger-ui/**")
                                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");

                // 其他静态资源
                registry.addResourceHandler("/static/**")
                                .addResourceLocations("classpath:/static/");
        }
}
