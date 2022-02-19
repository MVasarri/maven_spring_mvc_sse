package com.luv2code.springdemo.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.luv2code.springdemo.entity.MessageEntityModel;

public class MessageDAOImpl implements MessageDAO {
	
	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public List<MessageEntityModel> getMessage() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<MessageEntityModel> theQuery = 
				currentSession.createQuery("from Messages",
						MessageEntityModel.class);
		
		// execute query and get result list
		List<MessageEntityModel> messages = theQuery.getResultList();
				
		// return the results		
		return messages;
	}
	
	
	
	@Override
	public void saveMessage(MessageEntityModel theMessage) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate theMessage ... 
		currentSession.saveOrUpdate(theMessage);
		
	}


}
