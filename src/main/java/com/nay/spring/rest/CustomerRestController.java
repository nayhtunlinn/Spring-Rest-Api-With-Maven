package com.nay.spring.rest;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nay.spring.entity.Customer;
import com.nay.spring.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {
	
	@Autowired
	private CustomerService customerservice;
	
	@GetMapping("/customers")
	public List<Customer> getCustomers(){
		return 	customerservice.getCustomers();
		
	}
	
	@GetMapping("/customers/{customerId}")
	public Customer getCustomer(@PathVariable int customerId) {
		
		Customer c=customerservice.getCustomer(customerId);
		
		if(c==null) {
			throw new CustomerNotFoundException("customer id not found - "+customerId);
		}
		return c;
	}

	
	@PostMapping("/customers")
	public Customer addCustomer(@RequestBody Customer customer) {
		customerservice.saveCustomer(customer);
		return customer;
	}
	
	@PutMapping("/customers")
	public Customer updateCustomer(@RequestBody Customer customer) {
		Customer c=customerservice.getCustomer(customer.getId());
		if(c==null) {
			throw new CustomerNotFoundException("Update Customer doesn't exist - "+customer.getId());
		}
		customerservice.saveCustomer(customer);
		return customer;
	}
	
	@DeleteMapping("/customers/{customerId}")
	public String deleteCustomer(@PathVariable int customerId) {
		Customer c=customerservice.getCustomer(customerId);
		if(c==null) {
			throw new CustomerNotFoundException("customer id not found - "+customerId);
		}
		customerservice.delete(customerId);
		
		return "Deleted customer id - "+customerId;
	}
}
