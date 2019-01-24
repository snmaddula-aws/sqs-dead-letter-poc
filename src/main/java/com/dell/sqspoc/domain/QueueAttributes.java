package com.dell.sqspoc.domain;

import static com.dell.sqspoc.domain.SQSAttributeKeys.CONTENT_BASED_DEDUPLICATION;
import static com.dell.sqspoc.domain.SQSAttributeKeys.DELAY_SECONDS;
import static com.dell.sqspoc.domain.SQSAttributeKeys.FIFO_QUEUE;
import static com.dell.sqspoc.domain.SQSAttributeKeys.MESSAGE_RETENTION_PERIOD;
import static com.dell.sqspoc.domain.SQSAttributeKeys.REDRIVE_POLICY;
import static com.dell.sqspoc.util.MappingUtil.toJson;

import java.util.HashMap;
import java.util.Map;

import com.dell.sqspoc.service.RedrivePolicy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class QueueAttributes {

	@JsonProperty("FifoQueue")
	private boolean fifoQueue;

	@JsonProperty("ContentBasedDeduplication")
	private boolean contentBasedDeduplication;

	@JsonProperty("DelaySeconds")
	private int delaySeconds;

	@JsonProperty("MessageRetentionPeriod")
	private long messageRetentionPeriod;
	
	@JsonProperty("RedrivePolicy")
	private RedrivePolicy redrivePolicy;

	@JsonIgnore
	private Map<String, String> attributeMap;
	
	@JsonIgnore
	private boolean deadLetterEnabled;

	public QueueAttributes() {
		attributeMap = new HashMap<>();
	}

	public void setFifoQueue(boolean fifoQueue) {
		this.fifoQueue = fifoQueue;
		attributeMap.put(FIFO_QUEUE, String.valueOf(fifoQueue));
	}

	public void setContentBasedDeduplication(boolean contentBasedDeduplication) {
		this.contentBasedDeduplication = contentBasedDeduplication;
		attributeMap.put(CONTENT_BASED_DEDUPLICATION, String.valueOf(contentBasedDeduplication));
	}

	public void setDelaySeconds(int delaySeconds) {
		this.delaySeconds = delaySeconds;
		attributeMap.put(DELAY_SECONDS, String.valueOf(delaySeconds));
	}

	public void setMessageRetentionPeriod(long messageRetentionPeriod) {
		this.messageRetentionPeriod = messageRetentionPeriod;
		attributeMap.put(MESSAGE_RETENTION_PERIOD, String.valueOf(messageRetentionPeriod));
	}

	public void setRedrivePolicy(RedrivePolicy redrivePolicy) {
		this.redrivePolicy = redrivePolicy;
		attributeMap.put(REDRIVE_POLICY, toJson(redrivePolicy));
		deadLetterEnabled = true;
	}
	
	
	

}
