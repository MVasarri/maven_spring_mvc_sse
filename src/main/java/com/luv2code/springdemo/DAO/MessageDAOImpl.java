package com.luv2code.springdemo.DAO;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.entity.MessageEntityModel;

@Repository
public class MessageDAOImpl implements MessageDAO {
	
	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
    private static final Logger logger
    	= LoggerFactory.getLogger(HomeController.class);
    
	@Override
	public List<MessageEntityModel> getMessages() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<MessageEntityModel> theQuery = 
				currentSession.createQuery("from MessageEntityModel",
						MessageEntityModel.class);
		 
		// execute query and get result list
		List<MessageEntityModel> messages = theQuery.getResultList();
				
		// return the results		
		return messages;
	}
	
	
	@Override
	public MessageEntityModel getMessage(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		MessageEntityModel theMessage = currentSession.get(MessageEntityModel.class, theId);
		
		return theMessage;
	}
	
	@Override
	public Long getLastID() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Long lastId = 	(
							(BigInteger) currentSession.createSQLQuery(
											"SELECT LAST_INSERT_ID()")
												.uniqueResult()
						).longValue();
				
		// return the results	
		//Long lastId = theQuery.
		return lastId;
	}
	
	@Override
	public void saveMessage(MessageEntityModel theMessage) {
        logger.debug("L'oggetto theMessage è arrivato a destinazione \n IDMessaggio: {} \n title: {}\n paragrafo: {}", theMessage.getMessageID(), theMessage.getTitle(), theMessage.getText());
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate theMessage ... 
		currentSession.saveOrUpdate(theMessage);
		
	}


}
