package com.sagaorder.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "saga_orders")
@Getter
@Setter
public class Order {


	
    @Id
    @GeneratedValue
	private long id;
    
    private String item;
    
	private double amt;
	
	private String payment;
	
	private String status;
}
