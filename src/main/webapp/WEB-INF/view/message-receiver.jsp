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
        <h1>News sse client</h1>
        <h4>User ID: <span id="userID"></span></h4>
    </div>

    <div class="grid-container">
        <div id="sseNews1">
        	<h6 id="ttlNews"></h6>
        	<p id="txtNews"></p>
        	<p id="idNews"></p>
        	<p id="nNews"></p>
        	<p id="lastNews"></p>
        	<p id="mLost"></p>
        	
        
        </div> 
    </div>

    <script type="text/javascript">
    
   		var nNews = 1;

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
			url = '/maven-spring-mvc-sse/subscribe?userID=' + userID + '&nNews=' + nNews;
			eventSource = new EventSource(url); //creo un evento per inscriversi al Sse
													// se andiamo a controllare sul browser vedremo che subscription è un "text/event-stream"
			console.log(eventSource);
			staCon(eventSource.readyState);
				
			eventSource.addEventListener("INIT", (event) => {
			    console.log("Evento INIT ricevuto, connessione con il server avviata:", event);
			});
				
			eventSource.onopen = (event) => {
				staCon(eventSource.readyState);
			    reconnectFrequencySeconds = 1;
			    console.log("Evento Open ricevuto, evento open ricevuto senza implementazione sul server :", event);
			};
							
			//creo un evento ascoltatore "latestNews" che ascoltera l'evento con il medesimo ordine del server
			eventSource.addEventListener("latestNews", (event) => {
				console.log("Ricevuto evento", event);
			    staCon(eventSource.readyState);
			    const articleData = JSON.parse(event.data);
			    //const articleData = event.data;
				console.log("ultimo id evento ricevuto", event.lastEventId);
			    
			    console.log(articleData);
			    var el = document.getElementById('ttlNews').innerHTML = articleData.title;
			    var el = document.getElementById('txtNews').innerHTML = articleData.text;							    
			    //var el = document.getElementById('idNews').innerHTML = articleData.userID;
			    var el = document.getElementById('nNews').innerHTML = "ID messaggio aspettato: " + nNews;
			    var el = document.getElementById('lastNews').innerHTML = "ID messaggio ricevuto: " + parseInt(event.lastEventId);
		    	var mLost = (parseInt(event.lastEventId) - nNews);
		    	var el = document.getElementById('mLost').innerHTML = "Sono stati perzi "+ mLost+ " messaggi";

			    nNews = nNews +1;
/* 							    
				var el = document.getElementById('sseNews1');
			    el.appendChild(document.createTextNode(articleData.title));
			    el.appendChild(document.createElement('br'));
			    el.appendChild(document.createTextNode(articleData.text));
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