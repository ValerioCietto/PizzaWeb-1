<%@page import="mvc.Controller"%>
<%@page import="mvc.Model"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<nav>
    <h1>Operazioni disponibili</h1>
    <div>
        <form action="/PizzaWeb/Servlet" method="get">
        <input type="hidden" name="action" value="switch">
        <input type="submit" name="name"   value="catalogo">
        </form>
    </div>
    <%if(Controller.checkLogin(request)){ %>   
        <div>
            <form action="/PizzaWeb/Servlet" method="get">
                <input type="hidden" name="action" value="switch">
                <input type="submit" name="name" value="prenotazioni">
            </form>
        </div>
        <%if(Controller.checkAdmin(request)){ %>
            <div>
            <form action="/PizzaWeb/Servlet" method="post">
                <input type="hidden" name="action" value="switch">
                <input type="submit" name="name" value="utenti">
            </form>
            </div>
       <% }%>
    <%} %>
</nav>