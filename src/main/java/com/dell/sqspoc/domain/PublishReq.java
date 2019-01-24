package com.dell.sqspoc.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PublishReq {

	private String payload;
	private String queueName;
	private Integer times;
}
