<%@page import="mvc.*" %>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="components.UserBean"/>

<!DOCTYPE html>
<html>

    <head>
        <title>Da Purrer</title>
        <link rel="stylesheet" href="css/pizzacss.css" type="text/css"> 
        <link rel="stylesheet" href="css/article.css" type="text/css"> 
        <link rel="stylesheet" href="css/footer.css" type="text/css">
        <link rel="stylesheet" href="css/article.css" type="text/css"> 
        <link rel="stylesheet" href="css/header.css" type="text/css"> 
        <link rel="stylesheet" href="css/nav.css" type="text/css"> 

        <script src="js/jquery-2.1.4.min.js" ></script>
        <script src="js/re.js" ></script>
        <script src="js/menu.js" ></script>
    </head>

    <body>
        <div id="container">
            <%@include file="view/header.jsp" %>

            <div id="content">
                <%@include file="view/nav.jsp"%>
                <%@include file="view/article.jsp"%>
            </div>
            <%@include file="view/footer.jsp"%>
        </div>
    </body>
</html>
