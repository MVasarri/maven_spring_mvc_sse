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
			
		
		
<!-- 		<form:form action="dispatchEvent" modelAttribute="address" method="POST">
					<label>num:</label>
					<input type="text" value="name" class="save" param/>

					<label></label>
					<input type="submit" value="Save" class="save" />
		</form:form> -->
		<script >
/* 			function postData() {
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
				
				const response = fetch('/maven_spring_mvc_sse/dispatchEvent',options);
			}
			 */
			 function sendNews(){
				 var url = "http://localhost:8080/maven_spring_mvc_sse/dispatchEvent";

				 var xhr = new XMLHttpRequest();
				 xhr.open("POST", url);

				 xhr.setRequestHeader("Accept", "application/json");
				 xhr.setRequestHeader("Content-Type", "application/json");

				 xhr.onreadystatechange = function () {
				    if (xhr.readyState === 4) {
				       console.log(xhr.responseText);
				    }};

				 var data = `{
				   "title": "Messaggio 1",
				   "text": "Prova secondo messaggio un po piu lungo",
				 }`;

				 xhr.send(data);
				 
			 }
		
		</script>
		
		
</body>
</html>