<%@page import="mvc.*"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="components.UserBean"%>
<jsp:useBean id="user" scope="session" class="UserBean"></jsp:useBean>

<article>
  <div id="message">
    <%= user.getMessage()  %>
  </div>
  <div id="page"></div>
  <div id="body_article">
    <h1>BENVENUTI!</h1>
  </div>
  <%String view = user.getView();%>
          <%= view %>
  <% if (view != null) {

        if (view.equals("catalogo")) {%>
          <script src='js/catalogo.js'></script>
          <script> $("#body_article").html("<%=Controller.getCatalogo(request)%>")</script>
        <%} else if (view.equals("prenotazioni")) {%>
          <script> $("#body_article").html("<%= Controller.getPrenotazioni(request)%>")</script>  
        <%} else if (view.equals("utenti")) {%>
          <script> $("#body_article").html("<%= Controller.getUtenti(request)%>")</script>  
        <%}
      }%>
</article>
