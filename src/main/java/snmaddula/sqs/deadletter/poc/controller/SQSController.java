package snmaddula.sqs.deadletter.poc.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import snmaddula.sqs.deadletter.poc.domain.DivisionArithmeticRes;
import snmaddula.sqs.deadletter.poc.dto.ConsumeRequest;
import snmaddula.sqs.deadletter.poc.dto.QueueRequest;
import snmaddula.sqs.deadletter.poc.dto.QueueResponse;
import snmaddula.sqs.deadletter.poc.dto.PublishRequest;
import snmaddula.sqs.deadletter.poc.service.MessageConsumer;
import snmaddula.sqs.deadletter.poc.service.MessageProducer;
import snmaddula.sqs.deadletter.poc.service.QueueManager;

/**
 * 
 * @author snmaddula
 *
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sqs")
public class SQSController {

	private final QueueManager queueManager;
	private final MessageProducer producer;
	private final MessageConsumer consumer;
	
	@PostMapping("/create")
	public QueueResponse createQueue(@RequestBody QueueRequest createReq) throws JsonProcessingException {
		return queueManager.createQueue(createReq);
	}
	
	@PostMapping("/publish")
	public String publish(@RequestBody PublishRequest publishReq) {
		return producer.publish(publishReq);
	}
	
	@PostMapping("/consume")
	public DivisionArithmeticRes consume(@RequestBody ConsumeRequest consumeReq) {
		return consumer.consume(consumeReq);
	}
}
