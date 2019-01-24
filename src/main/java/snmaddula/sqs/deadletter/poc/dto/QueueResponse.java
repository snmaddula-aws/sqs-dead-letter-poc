package snmaddula.sqs.deadletter.poc.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author snmaddula
 *
 */
@Setter
@Getter
@JsonInclude(NON_EMPTY)
public class QueueResponse {

	private String queueName;
	private String queueUrl;
	private String queueArn;
	private Boolean dlEnabled;
	private String dlQueueName;
	private String dlQueueUrl;
	private String dlQueueArn;
	
}
