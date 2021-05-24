<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<c:url value="/static/css/style.css" />">
<link rel="icon" type="image/png"
	href="<c:url value="/static/images/icon.jpg" />">

<title>Login</title>
</head>
<body>
	<div class="container center">
		<div class="form-container bx-shadow">
		  <div class="tab-control bg-light border">
			<h3 class="active tab-control-btn login">Login</h3>
			<h3 class="tab-control-btn register">
			  <a href="<c:url value="/users/register" />" style="color: whitesmoke">Register</a>
			</h3>
		  </div>
		  <div class="login-form bg-light form active">
			<form action="<c:url value="/login" />" method="POST">
			  <input type="text" class="txt-input border" placeholder="User name" name="username">
			  <input type="password" class="txt-input border" placeholder="Password" name="password"> <button type="submit" class="btn btn-login bg-dark border">Login</button>
			</form>
		  </div>
		</div>
	  </div>
</body>
</html>