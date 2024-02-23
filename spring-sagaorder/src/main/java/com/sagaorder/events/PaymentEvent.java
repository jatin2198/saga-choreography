package com.sagaorder.events;

import com.sagaorder.*;
import com.sagaorder.dto.CustomerOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class PaymentEvent {
	private String type;

	private CustomerOrder order;
}
