<%@ page import = "mvc.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <article>
            <%String view=""+request.getSession().getAttribute("View");%>
            <%if(view.equals("catalogo")){%>
                <%= Controller.getCatalogo(request)%>
            <%}%>
        </article>
    </body>
</html>
