package com.dell.sqspoc.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(NON_EMPTY)
public class CreateRes {

	private String queueName;
	private String queueUrl;
	private String queueArn;
	private Boolean dlEnabled;
	private String dlQueueName;
	private String dlQueueUrl;
	private String dlQueueArn;
	
}
