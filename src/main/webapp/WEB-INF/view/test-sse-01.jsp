<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html>
<head>
<title>Listen Server</title>
</head>
<body>

	<h1>Sse client</h1>
	<div id="sse"></div>
	<div id="testOpen"></div>
	<script>
	console.log("prova");
		eventSource.addEventListener("message", (event) => {
		  		const message = event.data;//JSON.parse(event.data);
		  		console.log(message); 
		  		var el = document.getElementById('sse');
			    el.appendChild(document.createTextNode(message));
			    el.appendChild(document.createElement('br'));
		   		
		}) 
	
		eventSource.onpen = function () {
	  		var el = document.getElementById('testOpen');
		    el.appendChild(document.createTextNode('connection is live'));
		    el.appendChild(document.createElement('br'));
	   		console.log('connection is live');
		};
	
/*  		var sse = new EventSource('http://localhost:8080/spring-security-demo/sse');
			sse.onmessage = function (evt) {
			    var el = document.getElementById('sse');
			    el.appendChild(document.createTextNode(evt.data));
			    el.appendChild(document.createElement('br'));
			};  */
	</script> 
</body>
</html>