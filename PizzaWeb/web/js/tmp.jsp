<%@page import="components.UserBean"%>
<%@page import="mvc.*"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="UserBean"/>
<%= user.getView() %>