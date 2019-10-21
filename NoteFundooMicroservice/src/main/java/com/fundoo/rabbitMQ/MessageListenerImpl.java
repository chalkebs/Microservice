package com.fundoo.rabbitMQ;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerImpl
{
	private JavaMailSender javaMailSender;
	
	public MessageListenerImpl(JavaMailSender javaMailSender) 
	{
		this.javaMailSender = javaMailSender;
    }
	
	public void onMessage(List<String> list) 
	{
		System.out.println("Message Received : ...Consumer side Listening...");
				
		MimeMessage message1 = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message1);
		
		try 
		{
			helper.setFrom("bhagyashrichalke21@gmail.com");
		    helper.setTo(list.get(0));
		    helper.setText(list.get(1));
		    helper.setSubject(list.get(2));
		} 
		catch (MessagingException e) 
		{
		    e.printStackTrace();
		}
		javaMailSender.send(message1);
	  
	 }	
}
