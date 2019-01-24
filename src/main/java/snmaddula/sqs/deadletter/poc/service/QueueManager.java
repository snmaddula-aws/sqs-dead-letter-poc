package snmaddula.sqs.deadletter.poc.service;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import snmaddula.sqs.deadletter.poc.dto.QueueRequest;
import snmaddula.sqs.deadletter.poc.dto.QueueResponse;


/**
 * 
 * @author snmaddula
 *
 */
@Service
@AllArgsConstructor
public class QueueManager {

	private static final String ERR_QUEUE_ALREADY_EXISTS = "QueueAlreadyExists";

	private final AmazonSQS sqs;
	private final ObjectMapper mapper;
	
	
	public QueueResponse createQueue(QueueRequest request) throws JsonProcessingException {
		QueueResponse response = new QueueResponse();
		final String queueName = request.getQueueName();

		if (request.isDlEnabled()) {
			response = createWithDeadLetter(request, response);
		}else {
			create(queueName);
			response.setQueueArn(getArnByName(queueName));
		}

		return response;
	}
	
	
	
	/**
	 * Creates a queue with the provided "queueName".
	 * 
	 * @param queueName
	 * @return
	 */
	public String create(String queueName) {
		final CreateQueueRequest sourceQueue = new CreateQueueRequest(queueName);
		sourceQueue.addAttributesEntry("VisibilityTimeout", "5");
		String queueUrl = null;
		try {
			queueUrl = sqs.createQueue(sourceQueue).getQueueUrl();
		} catch (AmazonSQSException ex) {
			if (!ERR_QUEUE_ALREADY_EXISTS.equals(ex.getErrorCode())) {
				throw ex;
			}
		}
		return queueUrl == null ? sqs.getQueueUrl(queueName).getQueueUrl() : queueUrl;
	}

	/**
	 * 1. Creates two queues with the provided names "srcQueueName" and
	 * "dlQueueName". <br>
	 * 2. Attach the dlQueue to the srcQueue.
	 * 
	 * @param queueName
	 * @return
	 */
	public QueueResponse createWithDeadLetter(QueueRequest req, QueueResponse res) throws JsonProcessingException {
		String srcQueueUrl = create(req.getQueueName());
		
		create(req.getDlQueueName());
		String dlQueueArn = getArnByName(req.getDlQueueName());
		
		res.setQueueArn(getArnByName(req.getQueueName()));
		res.setDlQueueArn(dlQueueArn);
		
		req.getRedrivePolicy().setDeadLetterTargetArn(dlQueueArn);
		String redrivePolicyStr = mapper.writeValueAsString(req.getRedrivePolicy());
		SetQueueAttributesRequest request = new SetQueueAttributesRequest().withQueueUrl(srcQueueUrl)
				.addAttributesEntry("RedrivePolicy", redrivePolicyStr);

		sqs.setQueueAttributes(request);

		return res;
	}
	
	
	/**
	 * Gets the Amazon Resource Name (ARN) for the provided queueName.
	 * @param queueName
	 * @return
	 */
	public String getArnByName(String queueName) {
		GetQueueAttributesRequest getAttrReq = new GetQueueAttributesRequest(queueName).withAttributeNames("QueueArn");
		GetQueueAttributesResult queueAttrRes = sqs.getQueueAttributes(getAttrReq);
		return queueAttrRes.getAttributes().get("QueueArn");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	


	*//**
	 * Creates a queue with the provided "queueName".
	 * 
	 * @param queueName
	 * @return
	 *//*
	public CreateRes createQueue(CreateReq request) {
		final CreateRes response = new CreateRes();
		final String queueName = request.getQueueName();

		if (request.isDlEnabled()) {
			RedrivePolicy redrivePolicy = request.getRedrivePolicy();
			String dlQueueName = request.getDlQueueName();
			String dlQueueArn = redrivePolicy.getDeadLetterTargetArn();
			String dlQueueUrl = null;

			if (StringUtils.hasText(dlQueueArn)) {
				dlQueueUrl = sqs.getQueueUrl(dlQueueName).getQueueUrl();
			} else {
				dlQueueUrl = createQueue(dlQueueName, queueAttr, true);
				dlQueueArn = getArnByName(dlQueueName);
				redrivePolicy.setDeadLetterTargetArn(dlQueueArn);
			}

			response.setDlQueueUrl(dlQueueUrl);
			response.setDlQueueName(dlQueueName);
			response.setDlQueueArn(dlQueueArn);
			queueAttr.setRedrivePolicy(redrivePolicy);
		}

		String queueUrl = null;
		try {
			queueUrl = createQueue(queueName, queueAttr.getAttributeMap());
		} catch (AmazonSQSException ex) {
			if (!ERR_QUEUE_ALREADY_EXISTS.equals(ex.getErrorCode())) {
				throw ex;
			}
			queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		}

		response.setQueueName(queueName);
		response.setQueueUrl(queueUrl);
		response.setQueueArn(getArnByName(queueName));
		return response;
	}

	private String createQueue(String queueName, QueueAttributes queueAttr, boolean isDL) {
		String queueUrl = null;
		try {
			if (isDL) {
				Map<String, String> attributes = new HashMap<>(queueAttr.getAttributeMap());
				attributes.remove(SQSAttributeKeys.REDRIVE_POLICY);
				queueUrl = createQueue(queueName, attributes);
			} else {
				queueUrl = sqs.createQueue(queueName).getQueueUrl();
			}
		} catch (AmazonSQSException ex) {
			if (!ERR_QUEUE_ALREADY_EXISTS.equals(ex.getErrorCode())) {
				throw ex;
			}
		}
		return queueUrl;
	}
	
	private String createQueue(String queueName, Map<String, String> attributes) {
		CreateQueueRequest createNewQueueReq = new CreateQueueRequest(queueName).withAttributes(attributes);
		return  sqs.createQueue(createNewQueueReq).getQueueUrl();
	}

	private String getArnByName(String queueName) {
		GetQueueAttributesRequest getAttrReq = new GetQueueAttributesRequest(queueName).withAttributeNames("QueueArn");
		GetQueueAttributesResult queueAttrRes = sqs.getQueueAttributes(getAttrReq);
		return queueAttrRes.getAttributes().get("QueueArn");
	}*/
}
