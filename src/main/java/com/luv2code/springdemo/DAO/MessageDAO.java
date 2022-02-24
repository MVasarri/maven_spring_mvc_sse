package com.luv2code.springdemo.DAO;

import java.util.List;

import com.luv2code.springdemo.entity.Message;

public interface MessageDAO {
	
	public List<Message> getMessages();
	
	public Message getMessage(int theId);
	
	public Long getLastID();
	
	public void saveMessage(Message theMessage);

}
