package com.luv2code.springdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.DAO.ClientDAO;
import com.luv2code.springdemo.entity.Client;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
	
    // need to inject address dao
    @Autowired
    private ClientDAO clientDAO;
	
	@Override
	public void saveClient(Client theClient) {
		clientDAO.saveClient(theClient);
	}
	@Override
	public List<Client> getClients(){
		return clientDAO.getClients();	
	}
	@Override
	public Client getClient(String clientID) {
		return clientDAO.getClient(clientID);
	}

}
