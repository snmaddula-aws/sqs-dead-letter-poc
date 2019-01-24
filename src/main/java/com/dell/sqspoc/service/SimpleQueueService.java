package com.dell.sqspoc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.dell.sqspoc.domain.CreateReq;
import com.dell.sqspoc.domain.CreateRes;
import com.dell.sqspoc.domain.PublishReq;
import com.dell.sqspoc.domain.QueueAttributes;
import com.dell.sqspoc.domain.SQSAttributeKeys;

@Service
public class SimpleQueueService {

	private static final String ERR_QUEUE_ALREADY_EXISTS = "QueueAlreadyExists";

	private AmazonSQS sqs;
	private ProfileCredentialsProvider awsCredentials;

	@Value("${cloud.aws.credentials.profile:default}")
	private String awsProfile;

	@PostConstruct
	public void init() {
		awsCredentials = new ProfileCredentialsProvider(awsProfile);
		sqs = AmazonSQSClientBuilder.standard().withCredentials(awsCredentials).build();
	}

	/**
	 * Creates a queue with the provided "queueName".
	 * 
	 * @param queueName
	 * @return
	 */
	public CreateRes createQueue(CreateReq req) {
		final CreateRes res = new CreateRes();
		final String queueName = req.getQueueName();
		final QueueAttributes queueAttr = req.getQueueAttributes();

		if (queueAttr.isDeadLetterEnabled()) {
			RedrivePolicy redrivePolicy = queueAttr.getRedrivePolicy();
			String dlQueueName = req.getDlQueueName();
			String dlQueueArn = redrivePolicy.getDeadLetterTargetArn();
			String dlQueueUrl = null;

			if (StringUtils.hasText(dlQueueArn)) {
				dlQueueUrl = sqs.getQueueUrl(dlQueueName).getQueueUrl();
			} else {
				dlQueueUrl = createQueue(dlQueueName, queueAttr, true);
				dlQueueArn = getArnByName(dlQueueName);
				redrivePolicy.setDeadLetterTargetArn(dlQueueArn);
			}

			res.setDlQueueUrl(dlQueueUrl);
			res.setDlQueueName(dlQueueName);
			res.setDlQueueArn(dlQueueArn);
			queueAttr.setRedrivePolicy(redrivePolicy);
		}

		String queueUrl = null;
		try {
			CreateQueueRequest createNewQueueReq = new CreateQueueRequest(queueName)
					.withAttributes(queueAttr.getAttributeMap());
			queueUrl = sqs.createQueue(createNewQueueReq).getQueueUrl();
		} catch (AmazonSQSException ex) {
			if (!ERR_QUEUE_ALREADY_EXISTS.equals(ex.getErrorCode())) {
				throw ex;
			}
			queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		}

		res.setQueueName(queueName);
		res.setQueueUrl(queueUrl);
		res.setQueueArn(getArnByName(queueName));
		return res;
	}

	private String createQueue(String queueName, QueueAttributes queueAttr, boolean isDL) {
		String queueUrl = null;
		try {
			if (isDL) {
				Map<String, String> attributes = new HashMap<>(queueAttr.getAttributeMap());
				attributes.remove(SQSAttributeKeys.REDRIVE_POLICY);
				CreateQueueRequest createNewQueueReq = new CreateQueueRequest(queueName).withAttributes(attributes);

				queueUrl = sqs.createQueue(createNewQueueReq).getQueueUrl();
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

	private String getArnByName(String queueName) {
		GetQueueAttributesRequest getAttrReq = new GetQueueAttributesRequest(queueName).withAttributeNames("QueueArn");
		GetQueueAttributesResult queueAttrRes = sqs.getQueueAttributes(getAttrReq);
		return queueAttrRes.getAttributes().get("QueueArn");
	}

	public String bulkPublish(PublishReq publishReq) {
		String payload = publishReq.getPayload();
		String queueUrl = sqs.getQueueUrl(publishReq.getQueueName()).getQueueUrl();

		ExecutorService es = Executors.newFixedThreadPool(60);

		List<Callable<Integer>> callables = new ArrayList<>();
		while (callables.size() <= 900) {
			callables.add(() -> {
				final SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl,
						payload + "_" + System.currentTimeMillis() + callables.size());
				sendMessageRequest.setMessageGroupId("messageGroup1");
				SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
				String sequenceNumber = sendMessageResult.getSequenceNumber();
				System.out.println("Message published with Seq. Id: " + sequenceNumber);
				return 0;
			});
		}
		try {
			es.invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "DONE";
	}

	public void consume(String queueName) {
		while(true) {
			final ReceiveMessageRequest rq = new ReceiveMessageRequest(sqs.getQueueUrl(queueName).getQueueUrl());
			rq.setMaxNumberOfMessages(10);
			rq.setWaitTimeSeconds(1);
			List<Message> messages = sqs.receiveMessage(rq).getMessages();
			for (Message m : messages) {
				System.out.println(m.getBody());
			}
		}
	}

}
