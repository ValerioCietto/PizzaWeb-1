<%@page import="mvc.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <aside>
            <%=request.getSession().getAttribute("message")%>
            <%request.getSession().setAttribute("message","");%>
        </aside>
    </body>
</html>

