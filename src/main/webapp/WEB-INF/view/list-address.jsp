<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>

<head>
	<title>List Address</title>
	
	<!-- reference our style sheet -->

	<link type="text/css"
		  rel="stylesheet"
		  href="${pageContext.request.contextPath}/resources/css/style.css" />

</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>CRM - Address Relationship Manager</h2>
		</div>
	</div>
	
	<div id="container">
	
		<div id="content">
		
			<!-- put new button: Add Address -->
		
			<input type="button" value="Add Address"
				   onclick="window.location.href='showFormForAdd'; return false;"
				   class="add-button"
			/>
		
			<!--  add our html table here -->
		
			<table>
				<tr>
					<th>street</th>
					<th>Num</th>
					<th>City</th>
					<th>Province</th>
					<th>Action</th>
				</tr>
				
				<!-- loop over and print our address -->
				<c:forEach var="tempAddress" items="${address}">
				
					<!-- construct an "update" link with address id -->
					<c:url var="updateLink" value="/address/showFormForUpdate">
						<c:param name="addressId" value="${tempAddress.id}" />
					</c:url>					

					<!-- construct an "delete" link with address id -->
					<c:url var="deleteLink" value="/address/delete">
						<c:param name="addressId" value="${tempAddress.id}" />
					</c:url>					
					
					<tr>
						<td> ${tempAddress.street} </td>
						<td> ${tempAddress.num} </td>
						<td> ${tempAddress.city} </td>
						<td> ${tempAddress.province} </td>
						
						<td>
							<!-- display the update link -->
							<a href="${updateLink}">Update</a>
							|
							<a href="${deleteLink}"
							   onclick="if (!(confirm('Are you sure you want to delete this address?'))) return false">Delete</a>
						</td>
						
					</tr>
				
				</c:forEach>
						
			</table>
				
		</div>
		<p>
			<a href="${pageContext.request.contextPath}/">Back to Home</a>
		</p>
<%-- 	
		<p>
				<a href="<c:url var="homeLink" value="/"/>" >Back to Home</a>
		</p> 
--%>
		<script>
			console.log("prova");
	    </script> 
	
	</div>
	

</body>

</html>









