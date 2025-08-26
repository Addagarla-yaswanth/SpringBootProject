package com.infy.rewards.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infy.rewards.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer>{
	List<Transaction> findByCustomerCustIdAndDateAfter(int custId, LocalDate fromDate);

}
