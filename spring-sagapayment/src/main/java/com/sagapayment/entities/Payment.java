package com.sagapayment.entities;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
@Entity
@Getter
@Setter

public class Payment {

	@Id
	@GeneratedValue
	private Long id;

	
	private String mode;

	
	private Long orderId;

	
	private double amount;


	private String status;

}
