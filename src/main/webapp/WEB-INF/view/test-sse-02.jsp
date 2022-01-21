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

	<h1>Sse3 client</h1>
	<div class="grid-container">
		  <div id="sseM1"></div>
		  <div id="sseM2"></div> 
	</div>
	<script type="text/javascript">
	console.log("prova");
	 

 		var sse = new EventSource('http://localhost:8080/maven_spring_mvc_sse/sse3');
		sse.onmessage = function (evt) {
		    var el = document.getElementById('sseM1');
		    console.log(evt.data );
		    el.appendChild(document.createTextNode(evt.data + "  prova"));
		    el.appendChild(document.createElement('br'));
		};  

  
	</script> 
</body>
</html>