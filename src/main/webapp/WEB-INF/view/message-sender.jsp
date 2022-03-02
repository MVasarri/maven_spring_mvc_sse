<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Message Sender</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/css/bootstrap.min.css">
</head>
<body class="d-flex h-100 text-center text-secondary bg-dark">

	<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
		<main class=" px-4 py-5">
			<h1 class="display-5 fw-bold text-white">Invia Messaggio</h1>
			<form>
				<div class="mb-3">
					<label for="sTitle" class="form-label"> Titolo Messaggio</label>
					<!-- 					<input
						type="text" class="form-control" id="sTitle"
						aria-describedby="titleHelp"> -->
					<select class="form-select" aria-label="Default select example"
						id="sTitle">
						<option selected>Pista N° 1</option>
						<option >Pista N° 2</option>
						<option >Pista N° 3</option>
						<option >Pista N° 4</option>
					</select>

				</div>
				<div class="mb-3">
					<label for="sText" class="form-label">Testo Messaggio</label>
					<!-- 				<input
						type="text" class="form-control" id="sText"> -->
					<select class="form-select" aria-label="Default select example"
						id="sText">
						<option selected>La Pista leggermente Bagnata</option>
						<option >Strato Di ghiaccio sulla pista</option>
						<option >Pista Ben Asciutta</option>
						<option >Pista allagata e forti venti</option>
					</select>
				</div>

				<button type="button" class="btn btn-outline-light btn-lg px-4"
					onclick="sendJSON_All('dispatchEventJSON', false, null)">sendJSON</button>
				<button type="button" class="btn btn-outline-light btn-lg px-4"
					onclick="send100JSON_All('dispatchEventJSON')">send100JSON</button>
				<button type="button" class="btn btn-outline-light btn-lg px-4"
					onclick="sendJSON_All('dispatch100EventJSON', false, null)">rep100JSON</button>
			</form>
		</main>
	</div>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/js/bootstrap.bundle.min.js">
		
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/webResources/js/message-sender.js">
		
	</script>

</body>
</html>