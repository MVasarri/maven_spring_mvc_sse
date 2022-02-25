package com.luv2code.springdemo.service;

//import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luv2code.springdemo.entity.Message;

public interface MessageService {
    public SseEmitter subscribe(String userID, Integer prevMsgID);  
    
    public void dispatchEventJSON(String JSON, Long messageID);
    
    public void unsubscribe(String userID);
    
    public String saveAndGetJSON(Message message) throws JsonProcessingException;
    
//	public void saveMessage(MessageEntityModel theMessage);
//
//    public List<MessageEntityModel> getMessage();
   




    

	
}
