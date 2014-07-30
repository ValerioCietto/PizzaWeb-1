<%@page import="mvc.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <aside>
            <% //if(request.getSession().getAttribute("View").equals("Register")){ %>
                <%@include file='/function/inputCheck.jsp' %>
                <% request.getSession().setAttribute("View", "");%>
            <% //}else
                if(!Controller.checkLogin(request)){ %>
                <div>
                    <form action="/PizzaWeb/Servlet" method="post" >
                        <p>Username</p>
                        <input type ="text" name="username" required>
                        <p>Password</p>
                        <input type ="password" name="password" required>
                        <input type= "submit"   name="action" value= "login">
                    </form>
                </div>
                <div>
                    <form action="/PizzaWeb/Servlet" method="get">
                    <input type="hidden" name="action" value="switch">
                    <input type="submit" name="action"   value="Registrati">
                    </form>
                </div>
            <%}else{ %>
            <%= View.login(request) %>
                <div>
                    <form action="/PizzaWeb/Servlet" method="get">
                    <input type="submit" name="action" value="logout">
                    </form>
                </div>
            <%} %>
        </aside>
    </body>
</html>

