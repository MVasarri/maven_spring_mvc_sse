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
	public List<Message> getAllMessages() {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Message> theQuery = 
				currentSession.createQuery("from Message",
						Message.class);
		 
		// execute query and get result list
		List<Message> messages = theQuery.getResultList();
        logger.debug("lista completa dei messages del DB 'getAllMessages'\n clients: {}", messages);
			
		// return the results		
		return messages;
	}
	
	
	@Override
	public List<Message> getRecoverMessages(long prevMsgID) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Message> theQuery = 
				currentSession.createQuery("from Message WHERE id> :MYID",
						Message.class).setParameter("MYID", prevMsgID);
		 
		// execute query and get result list
		List<Message> messages = theQuery.getResultList();
        logger.debug("'getRecoverMessages' lista messages del DB a partire dal nMsg: {} \n messages: {}",prevMsgID, messages);			
		// return the results		
		return messages;
	}


	@Override
	public Message getMessage(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		Message theMessage = currentSession.get(Message.class, theId);
        logger.debug("L'oggetto theMessage {} è stato raccolto dal DB 'getMessage'\n IDMessaggio: {} \n title: {}\n text: {} ",theId, theMessage.getMessageID(), theMessage.getTitle(), theMessage.getText());
		
		return theMessage;
	}
	
	@Override
	public Long getLastID() {	
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Integer lastId = 	((Integer) currentSession.createSQLQuery(
											"SELECT max(id) FROM Message")
												.uniqueResult()
						);
		if(lastId==null) {
			return 0L;
		}
        logger.debug("'getLastID' DAO ultimo id caricato sul DB lastID: {} ",lastId);

		// return the results	
		//Long lastId = theQuery.
		return lastId.longValue();
	}
	
	@Override
	public Long countDBMsg() {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Long countM = 	((BigInteger) currentSession.createSQLQuery(
											"SELECT count(id) FROM Message")
												.uniqueResult()
						).longValue();
        logger.debug("'countDBmessages' DAO Numero di messaggi sul DB nDBMsg: {} ",countM);

		return countM;
	}
	

	@Override
	public Long countMsgLost(long prevMsgID) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Long nMsgLost = 	((BigInteger) currentSession.createSQLQuery(
											"SELECT count(id) FROM Message WHERE id>= :MYID")
												.setParameter("MYID", prevMsgID)
												.uniqueResult()
						).longValue();
        logger.debug("'countDBmessages' DAO Numero di messaggi sul DB nDBMsg: {} ",nMsgLost);

		return nMsgLost;
	}


	@Override
	public void saveMessage(Message theMessage) {
        logger.debug("L'oggetto theMessage è arrivato a destinazione nel DAO 'saveMessage'\n IDMessaggio: {} \n title: {}\n text: {}", theMessage.getMessageID(), theMessage.getTitle(), theMessage.getText());
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate theMessage ... 
		currentSession.saveOrUpdate(theMessage);
        logger.debug("theMessage: {} salvato ", theMessage.getMessageID());

		
	}


}
