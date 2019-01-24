package com.dell.sqspoc.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateReq {

	private String queueName;
	private String dlQueueName;
	private QueueAttributes queueAttributes;
	
}
