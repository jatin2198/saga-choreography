package com.sagapayment.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sagapayment.entities.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
	public List<Payment> findByOrderId(long orderId);
}
