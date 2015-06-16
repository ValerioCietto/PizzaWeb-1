<%@page import="mvc.*"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<article>
  <div id="message">
    <div id="goodMessage"><%= ( request.getSession().getAttribute("good") != null) ? request.getSession().getAttribute("good") : "" %></div>
    <div id="warningMessage"><%= ( request.getSession().getAttribute("warning") != null) ?  request.getSession().getAttribute("warning") : "" %></div>
    <div id="errorMessage"><%= (request.getSession().getAttribute("error") != null ) ? request.getSession().getAttribute("error") : "" %></div>
  </div>
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
        <%= "<h1>BENVENUTI!</h1>"%>
    <%}%>
</article>
