package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Address;

@Repository
public class AddressDAOImpl implements AddressDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
			
	@Override
	public List<Address> getAddress() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Address> theQuery = 
				currentSession.createQuery("from Address",
						Address.class);
		
		// execute query and get result list
		List<Address> address = theQuery.getResultList();
				
		// return the results		
		return address;
	}
	

	@Override
	public void saveAddress(Address theAddress) {

		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate the customer ... finally LOL
		currentSession.saveOrUpdate(theAddress);
		
	}

	@Override
	public Address getAddress(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		Address theAddress = currentSession.get(Address.class, theId);
		
		return theAddress;
	}

	@Override
	public void deleteAddress(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with primary key
		Query theQuery = 
				currentSession.createQuery("delete from Address where id=:addressId");
		theQuery.setParameter("addressId", theId);
		
		theQuery.executeUpdate();		
	}

}











