package com.sagaorder.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagaorder.entities.Order;
@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
	

}
