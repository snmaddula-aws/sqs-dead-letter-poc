package snmaddula.sqs.deadletter.poc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author snmaddula
 *
 */
@Configuration
public class AppConfig {

	@Bean
	public AmazonSQS sqs(@Value("${cloud.aws.profile}") String profile) {
		ProfileCredentialsProvider credentials = new ProfileCredentialsProvider(profile);
		return AmazonSQSClientBuilder
				.standard()
				.withCredentials(credentials)
				.build();
	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}
}
