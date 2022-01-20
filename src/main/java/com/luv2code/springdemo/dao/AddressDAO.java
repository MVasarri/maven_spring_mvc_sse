package com.luv2code.springdemo.dao;

import java.util.List;

import com.luv2code.springdemo.entity.Address;

public interface AddressDAO {

	public List<Address> getAddress();

	public void saveAddress(Address theAddress);

	public Address getAddress(int theId);

	public void deleteAddress(int theId);
	
}
