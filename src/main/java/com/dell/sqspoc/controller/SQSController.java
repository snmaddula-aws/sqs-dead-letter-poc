package com.dell.sqspoc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dell.sqspoc.domain.CreateReq;
import com.dell.sqspoc.domain.CreateRes;
import com.dell.sqspoc.domain.PublishReq;
import com.dell.sqspoc.service.SimpleQueueService;

@RestController
@RequestMapping("/sqs")
public class SQSController {

	private final SimpleQueueService service;
	
	public SQSController(SimpleQueueService service) {
		this.service = service;
	}
	
	@PostMapping("/create")
	public CreateRes createQueue(@RequestBody CreateReq createReq) {
		return service.createQueue(createReq);
	}
	
	@PostMapping("/publish/bulk")
	public String bulpPublish(@RequestBody PublishReq publishReq) {
		return service.bulkPublish(publishReq);
	}
	
	@GetMapping("/consume")
	public void consume() {
		service.consume("alpha.fifo");
	}
}
