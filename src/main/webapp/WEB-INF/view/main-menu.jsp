<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html>

<head>

</head>

<body>

<h2>Spring MVC Demo - Home Page</h2>


	<a href="customer/list">Customer List</a>
	<br>
	<a href="address/list">Address List</a>
	<br>
	<a href="testSse1">ClientListen List-test01</a>
	<br>
	<a href="testSse2">ClientListen List-test02</a>
	
	<div id="prova1"></div>
	
		<script type="text/javascript">
			console.log("prova");
			var el = document.getElementById('prova1');
		    el.appendChild(document.createTextNode("messaggio" + "***"));
		    el.appendChild(document.createElement('br'));
	    </script> 

</body>

</html>