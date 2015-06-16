<%@page import="mvc.View"%>
<%@page import="mvc.Controller"%>
<%@page import="mvc.Model"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<nav>
  <script src="js/account.js"></script>
  <div id="account_system">

    <%if (!Controller.checkLogin(request)) { %>
    <%String checkView = request.getSession().getAttribute("view") + "";%>

    <div id="Login">
      <form name="login_form" action="/PizzaWeb/Servlet" method="post" >
        <div class="input">
          <div class="row">
            <label>Username</label>
            <input type ="text" name="username" required value="<%= (request.getParameter("username") != null) ? request.getParameter("username") : ""%>">
          </div>
          <div class="row">
            <label>Password</label>
            <input type ="password" name="password" required>
          </div>
        </div>
          <input type= "button"   name="login_button" value= "login" onclick="Account.checkLogin();">
          <input type="button"  name = "join_button" value="Registrati" onclick="Account.updateForm('Join')">
          <input type= "hidden"   name="action" value= "login">
      </form>
    </div>
    <div id="Join">     
      <button onclick="Account.updateForm('Login')" >Back </button>
      <div class="join_div_form">
        <form id='myForm' action='/PizzaWeb/Servlet' method='post' name="join_form">
          <div class="row">
            <label>Username:</label> <input id='field_username' type='text' required pattern='\w+' name='username'>
          </div>
          <div class="row">
            <label>Password:</label> <input id='field_pwd1' type='password' required pattern='(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}' name='password1'>
          </div>
          <div class="row">
            <label>Confirm Password:</label> <input id='field_pwd2' type='password' required pattern='(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}' name='password2'>
          </div>
          <div>
            <input type='submit' name='action' value= 'register'>
          </div>
        </form>
      </div>
    </div>

    <script>
      Account.updateForm('Login');
    </script>     
    <%
    } else {%>
    <%= View.login(request)%>
    <div>
      <form action="/PizzaWeb/Servlet" method="get">
        <input type="submit" name="action" value="logout">
      </form>
    </div>
    <%}%>
    <%if (Controller.checkLogin(request) && Controller.checkAdmin(request)) {%>
    <div id="terminale">
      <%=request.getSession().getAttribute("message")%>
    </div>

    <%}%>

  </div>
  <div id="operazioni">  
    <h1>Operazioni disponibili</h1>
    <div>
      <form action="/PizzaWeb/Servlet" method="get">
        <input type="hidden" name="action" value="switch">
        <input type="submit" name="name"   value="catalogo">
      </form>
    </div>
    <%if (Controller.checkLogin(request)) { %>   
    <div>
      <form action="/PizzaWeb/Servlet" method="get">
        <input type="hidden" name="action" value="switch">
        <input type="submit" name="name" value="prenotazioni">
      </form>
    </div>
    <%if (Controller.checkAdmin(request)) { %>
    <div>
      <form action="/PizzaWeb/Servlet" method="post">
        <input type="hidden" name="action" value="switch">
        <input type="submit" name="name" value="utenti">
      </form>
    </div>
    <% }%>
    <%}%>
  </div>
</nav>