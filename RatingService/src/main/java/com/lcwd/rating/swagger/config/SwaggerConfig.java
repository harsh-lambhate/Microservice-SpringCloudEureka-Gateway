package com.lcwd.rating.swagger.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;

@Configuration
public class SwaggerConfig {
	
	 @Bean
	    public Docket atividadeApi() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.lcwd.rating"))
	                .paths(regex("/ratings.*"))
	                .build()
	                .apiInfo(metaInfo());
	    }

	    private ApiInfo metaInfo() {

	        ApiInfo apiInfo = new ApiInfo(
	                "Rating Service",
	                "REST API's for Rating Service.",
	                "1.0",
	                "Terms of Service",
	                new Contact("Data Template", "https://www.datatemplate.com/",""),
	                "Apache License Version 2.0",
	                "https://www.datatemplate.com/", new ArrayList<VendorExtension>()
	        );

	        return apiInfo;
	    }

}
