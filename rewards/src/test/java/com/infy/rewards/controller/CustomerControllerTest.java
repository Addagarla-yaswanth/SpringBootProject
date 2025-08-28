package com.infy.rewards.controller;

import com.infy.rewards.dto.CustomerDTO;
import com.infy.rewards.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // initialize @Mock and @InjectMocks
    }

    @Test
    void testCreateCustomer() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustId(1);
        dto.setCustName("Yaswanth");

        Mockito.when(customerService.createCustomer(Mockito.any(CustomerDTO.class)))
                .thenReturn(dto);

        ResponseEntity<CustomerDTO> response = customerController.createCustomer(dto);

        CustomerDTO body = response.getBody();

        assertEquals(1, body.getCustId());
        assertEquals("Yaswanth", body.getCustName());
    }

    @Test
    void testGetTotalRewardPoints() {
        Mockito.when(customerService.getTotalRewardPoints(1))
                .thenReturn("Total points: 100");

        ResponseEntity<String> result = customerController.getTotalRewardPoints(1);

        assertEquals("Total points: 100", result.getBody());
    }

    @Test
    void testGetMonthlyRewardPoints() {
        Mockito.when(customerService.getMonthlyRewardPoints(1, 2))
                .thenReturn("Points for month offset 2: 50");

        ResponseEntity<String> result = customerController.getMonthlyRewardPoints(1, 2);

        assertEquals("Points for month offset 2: 50", result.getBody());
    }
}