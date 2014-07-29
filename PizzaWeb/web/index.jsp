<%@page import="mvc.*" import="components.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <%@include file="blocks/header.jsp" %>
    <body>
        <header><h1>PIZZERIA ONLINE</h1></header>
        <%@include file="blocks/nav.jsp"%>
        <%@include file="blocks/aside.jsp"%>
        <%
        String view=(String)(request.getSession()).getAttribute("view");
        if(view==null || view.equals("")){%>
            <article><p>home</p></article>
        <%}else if(view.equals("catalogo")){%>
            <%@include file="function/catalogo.jsp"%>
        <%}else if(view.equals("loginManager")){%>
            <%@include file="function/loginmanager.jsp"%>
        <%}else if(view.equals("prenotazioni")){%>
            <%@include file="function/prenotazioni.jsp"%>    
        <%}else{ //non ancora pronto%>
            <article><p>non ancora pronto</p></article>
        <%}%>
        <footer>footer</footer>
    </body>
</html>
