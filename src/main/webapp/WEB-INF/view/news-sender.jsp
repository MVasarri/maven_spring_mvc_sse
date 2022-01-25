<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
			<h1>Prova</h1>			

			<input type="button" value="sendNews"
				   onclick= "sendNews()"; return false;"
				   class="add-button"
			/>
			
			<input type="button" value="sendJSON"
				   onclick= "sendJSON()"; return false;"
				   class="add-button"
			/>
			
		
		
<!-- 		<form:form action="dispatchEvent" modelAttribute="address" method="POST">
					<label>num:</label>
					<input type="text" value="name" class="save" param/>

					<label></label>
					<input type="submit" value="Save" class="save" />
			</form:form> -->
			
		<script type="text/javascript">

  			 function  sendJSON() {
				var title = 'messaggio corto 1';
				var text =	'prova di un messaggio lungo ma non tanto 1';
				const data = { title, text};
				const options = {
						method : 'POST',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify(data)
				};
				
				const response = fetch('/maven-spring-mvc-sse/dispatchEvent2', options);
			}   
			 		 
/*  			 function sendJSON(){		
				 	
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
		        }  */
			 
   			 function sendNews(){

				 var xhr = new XMLHttpRequest();
				 xhr.open("POST", 'http://localhost:8080/maven-spring-mvc-sse/dispatchEvent', true);
	
				 //Send the proper header information along with the request
				 xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	
				 xhr.onreadystatechange = function() { // Call a function when the state changes.
				     if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
				         console.log("inviato: " + this.status)
				     }
				 }
				 xhr.send("title=bar&text=ipsum");
				 // xhr.send(new Int8Array());
				 // xhr.send(document);
			 }  
			 
			 
		 
		</script>
		
		
</body>
</html>