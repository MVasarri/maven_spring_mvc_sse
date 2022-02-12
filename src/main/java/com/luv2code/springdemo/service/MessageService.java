package com.luv2code.springdemo.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.luv2code.springdemo.dataTransfert.ArticleModel;

public interface MessageService {
    public SseEmitter subscribe(String userID, Integer nNews);  
    
    public void dispatchEventToClients(String title,  String text)throws InterruptedException, ExecutionException;
    public void dispatchEventToSpecificUser(String title, String text, String userID);
    
    public void /*String*/ dispatchEventJSON(ArticleModel article) throws Exception;
    public void dispatchEventJSONToSpecificUser(ArticleModel article) throws Exception;
    
    public void unsubscribe(String userID);
    

   




    

	
}
