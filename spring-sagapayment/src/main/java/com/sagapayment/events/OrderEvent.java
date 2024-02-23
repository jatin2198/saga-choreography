package com.sagapayment.events;



import com.sagapayment.dto.CustomerOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderEvent {

	private CustomerOrder customerOrder;
	
	private String type;
}
