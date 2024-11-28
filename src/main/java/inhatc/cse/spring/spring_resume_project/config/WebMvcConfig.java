package inhatc.cse.spring.spring_resume_project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value(value = "${uploadPath}") //경로를 그냥 쓰면 다 보이기 때문에 바꿔주는것임
    private String uploadPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebMvcConfigurer.super.addResourceHandlers(registry); 날려버림
        registry.addResourceHandler("/files/**")//웹 상에서는 images 에다가 넣는 것으로 되지만
                .addResourceLocations(uploadPath); //실제로는 업로드 경로에 넣어주는 것임
    }
}