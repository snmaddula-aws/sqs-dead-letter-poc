package com.dell.sqspoc.util;

import com.dell.sqspoc.domain.CreateReq;
import com.dell.sqspoc.domain.QueueAttributes;
import com.dell.sqspoc.service.RedrivePolicy;

public class _ {

	
	public static void main(String[] args) {
		
		CreateReq cr = new CreateReq();
		cr.setQueueName("alpha");
		cr.setDlQueueName("alpha-helper");
		
		QueueAttributes qa = new QueueAttributes();
		qa.setDelaySeconds(60);
		qa.setFifoQueue(true);
		qa.setContentBasedDeduplication(true);
		qa.setMessageRetentionPeriod(86400);
		RedrivePolicy redrivePolicy = new RedrivePolicy();
		redrivePolicy.setMaxReceiveCount(2);
		qa.setRedrivePolicy(redrivePolicy);
		
		cr.setQueueAttributes(qa);
		
		String json = MappingUtil.toJson(cr);
		System.out.println(json);
	}
}
