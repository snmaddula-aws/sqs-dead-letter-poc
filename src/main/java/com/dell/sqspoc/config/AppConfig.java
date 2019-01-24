package com.dell.sqspoc.config;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.sqs.AmazonSQSAsync;

@Configuration
public class AppConfig {

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
	    return new QueueMessagingTemplate(amazonSQSAsync);
	}
	
}
