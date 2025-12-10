package com.semicolon.backend.global.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Paths;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://192.168.0.61:3000")
                .allowedMethods("HEAD","GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .maxAge(300)
                .allowedHeaders("*");
    }
    @Value("${com.semicolon.backend.upload}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String resourcePath = "file:"+uploadDir+"/";
        //file 접두사가 있으면 실제 디스크 경로를 의미
        registry.addResourceHandler("/upload/**") //프론트가 요청하는 URL
                //위 링크로 요청이 들어오면 서버의 실제 파일 경로에서 파일을 찾아 반환해주겠다
                .addResourceLocations(resourcePath);//서버의 실제 파일 경로
        registry.addResourceHandler("/download/**")
                .addResourceLocations(
                        "file:///C:/dev/upload/notice/",
                        "file:///C:/dev/upload/bank/",
                        "file:///C:/dev/upload/cert/",
                        "file:///C:/dev/upload/resume/"
                )
                .setCachePeriod(3600);
    }

    @Bean
    public Filter downloadHeaderFilter() {
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {

                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse res = (HttpServletResponse) response;

                String uri = req.getRequestURI();
                if (uri.startsWith("/download/")) {
                    String filename = Paths.get(uri).getFileName().toString();
                    res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                }

                chain.doFilter(request, response);
            }
        };
    }
}