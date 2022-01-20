package com.luv2code.springdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.dao.AddressDAO;
import com.luv2code.springdemo.entity.Address;

@Service
public class AddressServiceImpl implements AddressService {

	// need to inject address dao
	@Autowired
	private AddressDAO addressDAO;
	
	@Override
	@Transactional
	public List<Address> getAddress() {
		return addressDAO.getAddress();
	}


	@Override
	@Transactional
	public void saveAddress(Address theAddress) {

		addressDAO.saveAddress(theAddress);
	}

	@Override
	@Transactional
	public Address getAddress(int theId) {
		
		return addressDAO.getAddress(theId);
	}

	@Override
	@Transactional
	public void deleteAddress(int theId) {
		
	addressDAO.deleteAddress(theId);
	}
}





