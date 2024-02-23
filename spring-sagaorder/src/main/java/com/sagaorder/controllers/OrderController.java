package com.sagaorder.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baseevents.dto.CustomerOrder;
import com.baseevents.events.OrderEvent;
import com.sagaorder.entities.Order;

import com.sagaorder.repos.OrderRepo;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;
	
	@PostMapping("/create")
	public ResponseEntity createOrder(@RequestBody CustomerOrder customerOrder) {
		
		Order order=new Order();
		order.setItem(customerOrder.getItem());
		order.setAmt(customerOrder.getAmt());
		//order.setPayment(customerOrder.getPayment());
		order.setStatus("CREATED");
		
		try {
			
		order=orderRepo.save(order);
		
		customerOrder.setOrderId(order.getId());
		
		OrderEvent event=new OrderEvent();
		event.setCustomerOrder(customerOrder);
		event.setType("Order Created..");
		Message<OrderEvent> message=MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC,"saga-orders").build();
		//kafkaTemplate.send("saga-orders", event);
		kafkaTemplate.send(message);
		log.info("Produced order event sucessfully = "+event);
		}catch (Exception e) {
			// TODO: handle exception
			order.setStatus("Failed");
			orderRepo.save(order);
			return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Order failed");
		}
		return ResponseEntity.ok("Order Created....");
		
		
	}

}
