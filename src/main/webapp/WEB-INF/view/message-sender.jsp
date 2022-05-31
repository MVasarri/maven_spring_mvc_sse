<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Make report</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/css/bootstrap.min.css">
</head>
<body class="d-flex h-100 text-center text-secondary bg-dark">

	<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
		<main class=" px-4 py-5">
			<h1 class="display-5 fw-bold text-white">Make report</h1>
			<form>
				<div class="mb-3">
					<label for="sTitle" class="form-label"> Select runway to report</label>
					<!-- 					<input
						type="text" class="form-control" id="sTitle"
						aria-describedby="titleHelp"> -->
					<select class="form-select" aria-label="Default select example"
						id="sTitle">
						<option selected>Runway N° 1</option>
						<option >Runway N° 2</option>
						<option >Runway N° 3</option>
						<option >Runway N° 4</option>
					</select>

				</div>
				<div class="mb-3">
					<label for="sText" class="form-label">Select runway status</label>
					<!-- 				<input
						type="text" class="form-control" id="sText"> -->
					<select class="form-select" aria-label="Default select example"
						id="sText">

						<option selected>COMPACTED SNOW</option>
						<option >DRY</option>
						<option >DRY SNOW</option>
						<option >DRY SNOW ON TOP OF COMPACTED SNOW</option>
						<option >DRY SNOW ON TOP OF ICE</option>
						<option >FROST</option>
						<option >ICE</option>
						<option >SLIPPERY WET</option>
						<option >SLUSH</option>
						<option >SPECIALLY PREPARED WINTER RUNWAY</option>
						<option >STANDING WATER</option>
						<option >WATER ON TOP OF COMPACTED SNOW</option>
						<option >WET</option>
						<option >WET ICE</option>
						<option >WET SNOW</option>
						<option >WET SNOW ON TOP OF COMPACTED SNOW</option>
						<option >WET SNOW ON TOP OF ICE</option>
						<option >CHEMICALLY TREATED</option>
						<option >LOOSE SAND</option>			
					</select>
				</div>

				<button type="button" class="btn btn-outline-light btn-lg px-4"
					onclick="sendJSON_All('dispatchEventJSON', false, null)">sendReport</button>
				<button type="button" class="btn btn-outline-light btn-lg px-4"
					onclick="send100JSON_All('dispatchEventJSON')">100ReportClient</button>
				<button type="button" class="btn btn-outline-light btn-lg px-4"
					onclick="sendJSON_All('dispatch100EventJSON', false, null)">100ReportServer</button>
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