package com.sas.dhop.site.dto.request;

public record ConfirmPaymentRequest(String webhookURL) {

	public ConfirmPaymentRequest(String webhookURL) {
		this.webhookURL = webhookURL;
	}
}
