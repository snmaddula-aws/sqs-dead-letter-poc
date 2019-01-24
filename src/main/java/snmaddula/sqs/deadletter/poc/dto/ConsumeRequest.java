package snmaddula.sqs.deadletter.poc.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author snmaddula
 *
 */
@Setter
@Getter
public class ConsumeRequest {

	private Integer numOfMessages;
	private String queueName;
}
