<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Message Receiver</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/css/bootstrap.min.css">

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

	<div>
		<h1>Message SSE client Receiver</h1>
		<h4>
			User ID: <span id="userID"></span>
		</h4>
	</div>

	<div class="grid-container">
		<div id="sseMsg">
			<h6 id="ttlMsg"></h6>
			<p id="txtMsg"></p>
			<p id="idMsg"></p>
			<p id="mPrev"></p>
			<p id="nMsg"></p>
			<p id="nDBMsg"></p>
			<p id="lastMsg"></p>
		</div>
	</div>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/js/bootstrap.bundle.min.js">
		
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/webResources/js/message-receiver.js">	
	</script>


</body>
</html>