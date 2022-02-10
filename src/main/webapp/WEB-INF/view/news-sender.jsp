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
					  <label for="lid">Id Destinatario:</label><br>
					  <input type="text" id="lid" name="lid" value=""><br>
					  <div>
	  					  <input type="button" value="sendNewsAll"
										   onclick= "sendNewsAll()"
										   class="add-button"
						  />
  	  					  <input type="button" value="send100NewsAll"
										   onclick= "send100NewsAll()"
										   class="add-button"
						  />
					  </div>

						<div>							
							<input type="button" value="sendNewsByID"
								   onclick= "sendNewsByID()"
								   class="add-button"
							/>
							<input type="button" value="send100NewsByID"
								   onclick= "send100NewsByID()"
								   class="add-button"
							/>							
						</div>
						<div>
							<input type="button" value="sendJSON_All"
								   onclick= "sendJSON_All()"
								   class="add-button"
							/>
							<input type="button" value="send100JSON_All"
								   onclick= "send100JSON_All()"
								   class="add-button"
							/>							
						</div>
						<div>
							<input type="button" value="sendJSON_ByID"
								   onclick= "sendJSON_ByID()"
								   class="add-button"
							/>
							<input type="button" value="send100JSON_ByID"
								   onclick= "send100JSON_ByID()"
								   class="add-button"
							/>							
						</div>
	
				</form> 
			</div>

			
		<script type="text/javascript">

		//	async function  sendJSON_All() {
			function  sendJSON_All() {
		 		 var title = document.getElementById("ltitle").value;
		 		 var text = document.getElementById("ltext").value;
   				 if (title ==='' && text ===''){
   					title = "JSON News All sub - Default";
   					text = "Notizia di Default  è gestita mediante l\' invio di un messaggio di tipo JSON";
   				 }
  				var userID = 'Destinataro/i: All';
				console.log(title);
				console.log(text);
				const data = { title, text, userID};
				const options = {
						method : 'POST',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify(data)
				};	
				const response = fetch('/maven-spring-mvc-sse/dispatchEvent2', options);

/* 				const response = await fetch('/maven-spring-mvc-sse/dispatchEvent2', options);
				console.log("stampa qualcosa? " + await responce.status); */
  			 }   
  			 
  			// async function  sendJSON_ByID() {
  			function  sendJSON_ByID() {
				 var userID = document.getElementById("lid").value;
		 		 var title = document.getElementById("ltitle").value;
		 		 var text = document.getElementById("ltext").value;
   				 if (title ==='' && text ===''){
   					title = "JSON News All sub - Default";
   					text = "Notizia di Default  è gestita mediante l\' invio di un messaggio di tipo JSON";
   				 }
				const data = { title, text, userID};
				console.log("data:\n" + data);
				const options = {
						method : 'POST',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify(data)
				};	
				fetch('/maven-spring-mvc-sse/dispatchEvent2ToSpecificUser', options).then(function(response) {console.log(response.status);});
/* 				var response = await fetch('/maven-spring-mvc-sse/dispatchEvent2ToSpecificUser', options);
				console.log("stampa qualcosa? " + await responce.status); */
			} 

			 
   			 function sendNewsAll(){
		 		 var title = document.getElementById("ltitle").value;
				 var text = document.getElementById("ltext").value;
   				 if (title ==='' && text ===''){
   					title = "News AllSub tipo 1- Default";
   					text = "Notizia di default è gestita con il Content-Type: application/x-www-form-urlencoded";
   				 }
				console.log(title);
				console.log(text);
				 var xhr = new XMLHttpRequest();
				 xhr.open("POST", '/maven-spring-mvc-sse/dispatchEvent', true);
	
				 //Send the proper header information along with the request
				 xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	
				 xhr.onreadystatechange = function() { // Call a function when the state changes.
				     if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
				         console.log("inviato: " + this.status)
				     }
				 }
				 xhr.send("title=" + title + "&text=" + text);
				 // xhr.send(new Int8Array());
				 // xhr.send(document);
			 }  
		     
   			 function sendNewsByID(){
				 var subID = document.getElementById("lid").value;
		 		 var title = document.getElementById("ltitle").value;
				 var text = document.getElementById("ltext").value;
   				 if (title =='' && text ==''){
   					title = "News Id - Default";
   					text = "Notizia di Default che arrivera ad uno specifico utente gestita con il Content-Type: application/x-www-form-urlencoded";
   				 }
				console.log(subID);
				console.log(title);
				console.log(text);
				 var xhr = new XMLHttpRequest();
				 xhr.open("POST", '/maven-spring-mvc-sse/dispatchEventToSpecificUser', true);
	
				 //Send the proper header information along with the request
				 xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	
				 xhr.onreadystatechange = function() { // Call a function when the state changes.
				     if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
				         console.log("inviato: " + this.status)
				     }
				 }
				 xhr.send("title=" + title + "&text=" + text + "&userID="+ subID);
				 // xhr.send(new Int8Array());
				 // xhr.send(document);
			 }  
   			 
   			function  send100JSON_All() {
   				for (let i = 0; i < 100; i++) {
   					delay(1000);
   					sendJSON_All();
   				}; 				
   			}
   			function  send100JSON_ByID() {
   				for (let i = 0; i < 100; i++) {
   					delay(1000);
   					sendJSON_ByID();
   				};
   			}
   			function send100NewsAll(){
   				for (let i = 0; i < 100; i++) {
   					delay(1000);
   					sendNewsAll();
   				};
   			}
   			function send100NewsByID(){
   				for (let i = 0; i < 100; i++) {
   					delay(1000);
   					sendNewsByID();
   				};
   			}
   			
   			
   			
/*		 
		function sendJSON(){		
		 	
		 	const title = "messaggio 1";
		 	const text =  "vediamo de questo messaggio va";
		 
            // Creating a XHR object
            var xhr = new XMLHttpRequest();
            var url = '/maven-spring-mvc-sse/dispatchEvent2';
       
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