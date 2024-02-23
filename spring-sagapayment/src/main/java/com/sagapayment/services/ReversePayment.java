package com.sagapayment.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.baseevents.events.OrderEvent;
import com.baseevents.events.PaymentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapayment.entities.Payment;
import com.sagapayment.repos.PaymentRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReversePayment {
	@Autowired
	private PaymentRepo paymentRepo;
	
	@KafkaListener(topics = "reverse-payment",groupId = "reversepay-group")
	public void reverseOrder(String msg) {
		
		log.info("Consuming Reverse Order event= "+msg);
		
		try {
			PaymentEvent event = new ObjectMapper().readValue(msg, PaymentEvent.class);
			//OrderEvent orderEvent=new ObjectMapper().readValue(event, OrderEvent.class);
		
		List<Payment> items=	 paymentRepo.findByOrderId(event.getOrder().getOrderId());
		items.stream().forEach(item->{
			item.setStatus("Failed");
			
			paymentRepo.save(item);
		});
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Exception occured while listening Reverse Order= "+e.getMessage());
		}
		
	}
}
