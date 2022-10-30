package com.luv2code.springdemo.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.entity.Message;
import com.luv2code.springdemo.service.MessageService;

@Component
public class AsyncMessageSseEComponent {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// need to inject our message Service
	@Autowired
	private MessageService messageService;

	@Async
	public void async100mesJSON(Message message) throws Exception {
		logger.debug("Execute method asynchronously. {}", Thread.currentThread().getName());
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
				Message m = new Message();
				m.setTitle(message.getTitle());
				m.setText(message.getText() + "   serverRip N°:"+ i);
			    LocalDateTime myDateObj = LocalDateTime.now();
			    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss.ns");
			    String formattedDate = myDateObj.format(myFormatObj);				
			    m.setSendingTime(formattedDate);
				String messagString = messageService.saveAndGetJSON(m);
				messageService.dispatchEventJSON(messagString, m.getMessageID());
			} catch (final InterruptedException e) {
				logger.error("Errore InterruptedException - nell'invio del messaggio tipo di errore: {}", e);
			} catch (final Exception e) {
				logger.error("Errore Exception - nell'invio del messaggio tipo di errore: {}", e);
			}
		}
	}

	@Async
	public Future<String> asyncMethodWithReturnType() {
		logger.debug("Execute method asynchronously. {}", Thread.currentThread().getName());
		try {
			Thread.sleep(5000);
			return new AsyncResult<>("hello world !!!!  " + Thread.currentThread().getName());
		} catch (final InterruptedException e) {

		}
		return null;
	}

}
