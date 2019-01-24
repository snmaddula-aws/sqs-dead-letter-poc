package snmaddula.sqs.deadletter.poc.dto;

import lombok.Getter;
import lombok.Setter;
import snmaddula.sqs.deadletter.poc.domain.DivisionArithmeticReq;

/**
 * 
 * @author snmaddula
 *
 */
@Setter
@Getter
public class PublishRequest {

	private DivisionArithmeticReq payload;
	private String queueName;
}
