package com.sagapayment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import com.baseevents.dto.CustomerOrder;
import com.baseevents.events.OrderEvent;
import com.baseevents.events.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapayment.entities.Payment;

import com.sagapayment.repos.PaymentRepo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PaymentController {
	@Autowired
	private PaymentRepo paymentRepo;
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaOrder;
	
	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaPaymt;
	
	@KafkaListener(topics = "saga-orders",groupId = "payment-group")
	public void processPayment(String message) throws JsonMappingException, JsonProcessingException {
		OrderEvent event = new ObjectMapper().readValue(message, OrderEvent.class);
		log.info("Payment Service recevided Order event for payment processing..."+event.getCustomerOrder()+" evnt Id= "+event.getType());
	
		CustomerOrder customerOrder=event.getCustomerOrder();
		Payment payment=new Payment();
		try {
		payment.setAmount(customerOrder.getAmt());
		payment.setMode(customerOrder.getPaymentMethod());
		payment.setOrderId(customerOrder.getOrderId());
		payment.setStatus("Sucess");
		
		
		payment=paymentRepo.save(payment);
		
		customerOrder.setPymtStatus(payment.getStatus());
		PaymentEvent paymentEvent=new PaymentEvent();
		paymentEvent.setOrder(customerOrder);
		paymentEvent.setType("Payment Sucessfull");
		Message<PaymentEvent> paymessage=MessageBuilder.withPayload(paymentEvent).setHeader(KafkaHeaders.TOPIC, "payment-order").build();
		//throw new Exception("testing negative");
		kafkaPaymt.send(paymessage);
		}catch (Exception e) {
			// TODO: handle exception
			
			/*
			 * payment.setOrderId(customerOrder.getOrderId()); payment.setStatus("Failed");
			 * 
			 * paymentRepo.save(payment);
			 */
			customerOrder.setPymtStatus("Failed");
			PaymentEvent paymentEvent=new PaymentEvent();
			paymentEvent.setOrder(customerOrder);
			paymentEvent.setType("Payment Failed");
			Message<PaymentEvent> paymessage=MessageBuilder.withPayload(paymentEvent).setHeader(KafkaHeaders.TOPIC, "reverse-payment").build();
			kafkaPaymt.send(paymessage);
			OrderEvent orderEvent=new OrderEvent();
			orderEvent.setCustomerOrder(customerOrder);
			orderEvent.setType("Order reversed..");
			Message<OrderEvent> mesg=MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, "reverse-order").build();
			kafkaOrder.send(mesg);
		}
	}

}
