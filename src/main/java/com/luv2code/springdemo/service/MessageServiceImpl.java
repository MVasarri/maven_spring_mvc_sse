package com.luv2code.springdemo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.dataTransfert.MessageEntityModel;

@Service
public class MessageServiceImpl implements MessageService {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    //creiamo una Mappa dove immagazzinare le sottoscrizioni dei client-ricevitori all'evento con una stringa che fa da ID , in modo che si possa mandare altri eventi ai subscribers mediante l'ID o inviando a tutti
    public Map<String, SseEmitter> emitters = new HashMap<>();
    
    public Map<String, Integer> lastMessageSend = new HashMap<>();
    
    private final AtomicInteger IDmessage = new AtomicInteger();
    
    List<MessageEntityModel> messageList = new CopyOnWriteArrayList<>();

    
	@Override
    public SseEmitter subscribe(String userID, Integer nNews) {
        //creiamo l'oggetto sse, ci inserisco un time out molto grande, ma può essere impostato come si vuole e l'oggetto sseEmitter gestira gli errori di timeOut
        //Long.MAX_VALUE, (long) 100000,   10_000L
		SseEmitter sseEmitter;
		if(emitters.get(userID) == null) {
			
			sseEmitter = new SseEmitter(Long.MAX_VALUE);
	    	
	        emitters.put(userID, sseEmitter);
	        lastMessageSend.put(userID, nNews);
	        sendinitEvent(userID, sseEmitter);
	        logger.info("inizializzazione eseguita con successo del subscriber: {}", userID);
		}
		else {
			sseEmitter = emitters.get(userID);
			logger.info("Ben tornato subscriber: {}", userID);
		}
		if(IDmessage.get() > nNews) {
			//recoverMessage(nNews, userID, sseEmitter);
			int mLost = lastMessageSend.get(userID) - nNews;
			logger.error("Hai perso {} messaggi", mLost);
		}
		
		
        logger.debug("stampa l'oggetto Sse' \n {}", sseEmitter);
        logger.debug("stampa la lista di Sse' \n {}", emitters);
    
        //l'evento onCompletion fa in modo che l'ascoltatore non venga rimosso una volta fatta la prima esecuzione, ma lo mantiene attivo nell'elenco fino all'arrivo di una richiesta di ciusura da parte del client
        sseEmitter.onCompletion(() -> {
        	lastMessageSend.remove(userID);
            emitters.remove(userID);
            logger.debug("stampa la lista di Sse, dopo la rimozione degli eventi conclusi con: onCompletion' \n {}", emitters);
        });

        sseEmitter.onTimeout(() -> {
        	lastMessageSend.remove(userID);
            emitters.remove(userID);
            logger.debug("subscribers ancora Vivi dopo il Timeout' \n {}", emitters);
        });
        sseEmitter.onError((e) -> {  
        	lastMessageSend.remove(userID);
            emitters.remove(userID);
            logger.debug("stampa la lista di Sse, dopo la rimozione degli eventi conclusi: onError' \n {}", emitters);
        });

        return sseEmitter;
    }
    
	@Override
    public void /*String*/ dispatchEventJSON(@RequestBody MessageEntityModel article) throws Exception {
    	article.setMessageID(incrementIDmessage().toString());
    	logger.debug("dispatchEvent2- DEBUG-00- stampa l'articolo che invierà all'evento 'latestNews' nel formato JSON, ricevo dalla post dopo essere stato mappato come oggetto Articolo dal RequestBody \n IDMessaggio: {} \n title: {}\n paragrafo: {}", article.getMessageID(), article.getTitle(), article.getText());
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(article);
        logger.debug("dispatchEvent2- DEBUG-01- stampa l'articolo che invierà all'evento 'latestNews', dopo aver riconvertito l'articolo, da oggetto a stringa tramite Jeckson \n IDMessaggio: {} \n title: {}\n paragrafo: {}", article.getMessageID(), article.getTitle(), article.getText());
        messageList.add(article);
        List<String> emittersToBeDeleted = new CopyOnWriteArrayList<>();
        //scorro l'elenco dove sono memorizzati i miei diversi clienti
        for (String id : emitters.keySet()) {
            SseEmitter emitter= emitters.get(id);
        	//lastMessageSend.put(id, lastMessageSend.get(id) + 1);
            try {
                System.out.println("hello " + message);
                //inviero il mio evento latestNews con all'interno l'articolo ad ogni client presente nella lista
                //To do Analizzare questo elenco per verifichare chi è tra questi ancora aperto e togliere chi non è più in ascolto
                emitter.send(SseEmitter.event()
                						.name("latestNews")
                						.data(message)
                						.reconnectTime(1000)
                						.id(article.getMessageID())
				);
                logger.debug("evento latestNews n: {} di tipo Send All inviato ID: {}", article.getMessageID(),id);
            } catch (IOException e) {
                logger.error("errore sulla sseEmitter durante nella spedizione dell'evento 'latestNews' \n error: {}",e);
                //salvo l'id sulla lista dei eventi da cancellarte
                emittersToBeDeleted.add(id);
            }
        }
        //lacio una funzione che cancella elementi da una lista che indica gli eventi da cancellarte
        if(!emittersToBeDeleted.isEmpty()) {
            delateEmitter(emittersToBeDeleted);
        }
       // return "ha funzionato";
    }
   
    
	@Override
    public void unsubscribe(@RequestParam String userID) {
    	SseEmitter sseEmitter = emitters.get(userID);
        if (sseEmitter != null) {
            sseEmitter.complete();
            logger.info("completamento sseEmitter{}", userID);
        }
        else {
        	logger.warn("sseEmitter:{} non presente", userID);
        }
    }
    

    private void sendinitEvent(String userID, SseEmitter sseEmitter) {
        //sseEmiter.event richiede una catch per gestire eccezioni di tipo IO
        try {
            //invia un evento di inizializzazione ai client
            sseEmitter.send(SseEmitter.event().name("INIT"));
            logger.info("inizializzazione eseguita con successo del subscriber: {}", userID);
        } catch (IOException e) {
            logger.error("si è verificato sulla sseEmitter durante nella spedizione dell'evento INIT \n {}", e);
            emitters.remove(userID);     
        }
    }
    
    //questa tecnica rischia di sovrascrivere messaggi più recenti o di reinviare dei messaggi che in realta aveva già ricevuto
    private void recoverMessage(Integer nNews, String userID, SseEmitter sseEmitter) {
        //sseEmiter.event richiede una catch per gestire eccezioni di tipo IO
    	for (MessageEntityModel message : messageList ) {
        	try {
                //invia un evento di inizializzazione ai client
        		sseEmitter.send(SseEmitter.event()
						.name("latestNews")
						.data(message)
						.reconnectTime(1000)
						.id(message.getMessageID())
        		);
                logger.error("messaggio id:{} recuperato ", message.getMessageID());
            } catch (IOException e) {
                logger.error("si è verificato sulla sseEmitter durante nella spedizione dell'evento INIT \n {}", e);
                emitters.remove(userID);     
            }
    	}
    }
    
  

    private void delateEmitter(List<String> emittersToBeDeleted) {
    	logger.info("emitters prima della cancellazione: {}", emitters);
    	for (String id : emittersToBeDeleted) {
            //qui uso la rimozione, perchè non sono in grado di rilevare quando il client non è più connesso al mio emettitore
            emitters.remove(id);
        	//lastMessageSend.remove(id);
        }
    	logger.info("emitters dopo della cancellazione: {}", emitters);
	}
    
    public Integer incrementIDmessage() {
    	return IDmessage.getAndIncrement();
    }
    
//    public Integer getIDmessage() {
//        return IDmessage.get();
//    }





    	

	
	
	
}
