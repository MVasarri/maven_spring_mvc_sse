package com.luv2code.springdemo.controller;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.luv2code.springdemo.component.AsyncMessageSseEComponent;
import com.luv2code.springdemo.dataTransfert.ArticleModel;
import com.luv2code.springdemo.service.MessageService;

@RestController
public class NewsController {
	
	// need to inject our asyncMessageSseEComponent
	@Autowired
	private  AsyncMessageSseEComponent asyncMessageSseEComponent;
	
	// need to inject our message Service
	@Autowired
	private  MessageService messageService;

    private static final Logger logger
            = LoggerFactory.getLogger(HomeController.class);

    // method for client subscription
    //creiamo un endpoint di sottoscrizione che restituira l'oggetto SseEmitter
    //Questa annotazione @CrossOrigin abilita la condivisione di risorse tra origini solo per questo metodo specifico.
    //Per impostazione predefinita, consente tutte le origini, tutte le intestazioni e i metodi HTTP specificati 
    //nell'annotazione @RequestMapping.   
	@CrossOrigin
	@RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
	public SseEmitter subscribe(@RequestParam String userID) {
	     return messageService.subscribe(userID);
	} 
    
    
    // method for dispatching events to all clients
    @PostMapping(value = "/dispatchEvent")
    public void dispatchEventToClients(@RequestParam String title, @RequestParam String text) throws InterruptedException, ExecutionException {
        messageService.dispatchEventToClients(title, text);
    }
    
    
    // method for dispatching events to a Specific User
	@PostMapping(value = "/dispatchEventToSpecificUser")
    public void dispatchEventToSpecificUser(@RequestParam String title, @RequestParam String text, @RequestParam String userID) {
        messageService.dispatchEventToSpecificUser(title, text, userID);
	}

	
    // method for dispatching events to all clients
    @PostMapping(value = "/dispatchEvent2", consumes = "application/json")
    public void /*String*/ dispatchEvent2(@RequestBody ArticleModel article) throws Exception {
    	messageService.dispatchEvent2(article);
    }
    
 
    // method for dispatching events to all clients
    @PostMapping(value = "/dispatchEvent2ToSpecificUser", consumes = "application/json")
    public void dispatchEvent2ToSpecificUser(@RequestBody ArticleModel article) throws Exception {
    	messageService.dispatchEvent2ToSpecificUser(article);
    }
    

    @GetMapping(value = "/unsubscribe", consumes = MediaType.ALL_VALUE)
    public void unsubscribe(@RequestParam String userID) {
    	messageService.unsubscribe(userID);
    }
    
//	logger.debug("\n	Invoking an asynchronous method. {}", Thread.currentThread().getName());
//final Future<String> future = asyncMessageSseEComponent.asyncMethodWithReturnType();
//while (true) {
//    if (future.isDone()) {
//        logger.debug(" Result from asynchronous process - {}",future.get());
//        break;
//    }
//    logger.debug("Continue doing something else. ");
//    Thread.sleep(1000);
//}


}
