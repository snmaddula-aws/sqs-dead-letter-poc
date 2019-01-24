package snmaddula.sqs.deadletter.poc.service;

import static snmaddula.sqs.deadletter.poc.util.MappingUtil.toJson;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import lombok.AllArgsConstructor;
import snmaddula.sqs.deadletter.poc.dto.PublishRequest;

/**
 * Message Producer service
 * 
 * @author snmaddula
 *
 */
@Service
@AllArgsConstructor
public class MessageProducer {

	private final AmazonSQS sqs;
	
	public String publish(PublishRequest req) {
		String queueUrl = sqs.getQueueUrl(req.getQueueName()).getQueueUrl();
		SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, toJson(req.getPayload()));
		SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
		return sendMessageResult.getMessageId();
	}
}
