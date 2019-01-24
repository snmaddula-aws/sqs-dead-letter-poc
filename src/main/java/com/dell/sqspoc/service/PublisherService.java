package com.dell.sqspoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

	@Autowired
	private QueueMessagingTemplate messagingTemplate;
	  
	public void send(String topicName, Object message) {
	    messagingTemplate.convertAndSend(topicName, message);
	}
}
