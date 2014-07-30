<%@page import="mvc.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <article>
            <%String view=""+request.getSession().getAttribute("view");%>
            <%if(view.equals("catalogo")){%>
                <%= Controller.getCatalogo(request)%>
            <%}else if(view.equals("prenotazioni")){%>
                <%= Controller.getPrenotazioni(request)%>
            <%}else if(view.equals("registrazione")){%>
                <%= Controller.getCatalogo(request)%>
            <%}else{%>
                <%= "<div class='welcome'>BENVENUTI!</div>"%>
            <%}%>
        </article>
    </body>
</html>
