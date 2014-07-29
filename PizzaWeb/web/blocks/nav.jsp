<%@page import="mvc.Controller"%>
<%@page import="mvc.Model"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
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
            <%} %>
        </nav>   
    </body>
</html>

