package com.luv2code.springdemo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.dataTransfert.ArticleModel;

@Service
public class MessageServiceImpl implements MessageService {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    //creiamo una Mappa dove immagazzinare le sottoscrizioni dei client-ricevitori all'evento con una stringa che fa da ID , in modo che si possa mandare altri eventi ai subscribers mediante l'ID o inviando a tutti
    public Map<String, SseEmitter> emitters = new HashMap<>();
    
	@Override
    public SseEmitter subscribe(String userID) {
        //creiamo l'oggetto sse, ci inserisco un time out molto grande, ma può essere impostato come si vuole e l'oggetto sseEmitter gestira gli errori di timeOut
        //Long.MAX_VALUE, (long) 100000,   10_000L
    	SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
    	
        emitters.put(userID, sseEmitter);

        sendinitEvent(userID, sseEmitter);
        logger.info("inizializzazione eseguita con successo del subscriber: {}", userID);


        //sseEmitter.complete();

        logger.debug("stampa l'oggetto Sse' \n {}", sseEmitter);
        logger.debug("stampa la lista di Sse' \n {}", emitters);

        
        //l'evento onCompletion fa in modo che l'ascoltatore non venga rimosso una volta fatta la prima esecuzione, ma lo mantiene attivo nell'elenco fino all'arrivo di una richiesta di ciusura da parte del client
        sseEmitter.onCompletion(() -> {
            emitters.remove(userID);
            logger.debug("stampa la lista di Sse, dopo la rimozione degli eventi conclusi con: onCompletion' \n {}", emitters);
        });

        sseEmitter.onTimeout(() -> {
            emitters.remove(userID);
            logger.debug("subscribers ancora Vivi dopo il Timeout' \n {}", emitters);
        });
        sseEmitter.onError((e) -> {        	
            emitters.remove(userID);
            logger.debug("stampa la lista di Sse, dopo la rimozione degli eventi conclusi: onError' \n {}", emitters);
        });

        return sseEmitter;
    }
	
	@Override
    public void dispatchEventToClients(String title,  String text) throws InterruptedException, ExecutionException {        
        logger.debug("\n	dispatchEvent- DEBUG-00- stampa l'articolo che invierà all'evento 'latestNews' che ricevo dalla post prima di riformattarlo in JSON\n		title: {}\n		paragrafo: {}", title, text);
        String txtDestination = "Destinataro/i: All";
        //trasformo le stringhe title & text in formato JSON
        String eventFormatted = new JSONObject()
                .put("title", title)
                .put("text", text)
                .put("userID", txtDestination).toString();
        logger.debug("\n	dispatchEvent- DEBUG-01-  stampa l'articolo che inviera' all'evento 'latestNews' che ricevo dalla post dopo averlo riformattarlo in JSON\n			articolo: {}", eventFormatted);
		
		//dichiarazione e creazione della lista di id di elementi da cancellare
        List<String> emittersToBeDeleted = new CopyOnWriteArrayList<>();
        
        for (String id : emitters.keySet()) {
            
        	logger.debug("\n	Invoking an asynchronous method. {}", Thread.currentThread().getName());
        	
            SseEmitter emitter= emitters.get(id);
        	try {
                emitter.send(SseEmitter.event().name("latestNews").data(eventFormatted));
                logger.debug("evento Send All inviato");
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
    }
    
	@Override
	public void dispatchEventToSpecificUser(String title,  String text,  String userID) {
        logger.debug("\n	dispatchEvent- DEBUG-00- stampa l'articolo che invierà all'evento 'latestNews', ricevo tramite una post prima di riformattarlo in JSON\n		title: {}\n		paragrafo: {}", title, text);
        String txtDestination = "Destinataro/i: "+ userID;
        //trasformo le stringhe title & text in formato JSON
        String eventFormatted = new JSONObject()
                .put("title", title)
                .put("text", text)
                .put("userID", txtDestination).toString();
        logger.debug("\n	dispatchEvent- DEBUG-01-  stampa l'articolo che inviera' all'evento 'latestNews',dopo averlo riformattarlo in JSON\n			articolo: {}", eventFormatted);
		
		SseEmitter sseEmitter = emitters.get(userID);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("latestNews").data(eventFormatted));
                logger.debug("evento Send ById inviato");
            } catch (IOException e) {
                logger.error("si e' verificato sulla sseEmitter durante nella spedizione dell'evento 'latestNews' \n error: {}\n emitters prima: {}",e ,emitters);
                //qui uso la rimozione, perchù non sono in grado di rilevare quando il client non è più connesso al mio emettitore
                emitters.remove(userID);
                logger.error("emitters dopo: {}", emitters);
            }
        }else {
            logger.error("\n	il messaggio non è stato inviato perchè l'id non è presente");

        }
    }
    
	@Override
    public void /*String*/ dispatchEvent2(@RequestBody ArticleModel article) throws Exception {
    	logger.debug("dispatchEvent2- DEBUG-00- stampa l'articolo che invierà all'evento 'latestNews' nel formato JSON, ricevo dalla post dopo essere stato mappato come oggetto Articolo dal RequestBody \n title: {}\n paragrafo: {}", article.getTitle(), article.getText());

        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(article);
        logger.debug("dispatchEvent2- DEBUG-01- stampa l'articolo che invierà all'evento 'latestNews', dopo aver riconvertito l'articolo, da oggetto a stringa tramite Jeckson\n title: {}\n paragrafo: {}", article.getTitle(), article.getText());
        
        List<String> emittersToBeDeleted = new CopyOnWriteArrayList<>();
        //scorro l'elenco dove sono memorizzati i miei diversi clienti
        for (String id : emitters.keySet()) {
            SseEmitter emitter= emitters.get(id);
            try {
                System.out.println("hello " + message);
                //inviero il mio evento latestNews con all'interno l'articolo ad ogni client presente nella lista
                //To do Analizzare questo elenco per verifichare chi è tra questi ancora aperto e togliere chi non è più in ascolto
                emitter.send(SseEmitter.event().name("latestNews").data(message));
                logger.debug("evento Send All inviato");
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
    public void dispatchEvent2ToSpecificUser(@RequestBody ArticleModel article) throws Exception {
    	logger.debug("dispatchEvent2- DEBUG-00- stampa l'articolo che invierà all'evento 'latestNews' nel formato JSON, ricevo dalla post dopo essere stato mappato come oggetto Articolo dal RequestBody \n title: {}\n paragrafo: {}", article.getTitle(), article.getText());

        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(article);
        logger.debug("dispatchEvent2- DEBUG-01- stampa l'articolo che invierà all'evento 'latestNews', dopo aver riconvertito l'articolo, da oggetto a stringa tramite Jeckson\n title: {}\n paragrafo: {}", article.getTitle(), article.getText());
        
        SseEmitter sseEmitter = emitters.get(article.getUserID());
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("latestNews").data(message));
                logger.debug("evento Send ById inviato");
            } catch (IOException e) {
                logger.error("si e' verificato sulla sseEmitter durante nella spedizione dell'evento 'latestNews' \n error: {}\n emitters prima: {}",e ,emitters);
                //qui uso la rimozione, perchù non sono in grado di rilevare quando il client non è più connesso al mio emettitore
                emitters.remove(article.getUserID());
                logger.error("emitters dopo: {}", emitters);
            }
        }else {
            logger.error("\n	il messaggio non è stato inviato perchè l'id non è presente");

        }
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
    

    private void delateEmitter(List<String> emittersToBeDeleted) {
    	logger.info("emitters prima della cancellazione: {}", emitters);
    	for (String id : emittersToBeDeleted) {
            //qui uso la rimozione, perchè non sono in grado di rilevare quando il client non è più connesso al mio emettitore
            emitters.remove(id);
        }
    	logger.info("emitters dopo della cancellazione: {}", emitters);

	}





    	

	
	
	
}
