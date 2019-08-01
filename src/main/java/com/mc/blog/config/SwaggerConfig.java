package com.mc.blog.config;


import com.mc.blog.domain.Usuario;
import com.mc.blog.security.JWTUtil;
import com.mc.blog.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UsuarioService usuarioService;


	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.mc.blog"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"RESTful API With Spring Boot 2.2.0",
				"Some description about your API.",
				"v1",
				"Terms Of Service Url",
				new Contact("MCM", "www.mcm.com.br", "your_email@gmail.com"),
				"License of API", "License of URL", Collections.emptyList());
	}


	@Bean
	public SecurityConfiguration security() {
		String token;
		try {
			Usuario userDetails = this.usuarioService.find(new Long(1));
			token = this.jwtUtil.generateToken(userDetails.getEmail());
		} catch (Exception e) {
			token = "";
		}

		return new SecurityConfiguration(null, null, null, null, "Bearer " + token, ApiKeyVehicle.HEADER,
				"Authorization", ",");
	}
}
