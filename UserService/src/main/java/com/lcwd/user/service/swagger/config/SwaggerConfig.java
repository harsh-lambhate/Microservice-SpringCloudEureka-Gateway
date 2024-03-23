package com.lcwd.user.service.swagger.config;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	
	
	 @Bean
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2).select()
	                .apis(RequestHandlerSelectors.basePackage("com.lcwd.user.service"))
	                .build()
	                .apiInfo(metaInfo());
	    }
	 
	
	private ApiInfo metaInfo() {

		ApiInfo apiInfo = new ApiInfo("User Service", "REST API's for User Service.", "1.0", "Terms of Service",
				new Contact("Data Template", "https://www.datatemplate.com/", " "), "Apache License Version 2.0",
				"https://www.datatemplate.com/", new ArrayList<VendorExtension>());

		return apiInfo;
	}
	 
	
	@Bean
	  public OpenAPI myOpenAPI() {
	  
		 Server devServer = new Server();
		    devServer.setUrl("devUrl");
		    devServer.setDescription("Server URL in Development environment");

		    Server prodServer = new Server();
		    prodServer.setUrl("prodUrl");
		    prodServer.setDescription("Server URL in Production environment");

	    License mitLicense = new License().name("MIT License").url("https://www.datatemplate.com/");

	    Info info = new Info()
	        .title("User Service")
	        .version("1.0")
	        .description("Data Template Services").termsOfService("https://www.datatemplate.com/")
	        .license(mitLicense);

	    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
	  }
	    
	    /**
	     * This bean is used to resolve the swagger issue of (excep -> PatternsRequestCondition.getPatterns() is getting null)
	     * @param webEndpointsSupplier
	     * @param servletEndpointsSupplier
	     * @param controllerEndpointsSupplier
	     * @param endpointMediaTypes
	     * @param corsProperties
	     * @param webEndpointProperties
	     * @param environment
	     * @return
	     */
		@Bean
		public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
				ServletEndpointsSupplier servletEndpointsSupplier,
				ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
				CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties,
				Environment environment) {
			List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
			Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
			allEndpoints.addAll(webEndpoints);
			allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
			allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
			String basePath = webEndpointProperties.getBasePath();
			EndpointMapping endpointMapping = new EndpointMapping(basePath);
			boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment,
					basePath);
			return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
					corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
					shouldRegisterLinksMapping, null);
		}

		private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment,
				String basePath) {
			return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
					|| ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
		}

}
