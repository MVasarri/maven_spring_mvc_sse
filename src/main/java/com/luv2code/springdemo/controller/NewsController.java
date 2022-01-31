package com.luv2code.springdemo.controller;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger
            = LoggerFactory.getLogger(HomeController.class);

    //creiamo una lista dove immagazzinare le sottoscrizioni dei client all'evento, in modo che si possa mandare altri eventi al clients
    //CopyOnWriteArrayList<> Una variante thread-safe di {@link java.util.ArrayList} utile quando non puoi o non vuoi sincronizzare gli attraversamenti, ma devi precludere le interferenze tra thread simultanei.l'interferenza � impossibile e l'iteratore � garantito per non generare {@code ConcurrentModificationException}.utile quindi er gestire molti abbonati
    //public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    public Map<String, SseEmitter> emitters = new HashMap<>();

    // method for client subscription
    //creiamo un endpoint di sottoscrizione che restituira l'oggetto SseEmitter
    //Questa annotazione @CrossOrigin abilita la condivisione di risorse tra origini solo per questo metodo specifico.
    //Per impostazione predefinita, consente tutte le origini, tutte le intestazioni e i metodi HTTP specificati 
    //nell'annotazione @RequestMapping.
    @CrossOrigin
    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam String userID) {

        //creiamo l'oggetto sse, ci inserisco un time out molto grande, ma pu� essere impostato come si vuole e l'oggetto sseEmitter gestira gli errori di timeOut
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sendinitEvent(sseEmitter);

        sseEmitter.complete();

        logger.debug("stampa l'oggetto Sse' \n {}", sseEmitter);
        logger.debug("stampa la lista di Sse, prima della rimozione' \n {}", emitters);
        //memorizzo l'emettitore, che � solo un riferimento all'oggetto che mantiente la connessione con i client in modo che passiamo, in modo che possiamo usarlo in seguito e inviare altri eventi ai miei client
        //emitters.add(sseEmitter);
        emitters.put(userID, sseEmitter);
        //l'evento onCompletion fa in modo che l'ascoltatore non venga rimosso una volta fatta la prima esecuzione, ma lo mantiene attivo nell'elenco fino all'arrivo di una richiesta di ciusura da parte del client

        sseEmitter.onCompletion(() -> {
            emitters.remove(sseEmitter);
            logger.debug("stampa la lista di Sse, dopo la rimozione onCompletion' \n {}", emitters);
        });

        sseEmitter.onTimeout(() -> {
            emitters.remove(sseEmitter);
            logger.debug("stampa la lista di Sse, dopo la rimozione onTimeout' \n {}", emitters);
        });
        sseEmitter.onError((e) -> {
            emitters.remove(sseEmitter);
            logger.debug("stampa la lista di Sse, dopo la rimozione onError' \n {}", emitters);
        });

        return sseEmitter;
    }

    // method for dispatching events to all clients
    @PostMapping(value = "/dispatchEvent")
    public void dispatchEventToClients(@RequestParam String title, @RequestParam String text) {
        logger.debug("dispatchEvent- DEBUG-00- stampa l'articolo che invier� all'evento 'latestNews' che ricevo dalla post prima di riformattarlo in JSON\n title: {}\n paragrafo: {}", title, text);

        //trasformo le stringhe title & text in formato JSON
        String eventFormatted = new JSONObject()
                .put("title", title)
                .put("text", text).toString();
        logger.debug("dispatchEvent- DEBUG-01-  stampa l'articolo che invier� all'evento 'latestNews' che ricevo dalla post dopo averlo riformattarlo in JSON\n articolo: {}", eventFormatted);

        for (SseEmitter emitter : emitters.values()) {
            try {
                emitter.send(SseEmitter.event().name("latestNews").data(eventFormatted));
            } catch (IOException e) {
                logger.warn("si � verificato sulla sseEmitter durante nella spedizione dell'evento 'latestNews' \n {}", e);
                //qui uso la rimozione, perch� non sono in grado di rilevare quando il client non � pi� connesso al mio emettitore
                emitters.remove(emitter);
            }
        }
    }
    // method for dispatching events to a Specific User

    @PostMapping(value = "/dispatchEventToSpecificUser")
    public void dispatchEventToSpecificUser(@RequestParam String title, @RequestParam String text, @RequestParam String userID) {
        logger.debug("dispatchEvent- DEBUG-00- stampa l'articolo che invier� all'evento 'latestNews' che ricevo dalla post prima di riformattarlo in JSON\n title: {}\n paragrafo: {}", title, text);

        //trasformo le stringhe title & text in formato JSON
        String eventFormatted = new JSONObject()
                .put("title", title)
                .put("text", text).toString();
        logger.debug("dispatchEvent- DEBUG-01-  stampa l'articolo che invier� all'evento 'latestNews' che ricevo dalla post dopo averlo riformattarlo in JSON\n articolo: {}", eventFormatted);

        SseEmitter sseEmitter = emitters.get(userID);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("latestNews").data(eventFormatted));
            } catch (IOException e) {
                logger.warn("si � verificato sulla sseEmitter durante nella spedizione dell'evento 'latestNews' \n {}", e);
                //qui uso la rimozione, perch� non sono in grado di rilevare quando il client non � pi� connesso al mio emettitore
                emitters.remove(sseEmitter);
            }
        }
    }

    // method for dispatching events to all clients
    @PostMapping(value = "/dispatchEvent2", consumes = "application/json")
    public void dispatchEvent2ToClients(@RequestBody ArticleModel article) throws Exception {
        logger.debug("dispatchEvent2- DEBUG-00- stampa l'articolo che invier� all'evento 'latestNews' nel formato JSON che ricevo dalla post dopo che � stato mappato come oggetto Articolo dal RequestBody \n title: {}\n paragrafo: {}", article);

        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(article);
        logger.debug("dispatchEvent2- DEBUG-01- stampa l'articolo che invier� all'evento 'latestNews' dopo aver riconvertito l'articolo da oggetto a stringa da Jeckson\n title: {}\n paragrafo: {}", article);

        //scorro l'elenco dove sono memorizzati i miei diversi clienti
        for (SseEmitter emitter : emitters.values()) {
            try {
                System.out.println("hello " + message);
                //inviero il mio evento latestNews con all'interno l'articolo ad ogni client presente nella lista
                //To do Analizzare questo elenco per verifichare chi � tra questi ancora aperto e togliere chi non � pi� in ascolto
                emitter.send(SseEmitter.event().name("latestNews").data(message));
            } catch (IOException e) {
                logger.warn("si � verificato sulla sseEmitter durante nella spedizione dell'evento 'latestNews' \n {}", e);
                //in caso di errore di IOException rimoviamo emettitore
                emitters.remove(emitter);
            }
        }
    }

    private void sendinitEvent(SseEmitter sseEmitter) {
        //sseEmiter.event richiede una catch per gestire eccezioni di tipo IO
        try {
            //invia un evento di inizializzazione ai client
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            logger.warn("si � verificato sulla sseEmitter durante nella spedizione dell'evento INIT \n {}", e);
            emitters.remove(emitters);
        }
    }

}
