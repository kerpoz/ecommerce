package top.kerpoz.ecom_proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Wszystkie endpointy w /api/
                        .allowedOrigins("http://localhost:5173") // Adres frontendu
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Dozwolone metody HTTP
                        .allowedHeaders("*") // Pozwala na wszystkie nagłówki
                        .allowCredentials(true); // Pozwala na uwierzytelnianie
            }
        };
    }
}
