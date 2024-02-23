package com.sagaorder.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerOrder {
	
	private String item;
	
	private int quantity;
	
	private double amt;
	
	private String pymtStatus;
	
	private String paymentMethod;
	
	private long orderId;
	
	private String address;

}
