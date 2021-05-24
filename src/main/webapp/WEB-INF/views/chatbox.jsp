<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="<c:url value="/static/css/style.css" />">

<link rel="icon" type="image/png"
	href="<c:url value="/static/images/icon.jpg" />">
<title>Chat</title>
</head>
<body>
	<p id="username" style="display: none">${user.username}</p>
	<p id="userAvatar" style="display: none">
		<c:url value="/files/${user.username}/${user.avatar}" />
	</p>
	<div class="container center">
		<div class="conversation-container">
			<div class="left-side active">
				<div class="user-contain"
					style="color: white; margin-left: 2rem; float: left">
					<div class="user-img">
						<img
							src="<c:url value="./files/${user.username}/${user.avatar}" />">
					</div>
					<div class="user-info" style="padding-top: 1rem;">
						<span class="user-name">${user.username}</span>
					</div>
				</div>
				<div class="dropdown">
					<i class="fa fa-ellipsis-h dropbtn" onclick="dropMenu()"
						aria-hidden="true"></i>
					<div id="myDropdown" class="dropdown-content border">

						<a href="<c:url value="/users/update"/>">Change Password</a> <a
							href="<c:url value="/users/logout"/>">Logout</a>
					</div>
				</div>
				<div class="list-user-search">
					<input type="text" class="txt-input" placeholder="Search..."
						onkeyup="searchUser(this)">
				</div>
				<div class="list-user">
					<ul>
						<c:forEach var="friend" items="${friends}">
							<li id=${friend.username } onclick="setReceiver(this);">
								<div class="user-contain">
									<div class="user-img">
										<img id="img-${friend.username}"
											src="<c:url value="/files/${friend.username}/${friend.avatar}" />"
											alt="Image of user">
										<div id="status-${friend.username}" class="user-img-dot"></div>
									</div>
									<div class="user-info">
										<span class="user-name">${friend.username}</span>
									</div>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="right-side" id="receiver"></div>
		</div>
	</div>


	<script type="text/javascript"
		src="<c:url value="/static/js/chatbox.js" />" charset="utf-8"></script>
</body>
</html>