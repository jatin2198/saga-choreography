package com.sagaorder.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.baseevents.events.PaymentEvent;
import com.sagaorder.entities.Order;

import com.sagaorder.repos.OrderRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentStatus {

	@Autowired
	private OrderRepo orderRepo;
	@KafkaListener(topics = {"payment-order","reverse-payment"},groupId = "order-group")
	public void updatePaymentStatus(PaymentEvent event) {
		log.info("Entering to update payment status in Order= "+event);
		Optional<Order> order=orderRepo.findById(event.getOrder().getOrderId());
		if(order.isPresent()) {
			
			
			if(event.getType().equalsIgnoreCase("Payment Sucessfull")) {
				
				order.get().setPayment("Sucessfull");
				orderRepo.save(order.get());
			}else {
				order.get().setPayment("Failed");
				orderRepo.save(order.get());

			}
					
		}
		
}}
