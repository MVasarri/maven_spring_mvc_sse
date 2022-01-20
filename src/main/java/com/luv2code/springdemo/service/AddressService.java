package com.luv2code.springdemo.service;

import java.util.List;

import com.luv2code.springdemo.entity.Address;

public interface AddressService {
	

	public List<Address> getAddress();

	public void saveAddress(Address theAddress);

	public Address getAddress(int theId);

	public void deleteAddress(int theId);
	
	
	
}
