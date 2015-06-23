<%@page import="mvc.*"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>

  <div id="message">
    <jsp:getProperty name="user" property="message"/>
  </div>