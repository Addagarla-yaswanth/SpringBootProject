package com.infy.rewards.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomerDTO {
	private int custId;
	private String custName;
	private String phoneNo;
	private List<TransactionDTO> transaction;
}
