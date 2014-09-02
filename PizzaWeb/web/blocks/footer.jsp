<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<footer>
    Per info non contattateci in quanto siamo una truffa...le pizze non arriveranno mai.
    <%= session.getAttribute("alert") %>
   <% session.setAttribute("alert", ""); %>
</footer>