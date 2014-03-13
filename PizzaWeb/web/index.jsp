<%@page import="mypackage.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pizzeria</title>
    </head>
    <body>
        <% DBManager.inizializza();%>
        <% //=EditorHtml.pagina(request)%>
        <%
        String view=request.getParameter("view");
        String ruolo=(String)(request.getSession()).getAttribute("ruolo");
        String message=(String)(request.getSession()).getAttribute("message");
        String action=request.getParameter("action");
        %>
        <header><h1>PIZZERIA ONLINE</h1></header>
        <%@include file="nav.jsp"%>
        <%@include file="aside.jsp"%>
        <%
        if(view!=null && !view.equals("")){
            (request.getSession()).setAttribute("view", view);
        }
        view=(String)(request.getSession()).getAttribute("view");
        if(view==null || view.equals("")){%>
            <article><p>home</p></article>
        <%}else if(view.equals("catalogo")){%>
            <%@include file="catalogo.jsp"%>
        <%}else if(view.equals("modPermessi")){%>
            <%@include file="loginmanager.jsp"%>
        <%}else{ //non ancora pronto%>
            <article><p>non ancora pronto</p></article>
        <%}%>
        <footer>footer</footer>
    </body>
</html>
