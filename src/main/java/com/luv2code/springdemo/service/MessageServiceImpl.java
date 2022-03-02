package com.luv2code.springdemo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springdemo.DAO.MessageDAO;
import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.entity.Client;
import com.luv2code.springdemo.entity.Message;

@Service
public class MessageServiceImpl implements MessageService {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	// need to inject message DAO
	@Autowired
	private MessageDAO messageDAO;
	// need to inject client Service
	@Autowired
	private ClientService clientService;
	/**
	 * creiamo una Mappa dove immagazzinare le sottoscrizioni dei client-ricevitori
	 * all'evento con una stringa che fa da ID , in modo che si possa mandare altri
	 * eventi ai subscribers mediante l'ID o inviando a tutti
	 */
	public Map<String, SseEmitter> emitters = new HashMap<>();

	@Override
	@Transactional
	public SseEmitter subscribe(String userID, Integer prevMsgID) {
		/**
		 * creiamo l'oggetto sse, ci inserisco un time out molto grande, ma può essere
		 * impostato come si vuole e l'oggetto sseEmitter gestira gli errori di timeOut
		 */
		// Long.MAX_VALUE, (long) 100000, 10_000L
		SseEmitter sseEmitter;
		sseEmitter = new SseEmitter(Long.MAX_VALUE);

		Client client = clientService.getClient(userID);
		if (client == null) {
			Client subClient = new Client();
			subClient.setClientID(userID);
		    LocalDateTime myDateObj = LocalDateTime.now();
		    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss.ns");
		    String formattedDate = myDateObj.format(myFormatObj);	
			subClient.setLastConnection(formattedDate);
			clientService.saveClient(subClient);
		} else {
			client.setLastConnection(LocalDateTime.now().toString());
			clientService.saveClient(client);
		}

		emitters.put(userID, sseEmitter);

		Integer lastDBMsgID = messageDAO.getLastID().intValue();
		logger.info("clientSub: {}, lastDBMsgID: {} , prevMsgID {}", userID, lastDBMsgID, prevMsgID);

//      chiedere al DB i messaggi da spedire, quelli con lastDBMsgID > prevMsgID
		if (lastDBMsgID != 0 && lastDBMsgID > prevMsgID) {
			Integer nMsgLost = messageDAO.countMsgLost(prevMsgID.longValue()).intValue();
			logger.warn("clientSub: {} Ha perso {} messaggi", userID, nMsgLost);
			recoverMessage(prevMsgID, userID, emitters.get(userID));
		}
		logger.info("Client Subscriber salvato in un oggetto SseEmitter: {} id: {} ", sseEmitter, userID);
		logger.info("Mappa dei oggetti SseEmitter' \n {}", emitters);

		/**
		 * l'evento onCompletion fa in modo che l'ascoltatore non venga rimosso una
		 * volta fatta la prima esecuzione, ma lo mantiene attivo nell'elenco fino
		 * all'arrivo di una richiesta di ciusura da parte del client
		 */
		sseEmitter.onCompletion(() -> {
			emitters.remove(userID);
			logger.debug("stampa la lista di Sse, dopo la rimozione degli eventi conclusi con: onCompletion' \n {}",
					emitters);
		});

		sseEmitter.onError((e) -> {
			emitters.remove(userID);
			logger.warn("stampa la lista di Sse, dopo la rimozione degli eventi conclusi: onError' \n {}", emitters);
		});
		return sseEmitter;
	}

	// @RequestBody MessageEntityModel article riceve i dati in formato JSON e li va
	// a mettere nell'oggetto MessageEntityModel
	@Override
	public void dispatchEventJSON(String JSON, Long messageID) {
		List<String> emittersToBeDeleted = new CopyOnWriteArrayList<>();
		// scorro l'elenco dove sono memorizzati i miei diversi clienti
		for (String id : emitters.keySet()) {
			SseEmitter emitter = emitters.get(id);
			try {
				logger.debug("sseEmitter event 'latestMsg': {}, al client {}", JSON, id);
				// inviero il mio evento latestMsg con all'interno il messaggio ad ogni client
				// presente nella lista
				emitter.send(
						SseEmitter.event().name("latestMsg").data(JSON).reconnectTime(1000).id(messageID.toString()));
				logger.debug("sseEmitter event 'latestMsg': {}, al client {} - INVIATA CORRETTAMENTE", JSON, id);
			} catch (IOException e) {
				logger.error("sseEmitter event 'latestMsg': {}, al client {} - ERRORE NELL'INVIO \nerrore: ", JSON, id,
						e);
				// salvo l'id sulla lista dei eventi da cancellarte
				emittersToBeDeleted.add(id);
			}
		}
		// lacio una funzione che cancella elementi da una lista che indica gli eventi
		// da cancellarte
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

	private void recoverMessage(Integer prevMsgID, String userID, SseEmitter sseEmitter) {
		/**
		 * sseEmiter.event richiede una catch per gestire eccezioni di tipo IO la
		 * mappatura per convertirlo da json ad oggetto non è necessaria perche la
		 * prendo dalla lista che già è un oggetto
		 */
		String nDBMsg = messageDAO.countDBMsg().toString();
		for (Message message : messageDAO.getRecoverMessages(prevMsgID.longValue())) {
			String strMessageID = message.getMessageID().toString();
			try {
				logger.debug("sseEmitter event 'latestMsg': {}, al client {}", strMessageID, userID);

				// invia un evento di inizializzazione ai client
				sseEmitter.send(SseEmitter.event().name("latestMsg").data(message).reconnectTime(1000).id(nDBMsg));
				logger.debug("sseEmitter event 'latestMsg': {}, al client {} - INVIATA CORRETTAMENTE", strMessageID,
						userID);
			} catch (IOException e) {
				logger.error("sseEmitter event 'latestMsg': {}, al client {} - ERRORE NELL'INVIO \nerrore: ",
						strMessageID, userID, e);
				emitters.remove(userID);
				break;
			}
		}
	}

	private void delateEmitter(List<String> emittersToBeDeleted) {
		logger.info("Lista emitters prima della cancellazione: {}", emitters);
		logger.info("Lista emitters da cancellare: {}", emittersToBeDeleted);
		for (String id : emittersToBeDeleted) {
			// qui uso la rimozione, perchè non sono in grado di rilevare quando il client
			// non è più connesso al mio emettitore
			emitters.remove(id);
		}
		logger.info("Lista emitters dopo la cancellazione: {}", emitters);
	}

	@Override
	@Transactional
	public String saveAndGetJSON(Message message) throws JsonProcessingException {
		messageDAO.saveMessage(message);
		ObjectMapper mapper = new ObjectMapper();
		String messageString = mapper.writeValueAsString(message);
		logger.debug(
				"dispatchEventJSON- DEBUG-01- stampa message convertito da MessageEntityModel in Stringa \n message: {}",
				messageString);
		return messageString;
	}

}
