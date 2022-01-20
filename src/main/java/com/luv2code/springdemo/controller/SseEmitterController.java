package com.luv2code.springdemo.controller;

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
	
	@RequestMapping("/testSse")
	public String showPage( ) {
		return "test-sse-01";
	}
	
	
//    private ExecutorService nonBlockingService = Executors
//    	      .newCachedThreadPool();
//    	    
//    @GetMapping("/sse")
//    public SseEmitter handleSse() {
//         SseEmitter emitter = new SseEmitter();
//         nonBlockingService.execute(() -> {
//             try {
//                 emitter.send("/ORARIO" + " @ " + new Date());
//                 // we could send more events
//                 emitter.complete();
//             } catch (Exception ex) {
//                 emitter.completeWithError(ex);
//             }
//         });
//         return emitter;
//    }
    

	@GetMapping("/sse")
	public SseEmitter eventEmitter() {
	   SseEmitter emitter = new SseEmitter(); //12000 here is the timeout and it is optional   (long) 12000
	   
	   //create a single thread for sending messages asynchronously
	   ExecutorService executor = Executors.newSingleThreadExecutor();
	   executor.execute(() -> {
	       try {
	           for (int i = 0; i < 1000; i++) {
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
    
    

}
