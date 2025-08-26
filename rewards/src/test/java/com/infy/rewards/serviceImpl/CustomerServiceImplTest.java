package com.infy.rewards.serviceImpl;

import com.infy.rewards.dto.CustomerDTO;
import com.infy.rewards.dto.TransactionDTO;
import com.infy.rewards.entity.Customer;
import com.infy.rewards.entity.Transaction;
import com.infy.rewards.exception.CustomerNotFoundException;
import com.infy.rewards.exception.NoPurchaseFoundException;
import com.infy.rewards.repository.CustomerRepository;
import com.infy.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Environment env;

    @Mock
    private PasswordEncoder passwordEncoder; // Mock PasswordEncoder

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setCustId(1);
        customer.setCustName("John");

        // Mock passwordEncoder.encode to return the same value
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    // ---------- createCustomer ----------
    @Test
    void testCreateCustomer_Success() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustId(1);
        dto.setCustName("John");
        dto.setPhoneNo("pass123");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer saved = invocation.getArgument(0);
            saved.setCustId(1);
            return saved;
        });

        CustomerDTO result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals(1, result.getCustId());
        assertEquals("John", result.getCustName());
    }

    @Test
    void testCreateCustomer_WithTransactions() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustId(1);
        dto.setPhoneNo("pass123");

        TransactionDTO tx = new TransactionDTO();
        dto.setTransaction(List.of(tx));

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.createCustomer(dto);
        assertEquals(1, result.getCustId());
    }

    @Test
    void testCreateCustomer_NullTransactions() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustId(2);
        dto.setPhoneNo("pass123");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.createCustomer(dto);
        assertNotNull(result);
    }

    // ---------- getTotalRewardPoints ----------
    @Test
    void testGetTotalRewardPoints_Success() throws Exception {
        Transaction tx1 = new Transaction();
        Transaction tx2 = new Transaction();
        tx1.setCustomer(customer);
        tx1.setAmount(120);
        tx1.setDate(LocalDate.now());
        tx2.setCustomer(customer);
        tx2.setAmount(80);
        tx2.setDate(LocalDate.now());

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of(tx1, tx2));
        when(env.getProperty("reward.totalPoints"))
                .thenReturn("Hello {0}, your total reward points for last 3 months are {1}.");

        String message = customerService.getTotalRewardPoints(1);

        assertNotNull(message);
        assertTrue(message.contains("John"));
        assertTrue(message.contains("120")); // calculated reward points
    }

    @Test
    void testGetTotalRewardPoints_CustomerNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        when(env.getProperty("CustomerNotFound")).thenReturn("Customer {0} not found");

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getTotalRewardPoints(1));
    }

    @Test
    void testGetTotalRewardPoints_NoPurchaseFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of());
        when(env.getProperty("NoPurchaseFound")).thenReturn("No purchases");

        assertThrows(NoPurchaseFoundException.class,
                () -> customerService.getTotalRewardPoints(1));
    }

    @Test
    void testGetTotalRewardPoints_MultipleTransactions() throws Exception {
        Transaction tx1 = new Transaction();
        Transaction tx2 = new Transaction();
        tx1.setCustomer(customer);
        tx1.setAmount(120);
        tx1.setDate(LocalDate.now());
        tx2.setCustomer(customer);
        tx2.setAmount(200);
        tx2.setDate(LocalDate.now());

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of(tx1, tx2));
        when(env.getProperty("reward.totalPoints"))
                .thenReturn("Hello {0}, your total reward points for last 3 months are {1}.");

        String message = customerService.getTotalRewardPoints(1);

        assertNotNull(message);
        assertTrue(message.contains("John"));
        assertTrue(message.contains("340")); // calculated reward points for multiple transactions
    }

    // ---------- getMonthlyRewardPoints ----------
    @Test
    void testGetMonthlyRewardPoints_Success() throws Exception {
        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setAmount(150);
        tx.setDate(LocalDate.now());

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of(tx));
        when(env.getProperty("reward.monthlyPoints"))
                .thenReturn("Hello {0}, your reward points for month offset {2} are {1}");

        int month = LocalDate.now().getMonthValue();
        String message = customerService.getMonthlyRewardPoints(1, month);

        assertNotNull(message);
        assertTrue(message.contains("John"));
        assertTrue(message.contains(String.valueOf(month)));
        assertTrue(message.contains("150"));
    }

    @Test
    void testGetMonthlyRewardPoints_InvalidMonthOffset_Low() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(env.getProperty("InvalidMonthOffset")).thenReturn("Invalid month");

        assertThrows(IllegalArgumentException.class,
                () -> customerService.getMonthlyRewardPoints(1, 0));
    }

    @Test
    void testGetMonthlyRewardPoints_InvalidMonthOffset_High() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(env.getProperty("InvalidMonthOffset")).thenReturn("Invalid month");

        assertThrows(IllegalArgumentException.class,
                () -> customerService.getMonthlyRewardPoints(1, 13));
    }

    @Test
    void testGetMonthlyRewardPoints_CustomerNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        when(env.getProperty("CustomerNotFound")).thenReturn("Customer {0} not found");

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getMonthlyRewardPoints(1, 5));
    }

    @Test
    void testGetMonthlyRewardPoints_NoPurchaseFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of());
        when(env.getProperty("NoPurchaseFound")).thenReturn("No purchases");

        assertThrows(NoPurchaseFoundException.class,
                () -> customerService.getMonthlyRewardPoints(1, 5));
    }

    @Test
    void testGetMonthlyRewardPoints_EmptyMonthTransactions() throws Exception {
        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setAmount(90);
        tx.setDate(LocalDate.now().minusMonths(1));

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of(tx));
        when(env.getProperty("reward.monthlyPoints"))
                .thenReturn("Hello {0}, your reward points for month offset {2} are {1}");

        int month = LocalDate.now().getMonthValue();
        String message = customerService.getMonthlyRewardPoints(1, month);

        assertNotNull(message);
        assertTrue(message.contains(String.valueOf(month)));
        assertTrue(message.contains("0")); // points = 0 for empty month
    }

    @Test
    void testGetMonthlyRewardPoints_MultipleMonths() throws Exception {
        Transaction tx1 = new Transaction();
        Transaction tx2 = new Transaction();
        tx1.setCustomer(customer);
        tx1.setAmount(120);
        tx1.setDate(LocalDate.now().minusMonths(1));
        tx2.setCustomer(customer);
        tx2.setAmount(200);
        tx2.setDate(LocalDate.now());

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of(tx1, tx2));
        when(env.getProperty("reward.monthlyPoints"))
                .thenReturn("Hello {0}, your reward points for month offset {2} are {1}");

        int month = LocalDate.now().getMonthValue();
        String message = customerService.getMonthlyRewardPoints(1, month);

        assertNotNull(message);
        assertTrue(message.contains("50")); // only current month points counted
    }

    @Test
    void testGetMonthlyRewardPoints_ExactBoundary() throws Exception {
        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setAmount(100);
        tx.setDate(LocalDate.now());

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerCustIdAndDateAfter(eq(1), any()))
                .thenReturn(List.of(tx));
        when(env.getProperty("reward.monthlyPoints"))
                .thenReturn("Hello {0}, your reward points for month offset {2} are {1}");

        int month = LocalDate.now().getMonthValue();
        String message = customerService.getMonthlyRewardPoints(1, month);

        assertNotNull(message);
        assertTrue(message.contains("50")); // exact boundary points
    }

    // ---------- Edge tests for calculateRewards ----------
    @Test
    void testCalculateRewards_Below50() {
        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setAmount(30);
        assertEquals(0, invokeCalculateRewards(tx));
    }

    @Test
    void testCalculateRewards_Between50And100() {
        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setAmount(80);
        assertEquals(30, invokeCalculateRewards(tx));
    }

    @Test
    void testCalculateRewards_Above100() {
        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setAmount(150);
        assertEquals(150, invokeCalculateRewards(tx));
    }

    // Helper method to access private calculateRewards
    private int invokeCalculateRewards(Transaction tx) {
        try {
            var method = CustomerServiceImpl.class.getDeclaredMethod("calculateRewards", Transaction.class);
            method.setAccessible(true);
            return (int) method.invoke(customerService, tx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
