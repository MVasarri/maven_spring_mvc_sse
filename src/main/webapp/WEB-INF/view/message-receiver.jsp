<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Message Receiver</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/css/bootstrap.min.css">

</head>
<body class="d-flex h-100 text-center text-secondary bg-dark">
	<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
		<header class="row">
			<div class="col-9 ">
				<h1 class=" mb-0 display-5 fw-bold text-white">Message
					Receiver</h1>
			</div>
			<div class="col-3 ">
				<h3 class="mb-0">
					User ID: <span id="userID"></span>
				</h3>
			</div>
		</header>

		<main class="px-4 py-5 ">
			<div class="card text-center">
				<div class="row card-header">
					<div class="col-9 ">Message</div>
					<div class="col-3 ">
						msgID: <span id="idMsg"></span>
					</div>
				</div>
				<div class="row card-body">
					<div >
						<h2 id="ttlMsg" class="card-title fw-bold "></h2>
						<h5 id="txtMsg" class="card-text"></h5>
					</div>
				</div>
				<div class="collapse" id="collapseExample">
					<div class="card card-body">
						<p>
							ID msg precedente: <span id="mPrev"></span>
						</p>
						<p>
							N° msg sul DB: <span id="nDBMsg"></span>
						</p>
					</div>
				</div>
				<div class="row card-footer text-muted">
					<div class="col-5 mb-0"><span id="sTime"></span></div>
					<div class="col-2 mb-0">
						<!-- Button trigger modal -->
						<button type="button" class="btn btn-secondary"
							data-bs-toggle="collapse" data-bs-target="#collapseExample"
							aria-expanded="false" aria-controls="collapseExample">
							<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="currentColor" class="bi bi-info-square"
								viewBox="0 0 16 16">
  								<path
									d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"></path>
  								<path
									d="m8.93 6.588-2.29.287-.082.38.45.083c.294.07.352.176.288.469l-.738 3.468c-.194.897.105 1.319.808 1.319.545 0 1.178-.252 1.465-.598l.088-.416c-.2.176-.492.246-.686.246-.275 0-.375-.193-.304-.533L8.93 6.588zM9 4.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"></path>
							</svg>
						</button>
					</div>
					<div class="col-5 mb-0">
						N° msg received: <span id="nMsg"></span>
					</div>
				</div>
			</div>
		</main>
	</div>

	<!-- 	<div class="grid-container">
		<div id="sseMsg">
			<h6 id="ttlMsg"></h6>
			<p id="txtMsg"></p>
			<p id="idMsg"></p>
			<p id="mPrev"></p>
			<p id="nMsg"></p>
			<p id="nDBMsg"></p>
			<p id="lastMsg"></p>
		</div>
	</div> -->

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/js/bootstrap.bundle.min.js">
		
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/webResources/js/message-receiver.js">
		
	</script>


</body>
</html>