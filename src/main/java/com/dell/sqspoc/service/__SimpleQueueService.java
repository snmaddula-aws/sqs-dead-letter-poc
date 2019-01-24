package com.dell.sqspoc.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Service
public class __SimpleQueueService {

	private static final String ERR_QUEUE_ALREADY_EXISTS = "QueueAlreadyExists";

	private AmazonSQS sqs;
	private ObjectMapper mapper;
	private ProfileCredentialsProvider awsCredentials;

	@Value("${cloud.aws.credentials.profile:default}")
	private String awsProfile;

	@PostConstruct
	public void init() {
		awsCredentials = new ProfileCredentialsProvider(awsProfile);
		sqs = AmazonSQSClientBuilder.standard().withCredentials(awsCredentials).build();
		mapper = new ObjectMapper();
	}

	/**
	 * Creates a queue with the provided "queueName".
	 * 
	 * @param queueName
	 * @return
	 */
	public String create(String queueName) {
		final CreateQueueRequest sourceQueue = new CreateQueueRequest(queueName);
		String queueUrl = null;
		try {
			queueUrl = sqs.createQueue(sourceQueue).getQueueUrl();
		    CreateQueueRequest create_request = new CreateQueueRequest(queueName)
		    		.addAttributesEntry("DelaySeconds", "60")
		    		.addAttributesEntry("MessageRetentionPeriod", "86400");

			
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
	public String createWithDeadLetter(String srcQueueName, String dlQueueName, int maxReceiveCount)
			throws JsonProcessingException {
		String srcQueueUrl = create(srcQueueName);
		
		String dlQueueUrl = create(dlQueueName);
		String dlQueueArn = getArnByName(dlQueueUrl);

		RedrivePolicy redrivePolicy = null;// new RedrivePolicy(maxReceiveCount, dlQueueArn);
		String redrivePolicyStr = mapper.writeValueAsString(redrivePolicy);
		SetQueueAttributesRequest request = new SetQueueAttributesRequest().withQueueUrl(srcQueueUrl)
				.addAttributesEntry("RedrivePolicy", redrivePolicyStr);

		sqs.setQueueAttributes(request);

		return "DONE";
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
}
