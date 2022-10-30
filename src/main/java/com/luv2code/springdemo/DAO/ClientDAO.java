package com.luv2code.springdemo.DAO;

import java.util.List;

import com.luv2code.springdemo.entity.Client;

public interface ClientDAO {

	public void saveClient(Client theClient);

	public List<Client> getAllClients();

	public Client getClient(String clientID);

}
