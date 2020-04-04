package com.reservation.services.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.reservation.services.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;


@Service
public class SendgridEmailServiceImpl implements EmailService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SendgridEmailServiceImpl.class);

	private final SendGrid sendGridClient;
	private final TemplateEngine templateEngine;

	public SendgridEmailServiceImpl(SendGrid sendGridClient, TemplateEngine templateEngine) {
		this.sendGridClient = sendGridClient;
		this.templateEngine = templateEngine;
	}


	@Override
	public void sendText(String from, String to, String subject, String body) {
		 Response response = sendEmail(from, to, subject, new Content("text/plain", body));
		 LOGGER.info("Status code: " + response.getStatusCode() + "\n" +
				 	 "Body: " + response.getBody() + 
				 	 "Headers: "+ response.getHeaders());
		 
	}

	@Override
	public void sendHtml(String from, String to, String subject, String body, String link) {
			Context context = new Context();		
			context.setVariable("message", body);
			context.setVariable("subject", subject);
			if(link != null) {
				context.setVariable("link", link);
			}
			
			String process = templateEngine.process("email-template", context);
			Response response = sendEmail(from, to, subject, new Content("text/html", process));
			LOGGER.info("Status code: " + response.getStatusCode() + 
					"Body: " + response.getBody() + 
					"Headers: " + response.getHeaders());
	}
	
	
	private Response sendEmail(String from, String to, String subject, Content content) {
		Mail mail = new Mail(new Email(from), subject, new Email(to), content);
		Request request = new Request();
		Response response = null;
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			response = this.sendGridClient.api(request);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
