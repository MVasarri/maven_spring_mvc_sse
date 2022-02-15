<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
			<h1>Invia News</h1>	
			<div>	
				<form >
					  <label for="ltitle">Titolo Articolo:</label><br>
					  <input type="text" id="ltitle" name="ltitle" value=""><br>
					  <label for="ltext">Testo Articolo:</label><br>
					  <input type="text" id="ltext" name="ltext" value=""><br><br>
						<div>
							<input type="button" value="sendJSON_All"
								   onclick= "sendJSON_All('dispatchEventJSON')"
								   class="add-button"
							/>
							<input type="button" value="send100JSON_All"
								   onclick= "send100JSON_All('dispatchEventJSON')"
								   class="add-button"
							/>	
							<input type="button" value="repNews100JSON"
								   onclick= "sendJSON_All('dispatch100EventJSON')"
								   class="add-button"
							/>						
						</div>	
				</form> 
			</div>

			
		<script type="text/javascript">

		//	async function  sendJSON_All() {
			function  sendJSON_All(typeEv) {
				var messageID = '###'; 
				var title = document.getElementById("ltitle").value;
		 		 var text = document.getElementById("ltext").value;
   				 if (title ==='' && text ===''){
   					title = "JSON News All sub - Default";
   					text = "Notizia di Default  è gestita mediante l\' invio di un messaggio di tipo JSON";
   				 }
  				var userID = 'Destinataro/i: All';
				console.log(title);
				console.log(text);
				const data = { messageID, title, text, userID};
				const options = {
						method : 'POST',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify(data)
				};	
				var url = '/maven-spring-mvc-sse/' + typeEv;
				console.log(url);
				//	'/maven-spring-mvc-sse/dispatchEventJSON'
				const response = fetch(url, options);
/* 
				const response = await fetch('/maven-spring-mvc-sse/dispatchEvent2', options);
				console.log("stampa qualcosa? " + await responce.status); 
*/
  			}    

	 
	   		function sleep(ms) {
	   			  return new Promise(resolve => setTimeout(resolve, ms));
	   		}
	   			
	   		async function send100JSON_All(typeEv) {
	   			for (let i = 0; i < 100; i++) {
	   				await sleep(800).then(sendJSON_All(typeEv));
	   			}; 				
	   		}
	   		async function send100JSON_ByID() {
	   			for (let i = 0; i < 100; i++) {
	   				await sleep(800).then(sendJSON_ByID());
	   			};
	   		}			  			
/*		 
			function sendJSON(){		
			 	
			 	const title = "messaggio 1";
			 	const text =  "vediamo de questo messaggio va";
			 
	            // Creating a XHR object
	            var xhr = new XMLHttpRequest();
	            var url = '/maven-spring-mvc-sse/dispatchEventJSON';
	       
	            // open a connection
	            xhr.open("POST", url, true);
	 
	            // Set the request header i.e. which type of content you are sending
	            xhr.setRequestHeader("Content-Type", "application/json");
	 
	            // Create a state change callback
	            xhr.onreadystatechange = function () {
	                if (xhr.readyState === 4 && xhr.status === 200) {
	                    // Print received data from server
	                    //result.innerHTML = this.responseText;
	                    console.log("inviato: " + this.status)
	                    console.log(xhr.responseText);
	                }
	            };
	 
	            // Converting JSON data to string
	            var data = { "title": "messaggio 1", "text": "vediamo de questo messaggio va" };
	 
	            // Sending data with the request
	            xhr.send(JSON.stringify(data));
	            //xhr.send(data);
	        }  
*/

		</script>
		
		
</body>
</html>