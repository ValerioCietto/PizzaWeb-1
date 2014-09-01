<%@page import="mvc.*"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<article>
    <%String view=""+request.getSession().getAttribute("view");%>
    <%if(view.equals("catalogo")){%>
        <%= Controller.getCatalogo(request)%>
    <%}else if(view.equals("prenotazioni")){%>
        <%= Controller.getPrenotazioni(request)%>
    <%}else if(view.equals("utenti")){%>
        <%= Controller.getUtenti(request)%>
    <%}else if(view.equals("registration")){%>
        <%= Controller.getCatalogo(request)%>
    <%}else{%>
        <%= "<div class='welcome'>BENVENUTI!</div>"%>
    <%}%>
</article>
