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
		
		
		<h1>News sse client</h1>
		<div class="grid-container">
			  <div id="sseNews1"></div>
			  <div id="sseM2"></div> 
		</div>
		
		<script type="text/javascript">
			
 		(function() {
 		   // your page initialization code here
 		   // the DOM will be available here
 	 	   const eventSource = new EventSource('http://localhost:8080/maven_spring_mvc_sse/subscribe'); // se andiamo a controllare sul browser vedremo che subscription è un "text/event-stream"
 	 	   
 	 	   eventSource.addEventListener("latestNews", (event) => {
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
 		   
 		})();
 		
 		window.ombeforeunload = function () {
 			evtSource.close();
 		}
 		</script>
 		

</body>
</html>