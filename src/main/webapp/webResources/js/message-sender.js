function sendJSON_All(typeEv, repeat, nRepeat) {
	var title = document.getElementById("sTitle").value;
	var text = document.getElementById("sText").value;
	const d = new Date();
	var sendingTime = d.toLocaleString() + "." + d.getMilliseconds();
/*	if (title === '' && text === '') {
		title = "JSON News All sub - Default";
		text = "Notizia di Default  è gestita mediante l\' invio di un messaggio di tipo JSON";
	};*/
	if(repeat){
		console.log('sono entrato, repeat: ');
	    text = text + '  clientRip N°: ' + nRepeat;
	}
	console.log(title);
	console.log(text);
	const data = { title, text, sendingTime};
	const options = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	};
	var url = '/maven-spring-mvc-sse/' + typeEv;
	console.log(url);
	//	'/maven-spring-mvc-sse/dispatchEventJSON'
	const response = fetch(url, options);
};

function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
};

async function send100JSON_All(typeEv) {
	for (let i = 0; i < 100; i++) {
		await sleep(800).then(sendJSON_All(typeEv, true, i));
	};
};
		  			
