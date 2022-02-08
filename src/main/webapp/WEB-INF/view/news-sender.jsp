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
					  </div>

						<div>							
							<input type="button" value="sendNewsByID"
								   onclick= "sendNewsByID()"
								   class="add-button"
							/>
						</div>
						<div>
							<input type="button" value="sendJSON_All"
								   onclick= "sendJSON_All()"
								   class="add-button"
							/>
						</div>
						<div>
							<input type="button" value="sendJSON_ByID"
								   onclick= "sendJSON_ByID()"
								   class="add-button"
							/>
						</div>
	
				</form> 
			</div>

			
		<script type="text/javascript">

  			 function  sendJSON_All() {
		 		 var title = document.getElementById("ltitle").value;
		 		 var text = document.getElementById("ltext").value;
   				 if (title ==='' && text ===''){
   					title = "JSON News All sub - Default";
   					text = "Notizia di Default  � gestita mediante l\' invio di un messaggio di tipo JSON";
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
			}   
  			 
  			 function  sendJSON_ByID() {
				 var userID = document.getElementById("lid").value;
		 		 var title = document.getElementById("ltitle").value;
		 		 var text = document.getElementById("ltext").value;
   				 if (title ==='' && text ===''){
   					title = "JSON News All sub - Default";
   					text = "Notizia di Default  � gestita mediante l\' invio di un messaggio di tipo JSON";
   				 }
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
				const response = fetch('/maven-spring-mvc-sse/dispatchEvent2ToSpecificUser', options);
			} 

			 
   			 function sendNewsAll(){
		 		 var title = document.getElementById("ltitle").value;
				 var text = document.getElementById("ltext").value;
   				 if (title ==='' && text ===''){
   					title = "News AllSub tipo 1- Default";
   					text = "Notizia di default � gestita con il Content-Type: application/x-www-form-urlencoded";
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
			 
		</script>
		
		
</body>
</html>