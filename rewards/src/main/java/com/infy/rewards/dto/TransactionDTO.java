package com.infy.rewards.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionDTO {
	private int id;
	private LocalDate date;
	private int amount;
	private int customerId;

}
