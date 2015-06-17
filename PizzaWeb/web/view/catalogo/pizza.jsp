<%@page import="components.Pizza"%>
<%@page import="mvc.*" %>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>


<% Pizza al1 = (Pizza) request.getSession().getAttribute("pizza");%>
<div class='pizza'>
  <span class='nome'><%= al1.getNome()%></span>
  <span></br>Ingredienti:  </span>
  <span class='ingredienti'>" + al1.getIngredinti() + "</span>
  <span></br>Prezzo:  </span>
  <span class='prezzo'>" + al1.getPrezzo() + "</span>
</div>
