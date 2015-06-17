<%@page import="mvc.*"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<article>
  <div id="message">
    <div id="goodMessage"><%= ( request.getSession().getAttribute("good") != null) ? request.getSession().getAttribute("good") : "" %></div>
    <div id="warningMessage"><%= ( request.getSession().getAttribute("warning") != null) ?  request.getSession().getAttribute("warning") : "" %></div>
    <div id="errorMessage"><%= (request.getSession().getAttribute("error") != null ) ? request.getSession().getAttribute("error") : "" %></div>
  </div>
  <div id="page"></div>
  <div id="body_article">
    <h1>BENVENUTI!</h1>
  </div>
    <%String view=""+request.getSession().getAttribute("view");%>
    <%if(view.equals("catalogo")){%>
      <script> $("#body_article").html("<%=Controller.getCatalogo(request)%>") </script>
    <%}else if(view.equals("prenotazioni")){%>
      <script> $("#body_article").html("<%= Controller.getPrenotazioni(request)%>") </script>  
    <%}else if(view.equals("utenti")){%>
      <script> $("#body_article").html("<%= Controller.getUtenti(request)%>") </script>  
    <%}else if(view.equals("registration")){%>
      <script> $("#body_article").html("<%= Controller.getCatalogo(request)%>") </script>  
    <%}%>
</article>
