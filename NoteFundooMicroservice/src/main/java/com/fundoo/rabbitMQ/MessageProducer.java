package com.fundoo.rabbitMQ;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer 
{
	@Autowired
    private RabbitTemplate rabbitTemplate;
	 
	public void sendMessage(String emailId, String subject, String text) 
	{
		List<String> email = new ArrayList<String>();
		
		email.add(emailId);
		email.add(text);
		email.add(subject);
		
		rabbitTemplate.convertAndSend(RabbitMQProducerConfig.ROUTING_KEY, email);
     	System.out.println("Is listener returned ::: "+rabbitTemplate.isReturnListener());
	}
	
}
