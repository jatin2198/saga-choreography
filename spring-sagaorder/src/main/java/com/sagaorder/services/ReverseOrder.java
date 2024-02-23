package com.sagaorder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.baseevents.events.OrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sagaorder.repos.OrderRepo;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ReverseOrder {
	
	@Autowired
	private OrderRepo orderRepo;
	
	@KafkaListener(topics = "reverse-order",groupId = "order-group")
	public void reverseOrder(OrderEvent event) {
		
		log.info("Consuming Reverse Order event= "+event);
		
		try {
			
			//OrderEvent orderEvent=new ObjectMapper().readValue(event, OrderEvent.class);
		
			orderRepo.findById(event.getCustomerOrder().getOrderId()).ifPresent(item->{
				item.setStatus("Failed");
				orderRepo.save(item);
			});
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Exception occured while listening Reverse Order= "+e.getMessage());
		}
		
	}

}
