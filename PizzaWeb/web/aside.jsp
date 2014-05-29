<%@page import="mypackage.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <%String ruoloA=(String)(request.getSession()).getAttribute("ruolo");
        String messageA=(String)(request.getSession()).getAttribute("message");%>
        <aside>
            <table>
                <%if(ruoloA==null || ruoloA.equals("")){%>
                    <tr>
                        <td>
                            <form action="/PizzaWeb/Servlet" method="get" >
                                <p>Login</p>
                                <input type ="text" name="login" required>
                                <p>Password</p>
                                <input type ="password" name="password" required>
                                <input type= "submit"   name="action" value= "login">
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p><a href="registrati.html">registrati!</a></p>
                            
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
                if(messageA!=null && !messageA.equals("")){%>
                    <tr>
                        <td>
                            <p><%=messageA%></p>
                        </td>
                    </tr>
                    <%(request.getSession()).setAttribute("message","");
                }%>
            </table>
        </aside>
    </body>
</html>

