package com.luv2code.springdemo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="client" )
public class Client {
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO )IDENTITY
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	@Column(name="id")
	private Long databaseClientID;
	
	@Column(name="clientID")
	private String clientID;
	//new Date()
	
	@Column(name="lastConnection")
	private Long lastConnection;

	public Long getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(Long lastConnection) {
		this.lastConnection = lastConnection;
	}

	public Long getDatabaseClientID() {
		return databaseClientID;
	}

	public void setDatabaseClientID(Long databaseClientID) {
		this.databaseClientID = databaseClientID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

}
