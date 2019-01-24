package snmaddula.sqs.deadletter.poc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import snmaddula.sqs.deadletter.poc.service.RedrivePolicy;

/**
 * 
 * @author snmaddula
 *
 */
@Setter
@Getter
public class QueueRequest {

	private String queueName;
	private String dlQueueName;
	private boolean dlEnabled;
	
	@JsonProperty("RedrivePolicy")
	private RedrivePolicy redrivePolicy;
	
}
