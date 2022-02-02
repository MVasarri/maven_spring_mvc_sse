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
            <div id="sseNews1"></div> 
        </div>

        <script type="text/javascript">

				var userID = Math.floor((Math.random() * 1000) + 1);
				var sp1 = document.getElementById('userID');
				sp1.textContent = userID;
				
				//url dove va ad ascoltare l'evento SseEmitter
				console.log("SUBSCRIBE");
				const url = '/maven-spring-mvc-sse/subscribe?userID=' + userID;
				//creo un evento per iscriversi al Sse
				const eventSource = new EventSource(url); // se andiamo a controllare sul browser vedremo che subscription è un "text/event-stream"
				console.log(eventSource);
				
				//creo un evento ascoltatore "latestNews" che ascoltera l'evento con il medesimo ordine del server
				eventSource.addEventListener("latestNews", (event) => {
				    console.log("Ricevuto evento", event);
				    const articleData = JSON.parse(event.data);
				    //const articleData = event.data;
				    console.log(articleData);
				    var el = document.getElementById('sseNews1');
				    el.appendChild(document.createTextNode(articleData.title));
				    el.appendChild(document.createElement('br'));
				    el.appendChild(document.createTextNode(articleData.text));
				    el.appendChild(document.createElement('br'));
				    el.appendChild(document.createElement('br'));
				});
				
				//eventSource.onerror = (event) => {
				eventSource.addEventListener("error", (event) => {
					console.log("Error occured: ", event);
					console.log("EventSource.CLOSED: ", EventSource.CLOSED);
					console.log("event.target.readyState: ", event.target.readyState);
					console.log("STATO eventSource: ", eventSource.readyState);
					if (event.target.readyState == EventSource.CLOSED) {
					   	console.log('connection is closed');
					} else {
						console.log("STATO eventSource: ", eventSource.readyState);
					   	//è il punto in cui riconosce che la connessione del client è stata persa
					   	// TO-DO:
					   	// 			gestire la riconnessione automatica
					}
					event.target.close();
				});
				
				// puoi aggiungere anche alcuni eventi quando ci sono errori puoi chiudere semplicemente l'eventSources
				
				// onbeforeunload cioè quando la finestra viene scaricata chiude l'eventSources
				window.addEventListener("beforeunload", function(event) { 
					    console.log("on before unload");
					    eventSource.close();
					    fetch( '/unsubscribe?userID=' + userID );
						console.log('unsubscribe lanciata');
				});
				
				// addEventListener version
				window.addEventListener('online', (event) => {
				    console.log("You are now connected to the network.");
/* 				    console.log("event: ",event);
					if (eventSource.readyState == EventSource.CLOSED) {
					    const eventSource = new EventSource(url); 
						console.log('ricreata nuova connessione eventSource');
					} */
				});

        </script>


    </body>
</html>