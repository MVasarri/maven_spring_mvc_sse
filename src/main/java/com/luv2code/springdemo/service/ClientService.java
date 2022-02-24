package com.luv2code.springdemo.service;

import java.util.List;

import com.luv2code.springdemo.entity.Client;

public interface ClientService {
	
		public void saveClient(Client theClient);
		public List<Client> getClients();
		public Client getClient(String clientID);

}
