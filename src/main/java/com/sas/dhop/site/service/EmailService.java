package com.sas.dhop.site.service;

import jakarta.mail.MessagingException;

import java.util.concurrent.CompletableFuture;

public interface EmailService {

    CompletableFuture<Void> sendEmail(String to, String subject, String text) throws MessagingException;

}
