<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

<head>
	<title>Save Address</title>

	<link type="text/css"
		  rel="stylesheet"
		  href="${pageContext.request.contextPath}/resources/css/style.css">

	<link type="text/css"
		  rel="stylesheet"
		  href="${pageContext.request.contextPath}/resources/css/add-customer-style.css">
</head>

<body>
	
	<div id="wrapper">
		<div id="header">
			<h2>CRM - Address Relationship Manager</h2>
		</div>
	</div>

	<div id="container">
		<h3>Save Address</h3>
	
		<form:form action="saveAddress" modelAttribute="address" method="POST">

			<!-- need to associate this data with customer id -->
			<form:hidden path="id" />
					
			<table>
				<tbody>
					<tr>
						<td><label>Street:</label></td>
						<td><form:input path="street" /></td>
					</tr>
				
					<tr>
						<td><label>num:</label></td>
						<td><form:input path="num" /></td>
					</tr>

					<tr>
						<td><label>city:</label></td>
						<td><form:input path="city" /></td>
					</tr>
					
					<tr>
						<td><label>province:</label></td>
						<td><form:input path="province" /></td>
					</tr>

					<tr>
						<td><label></label></td>
						<td><input type="submit" value="Save" class="save" /></td>
					</tr>

				
				</tbody>
			</table>
		
		
		</form:form>
	
		<div style="clear; both;"></div>
		
		<p>
			<a href="${pageContext.request.contextPath}/address/list">Back to List</a>
		</p>
	
	</div>

</body>

</html>










