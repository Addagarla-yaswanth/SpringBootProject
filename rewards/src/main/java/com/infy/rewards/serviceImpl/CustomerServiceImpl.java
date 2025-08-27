package com.infy.rewards.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infy.rewards.dto.CustomerDTO;
import com.infy.rewards.entity.Customer;
import com.infy.rewards.entity.Transaction;
import com.infy.rewards.exception.CustomerNotFoundException;
import com.infy.rewards.exception.NoPurchaseFoundException;
import com.infy.rewards.repository.CustomerRepository;
import com.infy.rewards.repository.TransactionRepository;
import com.infy.rewards.service.CustomerService;
@Service
public class CustomerServiceImpl implements CustomerService{
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private Environment ev;
	@Autowired
	private PasswordEncoder passwordEncoder;
	public ModelMapper mapper=new ModelMapper();
	@Override
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		// TODO Auto-generated method stub
		Customer customer=mapper.map(customerDTO, Customer.class);
		customer.setPhoneNo(passwordEncoder.encode(customer.getPhoneNo()));
		if (customer.getTransaction() != null) {
			for (Transaction tx : customer.getTransaction()) {
		        tx.setCustomer(customer); // set back-reference
		    }
		}
		customerRepository.save(customer);
		return mapper.map(customer, CustomerDTO.class);
	}

	private int calculateRewards(Transaction txn) {
        int amount=txn.getAmount();
        if (amount <= 50) return 0;
        if (amount <= 100) return amount - 50;
        return (2 * (amount - 100)) + 50;
    }

	@Override
	public String getTotalRewardPoints(int custId) throws CustomerNotFoundException,NoPurchaseFoundException{
		// TODO Auto-generated method stub
		Customer customer = customerRepository.findById(custId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        ev.getProperty("CustomerNotFound")
                                .replace("{0}", String.valueOf(custId))
                ));
		LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Transaction> transactions =
                transactionRepository.findByCustomerCustIdAndDateAfter(custId, threeMonthsAgo);
        if (transactions.isEmpty()) {
            throw new NoPurchaseFoundException(ev.getProperty("NoPurchaseFound"));
        }
        int points= transactions.stream()
                           .mapToInt(this::calculateRewards)
                           .sum();
        String message = ev.getProperty("reward.totalPoints")
                .replace("{0}", customer.getCustName())
                .replace("{1}", String.valueOf(points));
        return message;

	}

	@Override
	public String getMonthlyRewardPoints(int custId, int monthOffset) throws CustomerNotFoundException,NoPurchaseFoundException,IllegalArgumentException{
		// TODO Auto-generated method stub
		Customer customer = customerRepository.findById(custId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        ev.getProperty("CustomerNotFound")
                                .replace("{0}", String.valueOf(custId))
                ));
		if (monthOffset < 1 || monthOffset>12) {
            throw new IllegalArgumentException(ev.getProperty("InvalidMonthOffset"));
        }
		LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Transaction> transactions =
                transactionRepository.findByCustomerCustIdAndDateAfter(custId, threeMonthsAgo);
        if (transactions.isEmpty()) {
            throw new NoPurchaseFoundException(ev.getProperty("NoPurchaseFound"));
        }

        // Group transactions by month
        Map<Integer, List<Transaction>> transactionsByMonth = transactions.stream()
            .collect(Collectors.groupingBy(txn -> txn.getDate().getMonthValue()));

        // Find the target month based on offset
        int targetMonth = monthOffset;

        int points= transactionsByMonth.getOrDefault(targetMonth, List.of())
                .stream()
                .mapToInt(this::calculateRewards)
                .sum();
        String message = ev.getProperty("reward.monthlyPoints")
                .replace("{0}", customer.getCustName())
                .replace("{1}", String.valueOf(points))
                .replace("{2}", String.valueOf(monthOffset));
        return message;
	}

}
