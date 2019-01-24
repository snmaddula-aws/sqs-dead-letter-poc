package snmaddula.sqs.deadletter.poc.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author snmaddula
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RedrivePolicy {

	private int maxReceiveCount;
	private String deadLetterTargetArn;

}
