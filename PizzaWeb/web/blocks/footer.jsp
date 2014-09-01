<%@page import="mvc.Controller"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <footer>
        <%if(Controller.checkAdmin(request)){ %>
            <%=request.getSession().getAttribute("message")%>
            
        <%}%>
        <%request.getSession().setAttribute("message","");%>
    </footer>
</html>
