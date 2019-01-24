package com.dell.sqspoc.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RedrivePolicy {

	private int maxReceiveCount;
	private String deadLetterTargetArn;

}
