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
import com.luv2code.springdemo.entity.Message;

@Repository
public class MessageDAOImpl implements MessageDAO {
	
	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
    private static final Logger logger
    	= LoggerFactory.getLogger(HomeController.class);
    
	@Override
	public List<Message> getMessages() {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Message> theQuery = 
				currentSession.createQuery("from Message",
						Message.class);
		 
		// execute query and get result list
		List<Message> messages = theQuery.getResultList();
        logger.debug("lista completa dei messages del DB \n clients: {}", messages);
			
		// return the results		
		return messages;
	}
	
	
	@Override
	public List<Message> getRecoverMessages(long nMsg) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Message> theQuery = 
				currentSession.createQuery("from Message WHERE id>= :MYID",
						Message.class).setParameter("MYID", nMsg);
		 
		// execute query and get result list
		List<Message> messages = theQuery.getResultList();
        logger.debug("lista messages del DB a partire dal nMsg: {} \n clients: {}",nMsg, messages);			
		// return the results		
		return messages;
	}


	@Override
	public Message getMessage(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		Message theMessage = currentSession.get(Message.class, theId);
        logger.debug("L'oggetto theMessage {} è stato raccolto dal DB \n IDMessaggio: {} \n title: {}\n paragrafo: {} ",theId, theMessage.getMessageID(), theMessage.getTitle(), theMessage.getText());
		
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
	public void saveMessage(Message theMessage) {
        logger.debug("L'oggetto theMessage è arrivato a destinazione nel DAO\n IDMessaggio: {} \n title: {}\n paragrafo: {}", theMessage.getMessageID(), theMessage.getTitle(), theMessage.getText());
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate theMessage ... 
		currentSession.saveOrUpdate(theMessage);
        logger.debug("theMessage: salvato ");

		
	}


}
