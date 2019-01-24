package snmaddula.sqs.deadletter.poc.service;

import static snmaddula.sqs.deadletter.poc.util.MappingUtil.toObject;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import lombok.AllArgsConstructor;
import snmaddula.sqs.deadletter.poc.domain.DivisionArithmeticRes;
import snmaddula.sqs.deadletter.poc.dto.ConsumeRequest;

/**
 * 
 * @author snmaddula
 *
 */
@Service
@AllArgsConstructor
public class MessageConsumer {
	
	private final AmazonSQS sqs;

	public DivisionArithmeticRes consume(ConsumeRequest consumeReq) {
		DivisionArithmeticRes result = null;
		final String queueUrl = sqs.getQueueUrl(consumeReq.getQueueName()).getQueueUrl();
		final ReceiveMessageRequest rq = new ReceiveMessageRequest(queueUrl);
		List<Message> messages = sqs.receiveMessage(rq).getMessages();
		if(!messages.isEmpty()) {
			try {
				String payload = messages.get(0).getBody();
				result = toObject(payload, DivisionArithmeticRes.class);
				int quotient =  result.getDividend() / result.getDivisor();
				result.setQuotient(quotient);
				sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messages.get(0).getReceiptHandle()));
			}catch(Exception ex) {
				System.err.println("FAILED TO PROCESS MESSAGE : REASON [ " + ex.getMessage() + " ]");
			}
		}
		return result;
	}
	
}
