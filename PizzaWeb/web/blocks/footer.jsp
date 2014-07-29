<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <footer>
            <%=request.getSession().getAttribute("message")%>
            <%request.getSession().setAttribute("message","");%>
    </footer>
</html>
