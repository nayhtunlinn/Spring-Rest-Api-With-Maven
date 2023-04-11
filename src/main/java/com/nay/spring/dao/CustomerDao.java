package com.nay.spring.dao;

import java.util.List;

import com.nay.spring.entity.Customer;

public interface CustomerDao {
	
public List<Customer> getCustomers();
	
	public void saveCustomer(Customer customer);

	public Customer getCustomer(int id);

	public void delete(int id);
	
	public List<Customer> searchCustomers(String theSearchName);
	
}
