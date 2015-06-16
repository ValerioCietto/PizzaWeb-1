<%@page import="mvc.*" %>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
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

      <!-- <script type="text/javascript" src="function/inputCheck.js"></script> -->
      <script src="js/jquery-2.1.4.min.js" ></script>
      <script src="js/re.js" ></script>
      
    </head>
        
    <body>
      <div id="container">
        <%@include file="blocks/header.jsp" %>

        <div id="content">
          <%@include file="blocks/nav.jsp"%>
          <%@include file="blocks/article.jsp"%>
        </div>
          <%@include file="blocks/footer.jsp"%>
      </div>
    </body>
</html>
