package edtech.afrilingo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AfriLingo API")
                        .version("1.0.0")
                        .description("REST API for African Language Learning Platform")
                        .contact(new Contact()
                                .name("AfriLingo Support")
                                .email("support@afrilingo.com")
                                .url("https://afrilingo.com/support")))
                .components(new Components()
                        .addSecuritySchemes("bearer-auth", 
                            new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authorization header using the Bearer scheme")));
    }
}