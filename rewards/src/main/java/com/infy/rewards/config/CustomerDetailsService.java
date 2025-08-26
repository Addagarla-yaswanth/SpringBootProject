package com.infy.rewards.config;

import com.infy.rewards.entity.Customer;
import com.infy.rewards.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String custName) throws UsernameNotFoundException {
        Customer customer = customerRepository.findAll()
                .stream()
                .filter(c -> c.getCustName().equals(custName))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found: " + custName));

        // Use phoneNo as password (encoded in SecurityConfig)
        return new User(customer.getCustName(), customer.getPhoneNo(), new ArrayList<>());
    }
}
