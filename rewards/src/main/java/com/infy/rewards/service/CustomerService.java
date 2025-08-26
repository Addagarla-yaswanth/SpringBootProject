package com.infy.rewards.service;


import com.infy.rewards.dto.CustomerDTO;

public interface CustomerService {
	public CustomerDTO createCustomer(CustomerDTO customerDTO);
	public String getTotalRewardPoints(int custId);
    public String getMonthlyRewardPoints(int custId, int monthOffset);

}
