<%@page import="mypackage.Model.*"%>
<%@page import="mypackage.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <%String ruoloN=(String)(request.getSession()).getAttribute("ruolo");%>
        <nav>
            <ul>
                <li><a href="catalogo.jsp">Catalogo</a></li>
                <% if(ruoloN!=null && ruoloN.equals("user")){%>
                    <li>Visualizza Tue Prenotazioni</li>
                <%}else if(ruoloN!=null && ruoloN.equals("admin")){%>
                    <li>Visualizza Tutte le Prenotazioni</li>"
                    <li><a href="loginmanager.jsp">Modifica Permessi</a></li>
                <%}%>
            </ul>
        </nav>   
    </body>
</html>

