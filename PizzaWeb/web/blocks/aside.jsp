<%@page import="mvc.*" %>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <aside>
            <%if(!Controller.checkLogin(request)){ %>
                <%String checkView = request.getSession().getAttribute("view")+"";
                if(checkView!=null && !checkView.equals("register")){%>
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
                        <input type="submit" name="name"   value="Registrati">
                        </form>
                    </div>
                <%}else {%>
                    <%@include file="/function/inputCheck.jsp" %>
                <div>
                        <form action="/PizzaWeb/Servlet" method="get">
                        <input type="hidden" name="action" value="switch">
                        <input type="submit" name="name"   value="back">
                        </form>
                </div>
                <%}
            }else{ %>
                <%= View.login(request) %>
                <div>
                    <form action="/PizzaWeb/Servlet" method="get">
                        <input type="submit" name="action" value="logout">
                    </form>
                </div>
            <%}%>
        </aside>
    </body>
</html>

