var nMsg = 0;
var prevMsgID = 0;

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

var staCon = function statoConnesssione(test) {
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
	url = '/maven-spring-mvc-sse/subscribe?userID=' + userID + '&prevMsgID=' + prevMsgID;
	eventSource = new EventSource(url); //creo un evento per inscriversi al Sse
	// se andiamo a controllare sul browser vedremo che subscription è un "text/event-stream"
	console.log(eventSource);
	staCon(eventSource.readyState);

	eventSource.onopen = (event) => {
		staCon(eventSource.readyState);
		reconnectFrequencySeconds = 1;
		console.log("Evento Open ricevuto, evento open ricevuto senza implementazione sul server :", event);
	};

	//creo un evento ascoltatore "latestNews" che ascoltera l'evento con il medesimo ordine del server
	eventSource.addEventListener("latestMsg", (event) => {
		nMsg += 1;
		console.log("Ricevuto evento", event);
		staCon(eventSource.readyState);
		const messageData = JSON.parse(event.data);
		const nDBMsg = parseInt(event.lastEventId);
		//const messageData = event.data;
		console.log("ultimo id evento ricevuto", event.lastEventId);

		console.log(messageData);
		var el = document.getElementById('ttlMsg').innerHTML = messageData.title;
		el = document.getElementById('txtMsg').innerHTML = messageData.text;
		el = document.getElementById('idMsg').innerHTML = messageData.messageID;
		el = document.getElementById('sTime').innerHTML = messageData.sendingTime;
		el = document.getElementById('mPrev').innerHTML = prevMsgID;
		el = document.getElementById('nMsg').innerHTML =  nMsg;
		el = document.getElementById('nDBMsg').innerHTML = nDBMsg;
		prevMsgID = messageData.messageID;

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

};

setupEventSource(staCon);

// onbeforeunload cioè quando la finestra viene scaricata chiude l'eventSources
window.addEventListener("beforeunload", function(event) {
	console.log("on before unload");
	eventSource.close();
	fetch('/maven-spring-mvc-sse/unsubscribe?userID=' + userID);
	console.log('unsubscribe lanciata');
});
