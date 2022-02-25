package com.luv2code.springdemo.DAO;

import java.util.List;

import com.luv2code.springdemo.entity.Message;

public interface MessageDAO {
	
	public List<Message> getRecoverMessages(long prevMsgID);
	
	public List<Message> getAllMessages();

	public Message getMessage(int theId);
	
	public Long getLastID();
	
	public Long countDBMsg();
	
	public Long countMsgLost(long prevMsgID);
	
	public void saveMessage(Message theMessage);

}
