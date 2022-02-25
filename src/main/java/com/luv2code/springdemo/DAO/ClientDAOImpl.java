package com.luv2code.springdemo.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.entity.Client;

@Repository
public class ClientDAOImpl implements ClientDAO {
	
	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
    private static final Logger logger
    	= LoggerFactory.getLogger(HomeController.class);
    
	
	@Override
	public void saveClient(Client theClient) {
        logger.debug("L'oggetto theClient è arrivato al DAO 'saveClient'\n DatabaseClientID: {} \n ClientID: {} \n lastConnection: {} ", theClient.getDatabaseClientID(), theClient.getClientID(), theClient.getLastConnection());
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate theMessage ... 
		currentSession.saveOrUpdate(theClient);
	}
	
	@Override
	public List<Client> getAllClients(){
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Client> theQuery = 
				currentSession.createQuery("from Client",
						Client.class);
		 
		// execute query and get result list
		List<Client> clients = theQuery.getResultList();
        logger.debug("lista clients raccolta dal DB 'getAllClients'\n clients: {}", clients);

				
		// return the results		
		return clients;
		
	}
	@Override
	public Client getClient(String clientID) {
		// TODO: va cambiata la funzione Get perche occorre una createQuery per  cervare sulla colonna clientID e non su ID del database
		// TODO: va impostato clientID come paramentro unique per evitare che vengano caricati user con lo stesso ID sul database
        logger.debug("Id del client Ricevitore arrivato al DAO 'getClient' \n clientID: {} ", clientID );

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		Query<Client> theQuery = 
				currentSession.createQuery("FROM Client WHERE clientID= :MYID ",
						Client.class).setParameter("MYID", clientID);
		List<Client> clients = theQuery.getResultList();
        logger.debug("lista dei clients raccolti dal DB \n clients: {}", clients);

		if(clients.isEmpty()) {
			return null;
		}
		else {
			//Client theClient = currentSession.get(Client.class, clientID);
	        logger.debug("L'oggetto theClient è stato raccolto dal DB 'getClients'\n DatabaseClientID: {} \n ClientID: {}", clients.get(0).getDatabaseClientID(), clients.get(0).getClientID());
			return clients.get(0);
		}
	}

}
