package com.luv2code.springdemo.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springdemo.dataTransfert.ArticleModel;

@RestController	
public class NewsController {
	
	public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	// method for client subscription
	@CrossOrigin
	@RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
	public SseEmitter subscribe() {
		
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		try {
			sseEmitter.send(SseEmitter.event().name("INIT"));
		} catch (IOException e) {
			emitters.remove(emitters);
		}
		sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
		
		emitters.add(sseEmitter);
		return sseEmitter;
	}
	
	// method for dispatching events to all clients
	@PostMapping(value = "/dispatchEvent")
	public void dispatchEventToClients (@RequestParam String title, @RequestParam String text) {
	   
		System.out.println("hello " + title + "/n" + text);
		String eventFormatted = new JSONObject()
				.put("title", title)
				.put("text", text).toString();
		System.out.println("hello " + eventFormatted);
		for ( SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().name("latestNews").data(eventFormatted));
			} catch (IOException e) {	
				emitters.remove(emitter);
			}
		}	
	}
	
	// method for dispatching events to all clients
	@PostMapping(value = "/dispatchEvent2", consumes="application/json")
	public void dispatchEvent2ToClients (@RequestBody ArticleModel article	) throws Exception {
		
		System.out.println("hello " + article);
		
		for ( SseEmitter emitter : emitters) {
			try {
				
				ObjectMapper mapper = new ObjectMapper();
                String message = mapper.writeValueAsString(article);
                System.out.println("hello " + message);
				emitter.send(SseEmitter.event().name("latestNews").data(message));
			} catch (IOException e) {	
				emitters.remove(emitter);
			}
		}	
	}
	
	
	
	
	

}
