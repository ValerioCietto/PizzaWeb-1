<%@page import="mvc.*" %>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <head>
        <title>Da Purrer</title>
        <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
        <link rel="stylesheet" href="css/article.css" type="text/css"> 
        <link rel="stylesheet" href="css/aside.css" type="text/css"> 
        <link rel="stylesheet" href="css/footer.css" type="text/css"> 
        <link rel="stylesheet" href="css/article.css" type="text/css"> 
        <link rel="stylesheet" href="css/header.css" type="text/css"> 
        <link rel="stylesheet" href="css/nav.css" type="text/css"> 
        
        <script type="text/javascript" src="function/inputCheck.js"></script>
        
    </head>
        
    <body>
        <%@include file="blocks/header.jsp" %>
        <%@include file="blocks/nav.jsp"%>
        <%@include file="blocks/article.jsp"%>
        <%@include file="blocks/aside.jsp"%>
        <%@include file="blocks/footer.jsp"%>
    </body>
</html>
