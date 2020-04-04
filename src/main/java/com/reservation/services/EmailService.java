package com.reservation.services;

public interface EmailService {
	
	void sendText(String from, String to, String subject, String body);
	
	void sendHtml(String from, String to, String subject, String body, String link);
}

