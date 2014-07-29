<%@page import="mvc.*" import="components.*"%>

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
            <table>
                <tr>
                    <td>
                        <form action="/PizzaWeb/Servlet" method="get">
                            <input type="hidden" name="action" value="switch">
                            <input type="submit" name="name" value="catalogo">
                        </form>
                    </td>
                </tr>
                <% if(ruoloN!=null && ruoloN.equals("user")){%>
                    <tr><td><form action="/PizzaWeb/Servlet" method="get">
                                <input type="hidden" name="action" value="switch">
                                <input type="submit" name="name" value="prenotazioni">
                            </form>
                        </td>
                    </tr>
                <%}else if(ruoloN!=null && ruoloN.equals("admin")){%>
                    <tr><td><form action="/PizzaWeb/Servlet" method="get">
                                <input type="hidden" name="action" value="switch">
                                <input type="submit" name="name" value="prenotazioni">
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form action="/PizzaWeb/Servlet" method="get">
                                <input type="hidden" name="action" value="switch">
                                <input type="submit" name="name" value="loginManager">
                            </form>
                        </td>
                    </tr>
                <%}%>
            </table>
        </nav>   
    </body>
</html>

