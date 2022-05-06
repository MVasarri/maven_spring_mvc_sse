<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

<head>
<title>Airport runway condition - Home Page</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/css/bootstrap.min.css">
</head>

<body class="d-flex h-100 text-center text-secondary bg-dark">
	<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
		<main class=" px-4 py-5">
			<div class="py-5">
				<h1 class="display-5 fw-bold text-white">Airport runway condition - Home Page</h1>
				<div class="col-lg-6 mx-auto">
					<div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
						<button type="button" 
							class="btn btn-outline-light btn-lg px-4"
							onclick="window.location.href='messageSender'; return false;">
							Make report</button>
						<button type="button"
							class="btn btn-outline-info btn-lg px-4 me-sm-3 fw-bold"
							onclick="window.location.href='messageReceiver'; return false;">
							View report</button>
					</div>
				</div>
			</div>
		</main>
	</div>
	
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/webResources/bootstrap-5.1.3-dist/js/bootstrap.bundle.min.js">
		
	</script>
</body>

</html>