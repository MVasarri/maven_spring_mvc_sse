<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html>
<head>
<title>Listen Server</title>
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

	<h1>Sse1 e Ss2 client</h1>
	<div class="grid-container">
		  <div id="sseM1"></div>
		  <div id="sseM2"></div> 
	</div>
	<script type="text/javascript">
	console.log("prova");
		
 		const eventSource = new EventSource('http://localhost:8080/maven_spring_mvc_sse/sse1');
 		
		eventSource.addEventListener("open", (event) => {
		  		var el = document.getElementById('sseM1');
			    el.appendChild(document.createTextNode('connection is live'));
			    el.appendChild(document.createElement('br'));
			   	console.log('connection is live');
		});
 		
 		eventSource.addEventListener("message", (event) => {
				const message = event.data;//JSON.parse(event.data);
		  		console.log(message); 
		  		var el = document.getElementById('sseM1');
			    el.appendChild(document.createTextNode(message + "***"));
			    el.appendChild(document.createElement('br'));
		   		
		}) 
	
		eventSource.addEventListener("error", (event) => {
				var el = document.getElementById('sseM1');
				if (event.readyState == EventSource.CLOSED) {
				  	el.appendChild(document.createTextNode('connection is closed'));
				  	el.appendChild(document.createElement('br'));
				   	console.log('connection is closed');
				} else {
				 	el.appendChild(document.createTextNode("Error occured: " + event));
				 	el.appendChild(document.createElement('br'));
				   	console.log("Error occured", event);
				}
				event.target.close();
		});
		
		
 		const eventSource2 = new EventSource('http://localhost:8080/maven_spring_mvc_sse/sse2');
		
		eventSource2.addEventListener("open", (event) => {
	  		var el = document.getElementById('sseM2');
		    el.appendChild(document.createTextNode('connection is live'));
		    el.appendChild(document.createElement('br'));
		   	console.log('connection is live');
		});
 		
		eventSource2.addEventListener("customEventName", (event)=> {
				var id = event.lastEventId;
				var message = event.data;//JSON.parse(event.data);
				console.log("Message id is " + id);
				console.log(message); 
				var el = document.getElementById('sseM2');
			  	el.appendChild(document.createTextNode("Message id: " + event.lastEventId + " " + message ));
			  	el.appendChild(document.createElement('br'));		     
		 })
		 
		eventSource2.addEventListener("error", (event) => {
				var el = document.getElementById('sseM2');
				if (event.readyState == EventSource.CLOSED) {
				  	el.appendChild(document.createTextNode('connection is closed'));
				  	el.appendChild(document.createElement('br'));
				   	console.log('connection is closed');
				} else {
				 	el.appendChild(document.createTextNode("Error occured: " + event));
				 	el.appendChild(document.createElement('br'));
				   	console.log("Error occured", event);
				}
				event.target.close();
		});
	 
  
	</script> 
</body>
</html>