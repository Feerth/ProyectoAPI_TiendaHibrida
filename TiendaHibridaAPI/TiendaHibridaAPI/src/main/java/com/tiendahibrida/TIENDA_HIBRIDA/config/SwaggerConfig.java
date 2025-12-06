package com.tiendahibrida.TIENDA_HIBRIDA.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI tiendaHibridaAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Tienda Híbrida")
                        .description("Documentación de la API del proyecto POO")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Fernando y Sebastian")
                                .email("soporte@tiendahibrida.com")
                        )
                );
    }
}
