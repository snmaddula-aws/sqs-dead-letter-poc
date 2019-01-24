package snmaddula.sqs.deadletter.poc.config;

import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Setter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author snmaddula
 *
 */
@Setter
@Controller
@Configuration
@EnableSwagger2
@ConfigurationProperties("swagger")
public class SwaggerConfig {

	private String title;
	private String description;
	private String version;
	private String contactName;
	private String contactUrl;
	private String contactEmail;
	
	@GetMapping("/")
	public String swaggerPage() {
	    return "redirect:swagger-ui.html";
	}
	
	@Bean
	public Docket swaggerApi() {
		return new Docket(SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(withClassAnnotation(RestController.class))
				.build()
				.useDefaultResponseMessages(false);
	}
		
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(title)
				.description(description)
				.version(version)
				.contact(new Contact(contactName, contactUrl, contactEmail))
				.build();
	}
	
	
}