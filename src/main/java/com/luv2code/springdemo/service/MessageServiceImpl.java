package com.luv2code.springdemo.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springdemo.DAO.MessageDAO;
import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.entity.Client;
import com.luv2code.springdemo.entity.Message;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    // need to inject message DAO
    @Autowired
    private MessageDAO messageDAO;
    
    // need to inject client Service
    @Autowired
    private ClientService clientService;
    
    

    //creiamo una Mappa dove immagazzinare le sottoscrizioni dei client-ricevitori all'evento con una stringa che fa da ID , in modo che si possa mandare altri eventi ai subscribers mediante l'ID o inviando a tutti
    public Map<String, SseEmitter> emitters = new HashMap<>();

    //public Map<String, Integer> lastMessageSend = new HashMap<>();  
//    private final AtomicInteger IDmessage = new AtomicInteger(); 
//    List<MessageEntityModel> messageList = new CopyOnWriteArrayList<>();
// 
    @Override
    public SseEmitter subscribe(String userID, Integer nMsg) {
        //creiamo l'oggetto sse, ci inserisco un time out molto grande, ma può essere impostato come si vuole e l'oggetto sseEmitter gestira gli errori di timeOut
        //Long.MAX_VALUE, (long) 100000,   10_000L
        SseEmitter sseEmitter;
        sseEmitter = new SseEmitter(Long.MAX_VALUE);
        
        if(clientService.getClient(userID)==null) {
        	Client subClient = new Client();
        	subClient.setClientID(userID);
        	subClient.setLastConnection(new Date().getTime());
        	clientService.saveClient(subClient);
        }else {
            Client subClient = clientService.getClient(userID);
        	subClient.setLastConnection(new Date().getTime());
        	clientService.saveClient(subClient);
        }

        // TODO: controllare se sul DB userID è già presente ovvero se Client getClient(String userID), ritorna qualcosa o no
	        // TODO: salva sul DB l'userID se non è presente
	        // TODO: NON salvare sul DB l'userID se è già presente

        emitters.put(userID, sseEmitter);

        //chiedere al DB i messaggi da spedire, quelli con ID > nNews
        Integer lastMessageID = messageDAO.getLastID().intValue();
        logger.info("lastMessageID: {} , nNews {}", lastMessageID, nMsg);

        if (lastMessageID != 0 && lastMessageID > nMsg) {
            Integer mLost = lastMessageID - nMsg;
            logger.warn("Hai perso {} messaggi", mLost);
            recoverMessage(nMsg, userID, emitters.get(userID));
        }
        logger.info("Client Subscriber salvato in un oggetto SseEmitter: {} id: {} ", sseEmitter, userID);
        logger.info("Mappa dei oggetti SseEmitter' \n {}", emitters);

        //l'evento onCompletion fa in modo che l'ascoltatore non venga rimosso una volta fatta la prima esecuzione, ma lo mantiene attivo nell'elenco fino all'arrivo di una richiesta di ciusura da parte del client
        sseEmitter.onCompletion(() -> {
            emitters.remove(userID);
            logger.debug("stampa la lista di Sse, dopo la rimozione degli eventi conclusi con: onCompletion' \n {}", emitters);
        });
//        sseEmitter.onTimeout(() -> {
//            emitters.remove(userID);
//            logger.debug("subscribers ancora Vivi dopo il Timeout' \n {}", emitters);
//        });
        sseEmitter.onError((e) -> {
            emitters.remove(userID);
            logger.warn("stampa la lista di Sse, dopo la rimozione degli eventi conclusi: onError' \n {}", emitters);
        });
        return sseEmitter;
    }

    //@RequestBody MessageEntityModel article riceve i dati in formato JSON e li va a mettere nell'oggetto MessageEntityModel
    @Override
    public void dispatchEventJSON(@RequestBody Message message) throws Exception {
        // dopo questa istruzione, l'ID sarà valorizzato
    	//message.setMessageID(incrementIDmessage().toString());
        logger.debug("dispatchEventJSON- DEBUG-00- stampa message  nel formato MessageEntityModel, ricevo dalla post \n IDMessaggio: {} \n title: {}\n text: {}", message.getMessageID(), message.getTitle(), message.getText());
        messageDAO.saveMessage(message);
        logger.debug("dispatchEventJSON- DEBUG-01- stampa message tornato dal DB: \n IDMessaggio: {} \n title: {}\n text: {}", message.getMessageID(), message.getTitle(), message.getText());
        //occore Jeckson per fare questa mappatura
        ObjectMapper mapper = new ObjectMapper();
        String messageString = mapper.writeValueAsString(message);
        logger.debug("dispatchEventJSON- DEBUG-01- stampa l'articolo convertito da MessageEntityModel in Stringa \n message: {}", messageString);
        
        long lastMessageID = messageDAO.getLastID();
        logger.info("ultimo ID salvato sul DB:\nlastMessageID: {} ", lastMessageID);
        
        List<String> emittersToBeDeleted = new CopyOnWriteArrayList<>();
        //scorro l'elenco dove sono memorizzati i miei diversi clienti
        for (String id : emitters.keySet()) {
            SseEmitter emitter = emitters.get(id);
            String strMessageID = message.getMessageID().toString();
            try {
                logger.debug("messageString: {}", messageString);
                //inviero il mio evento latestNews con all'interno l'articolo ad ogni client presente nella lista
                //To do Analizzare questo elenco per verifichare chi è tra questi ancora aperto e togliere chi non è più in ascolto
                emitter.send(
                        SseEmitter.event()
                                .name("latestMsg")
                                .data(messageString)
                                .reconnectTime(1000)
                                .id(strMessageID)
                );
                logger.debug("Il Server invia al Subscriber ID: {}, evento 'latestMsg' n: {} ", id, strMessageID);
            } catch (IOException e) {
                logger.error("Il Server non è riuscito ad invia al Subscriber ID: {}, evento 'latestMsg' n: {} \nerrore: ", id, strMessageID, e);
                //salvo l'id sulla lista dei eventi da cancellarte
                emittersToBeDeleted.add(id);
            }
        }
        //lacio una funzione che cancella elementi da una lista che indica gli eventi da cancellarte
        if (!emittersToBeDeleted.isEmpty()) {
            delateEmitter(emittersToBeDeleted);
        }
    }

    @Override
    public void unsubscribe(@RequestParam String userID) {
        SseEmitter sseEmitter = emitters.get(userID);
        if (sseEmitter != null) {
            sseEmitter.complete();
            logger.info("Il subscriber sseEmitter {} è stato Completato", userID);
        } else {
            logger.warn("Il subscriber sseEmitter {} Non è Presente", userID);
        }
    }

//    private void sendinitEvent(String userID, SseEmitter sseEmitter) {
//        //sseEmiter.event richiede una catch per gestire eccezioni di tipo IO
//        try {
//            //invia un evento di inizializzazione ai client
//            sseEmitter.send(SseEmitter.event().name("INIT"));
//            logger.info("inizializzazione eseguita con successo del subscriber: {}", userID);
//        } catch (IOException e) {
//            logger.error("si è verificato sulla sseEmitter durante nella spedizione dell'evento INIT \n {}", e);
//            emitters.remove(userID);
//        }
//    }

    //questa tecnica rischia di sovrascrivere messaggi più recenti o di reinviare dei messaggi che in realta aveva già ricevuto
    private void recoverMessage(Integer nMsg, String userID, SseEmitter sseEmitter) {
        //sseEmiter.event richiede una catch per gestire eccezioni di tipo IO
        //la mappatura per convertirlo da json ad oggetto non è necessaria perche la prendo dalla lista cheè gaà un oggetto
        for (Message message : messageDAO.getMessages()) {
            if (message.getMessageID().intValue() >= nMsg) {
                String strMessageID = message.getMessageID().toString();
            	try {
                    //invia un evento di inizializzazione ai client
                    sseEmitter.send(SseEmitter.event()
                            .name("latestMsg")
                            .data(message)
                            .reconnectTime(1000)
                            .id(strMessageID)
                    );
                    logger.info("Il Subscriber {} ha recuperato il messaggio id:{} ", userID, strMessageID);
                } catch (IOException e) {
                    logger.error("Il Server non è riuscito a far recuperare  l'evento 'latestMsg' n: {} al Subscriber ID: {} \nerrore: ", strMessageID, userID, e);
                    emitters.remove(userID);
                    break;
                }
            }
        }
    }

    private void delateEmitter(List<String> emittersToBeDeleted) {
        logger.info("Lista emitters prima della cancellazione: {}", emitters);
        logger.info("Lista emitters da cancellare: {}", emittersToBeDeleted);
        for (String id : emittersToBeDeleted) {
            //qui uso la rimozione, perchè non sono in grado di rilevare quando il client non è più connesso al mio emettitore
            emitters.remove(id);

        }
        logger.info("Lista emitters dopo la cancellazione: {}", emitters);
    }

//    public Integer incrementIDmessage() {
//        return IDmessage.getAndIncrement();
//    }
//
//    public Integer getIDmessage() {
//        return IDmessage.get();
//    }

//    @Override
//    public void saveMessage(MessageEntityModel theMessage) {
//
//        messageDAO.saveMessage(theMessage);
//    }
//
//    @Override
//    public List<MessageEntityModel> getMessage() {
//        return messageDAO.getMessages();
//    }

}
