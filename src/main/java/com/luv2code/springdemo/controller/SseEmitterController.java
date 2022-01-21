package com.luv2code.springdemo.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.luv2code.springdemo.entity.Address;

@Controller
public class SseEmitterController {
	
	// TODO
    // 1. creare un CONTROLLER che torna la pagina test-sse-01
    // 2. nella pagina, si mette il tag <script type=javascript>
    // 3. nello script si chiama il controller sse (quello di questa classe): 
    // const eventSource = new EventSource('http://localhost:8080/.../sse');
	
	@RequestMapping("/testSse1")
	public String showPage1( ) {
		return "test-sse-01";
	}
	
	@RequestMapping("/testSse2")
	public String showPage2( ) {
		return "test-sse-02";
	}
	

	@GetMapping("/sse1")
	public SseEmitter eventEmitter() {
	   SseEmitter emitter = new SseEmitter(); //12000 here is the timeout and it is optional   (long) 12000
	   
	   //create a single thread for sending messages asynchronously
	   ExecutorService executor = Executors.newSingleThreadExecutor();
	   executor.execute(() -> {
	       try {
	           for (int i = 0; i < 10; i++) {
	        	   Thread.sleep(1000);
	        	   emitter.send("message" + i);           
	           }       
	       } catch(Exception e) {
	            emitter.completeWithError(e);       
	       } finally {
	            emitter.complete();       
	       }   
	   });
	   executor.shutdown();
	   return emitter;
	}
	
	@GetMapping("/sse2")
	public SseEmitter eventEmitter2() {
	   SseEmitter emitter = new SseEmitter(); //12000 here is the timeout and it is optional   (long) 12000
	   
	   //create a single thread for sending messages asynchronously
	   ExecutorService executor = Executors.newSingleThreadExecutor();
	   
	   SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event()
	             .id("0") // You can give nay string as id
	             .name("customEventName")
	             .data("message1")
	             .reconnectTime(10000); //reconnect time in millis
	   
	   executor.execute(() -> {	
		   try {
	           for (int i = 0; i < 8; i++) {
	        	   Thread.sleep(800);
	        	   sseEventBuilder.data("chiamata" + i);
	        	   emitter.send(sseEventBuilder);          
	           } 
			   		
		   } catch(Exception e) {
		           	emitter.completeWithError(e);       
		   } finally {
	            	emitter.complete();       
		   }
	   });
	   executor.shutdown();
	   return emitter;
	}
    
	  
	@GetMapping("/sse3")
	public SseEmitter handleSse() {
			final ExecutorService nonBlockingService = Executors
			    .newCachedThreadPool();
			SseEmitter emitter = new SseEmitter();
			nonBlockingService.execute(() -> {
			   try {
		           for (int i = 0; i < 10; i++) {
		        	   Thread.sleep(1000);
				       emitter.send("/ORARIO" + " @ " + new Date());
				       // we could send more events
				       emitter.complete();    
		           }    
			   } catch (Exception ex) {
			       emitter.completeWithError(ex);
			   }
			});
	return emitter;
	}
    

}
