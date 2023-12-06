package com.marketplace.springboot.Swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Marketplace API REST", version = "1.0"))
public class SwaggerConfig {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.marketplace.springboot"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo metaInfo() {
        return new ApiInfoBuilder()
                .title("Marketplace API REST")
                .description("API REST for managing marketplace products.")
                .version("1.0")
                .contact(new Contact("Lucas Sousa", "https://github.com/SrSousa011", "lucas.sousa_99@hotmail.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}
