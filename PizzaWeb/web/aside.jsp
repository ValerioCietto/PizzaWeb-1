<%@page import="mypackage.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <aside>
            <table>
                <%if(ruolo==null || ruolo.equals("")){%>
                    <tr>
                        <td>
                            <form action="/PizzaWeb/Servlet" method="get">
                                <p>Login</p>
                                <input type ="text" name="login">
                                <p>Password</p>
                                <input type ="password" name="password">
                                <input type= "submit"   name="action" value= "login">
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>registrati</p>
                        </td>
                    </tr>
                <%}else{%>
                    <tr>
                        <td>
                            <form action="/PizzaWeb/Servlet" method="get">
                                <input type="submit" name= "action" value= "logout">
                            </form>
                        </td>
                    </tr>
                <%}
                if(message!=null && !message.equals("")){%>
                    <tr>
                        <td>
                            <p><%=message%></p>
                        </td>
                    </tr>
                    <%(request.getSession()).setAttribute("message","");
                }%>
            </table>
        </aside>
    </body>
</html>

