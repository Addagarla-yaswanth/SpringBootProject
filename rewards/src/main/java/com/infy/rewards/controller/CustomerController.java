//package com.infy.rewards.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.infy.rewards.dto.CustomerDTO;
//import com.infy.rewards.service.CustomerService;
//
//@RestController
//@RequestMapping("/customer")
//public class CustomerController {
//	@Autowired
//    private CustomerService customerService;
//
//    //Create a new customer
//    @PostMapping
//    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
//        return customerService.createCustomer(customerDTO);
//    }
//
//    //Get total reward points for a customer (last 3 months)
//    @GetMapping("/{custId}/rewards")
//    public String getTotalRewardPoints(@PathVariable int custId) {
//        return customerService.getTotalRewardPoints(custId);
//    }
//
//    //Get reward points of a particular month (offset: 0 = current month, 1 = last month, etc.)
//    @GetMapping("/{custId}/rewards/{monthOffset}")
//    public String getMonthlyRewardPoints(@PathVariable int custId,@PathVariable int monthOffset) {
//        return customerService.getMonthlyRewardPoints(custId, monthOffset);
//    }
//
//}

package com.infy.rewards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.infy.rewards.dto.CustomerDTO;
import com.infy.rewards.exception.CustomerNotFoundException;
import com.infy.rewards.exception.NoPurchaseFoundException;
import com.infy.rewards.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Create a new customer
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.ok(savedCustomer);
    }

    // Get total reward points for a customer (last 3 months)
    @GetMapping("/{custId}/rewards")
    public ResponseEntity<String> getTotalRewardPoints(@PathVariable int custId) {
        try {
            String message = customerService.getTotalRewardPoints(custId);
            return ResponseEntity.ok(message);
        } catch (CustomerNotFoundException | NoPurchaseFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get reward points of a particular month (offset: 1 = current month, 2 = last month, etc.)
    @GetMapping("/{custId}/rewards/{monthOffset}")
    public ResponseEntity<String> getMonthlyRewardPoints(@PathVariable int custId,@PathVariable int monthOffset) {
        try {
            String message = customerService.getMonthlyRewardPoints(custId, monthOffset);
            return ResponseEntity.ok(message);
        } catch (CustomerNotFoundException | NoPurchaseFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

