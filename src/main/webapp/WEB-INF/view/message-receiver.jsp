<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
        <meta charset="ISO-8859-1">
        <title>Insert title here</title>
        <style>
            .grid-container {
                display: grid;
                grid-template-columns: auto auto auto;
                background-color: #2196F3;
                padding: 10px;
            }
        </style>
</head>
<body>		

	<div>
        <h1>Message SSE client Receiver</h1>
        <h4>User ID: <span id="userID"></span></h4>
    </div>

    <div class="grid-container">
        <div id="sseMsg">
        	<h6 id="ttlMsg"></h6>
        	<p id="txtMsg"></p>
        	<p id="idMsg"></p>
        	<p id="nMsg"></p>
        	<p id="lastMsg"></p>
        	<p id="mLost"></p>
        	
        
        </div> 
    </div>

    <script type="text/javascript">
    
   		var nMsg = 1;
   		//var lastIdMsg = null;

		var userID = Math.floor((Math.random() * 1000) + 1);
		var sp1 = document.getElementById('userID');
		sp1.textContent = userID;
		//url dove va ad ascoltare l'evento SseEmitter
		var url = null;

		var eventSource;
		
		var reconnectFrequencySeconds = 1;
		// Putting these functions in extra variables is just for the sake of readability
		var waitFunc = function() { return reconnectFrequencySeconds * 1000 };
		
		var tryToSetupFunc = function() {
		    setupEventSource();
		    reconnectFrequencySeconds *= 2;
		    if (reconnectFrequencySeconds >= 64) {
		        reconnectFrequencySeconds = 64;
		    }
		};	
		
		var reconnectFunc = function() { setTimeout(tryToSetupFunc, waitFunc()) };
		
		var staCon =	function statoConnesssione(test) {
		 	console.log("Stato Connessione");
			switch (test) {
		         case 0:
					 console.log("0 - connecting ti Stai connettendo");
		             break;
		         case 1:
					 console.log("1 - open la connessione aperta");
		             break;
		         case 2:
					 console.log("2 - closed la connessione è chiusa");
		             break;
		         default:
		             throw new IllegalArgumentException("Invalid day of the week: " + dayOfWeekArg);
	     	}
		};
				
		function setupEventSource() {
			console.log("SUBSCRIBE");
			url = '/maven-spring-mvc-sse/subscribe?userID=' + userID + '&nMsg=' + nMsg;
			eventSource = new EventSource(url); //creo un evento per inscriversi al Sse
													// se andiamo a controllare sul browser vedremo che subscription è un "text/event-stream"
			console.log(eventSource);
			staCon(eventSource.readyState);
				
/* 			eventSource.addEventListener("INIT", (event) => {
			    console.log("Evento INIT ricevuto, connessione con il server avviata:", event);
			}); */
				
			eventSource.onopen = (event) => {
				staCon(eventSource.readyState);
			    reconnectFrequencySeconds = 1;
			    console.log("Evento Open ricevuto, evento open ricevuto senza implementazione sul server :", event);
			};
							
			//creo un evento ascoltatore "latestNews" che ascoltera l'evento con il medesimo ordine del server
			eventSource.addEventListener("latestMsg", (event) => {
				console.log("Ricevuto evento", event);
			    staCon(eventSource.readyState);
			    const messageData = JSON.parse(event.data);
			    const messageID = parseInt(event.lastEventId);
			    //const messageData = event.data;
				console.log("ultimo id evento ricevuto", event.lastEventId);
			    
			    console.log(messageData);
			    var el = document.getElementById('ttlMsg').innerHTML = messageData.title;
			    var el = document.getElementById('txtMsg').innerHTML = messageData.text;							    
			    //var el = document.getElementById('idMsg').innerHTML = messageData.userID;
			    var el = document.getElementById('nMsg').innerHTML = "ID messaggio aspettato: " + nMsg;
			    var el = document.getElementById('lastMsg').innerHTML = "ID messaggio ricevuto: " + messageID;
		    	var mLost = (messageID - nMsg);
		    	var el = document.getElementById('mLost').innerHTML = "Sono stati persi "+ mLost+ " messaggi";

		    	nMsg = nMsg +1;

/* 			    if(lastIdMsg != messageID){
			    	lastIdMsg = messageID;
			    	nMsg = nMsg +1;
			    } */
/* 							    
				var el = document.getElementById('sseMsg');
			    el.appendChild(document.createTextNode(messageData.title));
			    el.appendChild(document.createElement('br'));
			    el.appendChild(document.createTextNode(messageData.text));
			    el.appendChild(document.createElement('br'));
			    el.appendChild(document.createTextNode(articleData.userID));
			    el.appendChild(document.createElement('br'));
			    el.appendChild(document.createElement('br')); 
*/
				});
		
			eventSource.addEventListener("error", (event) => {
				console.log("Error occured: ", event);  
				if (eventSource.readyState == EventSource.CLOSED) {
				   	console.log('connection è già closed');
				} else {
					console.log('è successo un qualche altro errore la connessione la chiusa');
				}
				eventSource.close();
			    reconnectFunc();
			});	
							
/* 				
				window.addEventListener('online', (event) => {
					if (eventSource.readyState == EventSource.CLOSED) {
						console.log('ricreata nuova connessione eventSource');
						reconnectFunc();
					}
				}); 
*/
		};		
										
		setupEventSource(staCon);
				
		// puoi aggiungere anche alcuni eventi quando ci sono errori puoi chiudere semplicemente l'eventSources
			
		// onbeforeunload cioè quando la finestra viene scaricata chiude l'eventSources
		window.addEventListener("beforeunload", function(event) { 
		    console.log("on before unload");
		    eventSource.close();
		    fetch( '/maven-spring-mvc-sse/unsubscribe?userID=' + userID );
			console.log('unsubscribe lanciata');
		});

   	</script>


    </body>
</html>