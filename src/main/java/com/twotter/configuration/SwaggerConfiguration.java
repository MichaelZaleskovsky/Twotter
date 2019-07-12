package com.twotter.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.google.common.base.Predicates;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@SuppressWarnings("deprecation")
@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurerAdapter
{
    @Bean
    public Docket api() {
        // @formatter:off
        //Register the controllers to swagger and configuring the Swagger Docket
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build()
                .genericModelSubstitutes(ResponseEntity.class)
                .apiInfo(metaInfo())
                .useDefaultResponseMessages(false);
        // @formatter:on
    }
    
    
    private ApiInfo metaInfo() {
    	return new ApiInfo(
    			"Open API documentation for project TWOTTER.COM",
    			"Code chalenge task for HSBC",
    			"",
    			"",
    			new Contact("Mikhail Zaleskovskiy", "https://github.com/MichaelZaleskovsky/Twotter.git", "mz.java.developer@gmail.com"),
    			"",
    			""
    			);
    }
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}