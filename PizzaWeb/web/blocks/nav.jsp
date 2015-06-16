<%@page import="mvc.View"%>
<%@page import="mvc.Controller"%>
<%@page import="mvc.Model"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<nav>
  <div id="account_system">
    <%if (!Controller.checkLogin(request)) { %>
    <%String checkView = request.getSession().getAttribute("view") + "";
      if (checkView != null && !checkView.equals("Registrati")) {%>
    <div class="Login">
      <form name="login_form" action="/PizzaWeb/Servlet" method="post" >
        <div class="input">
          <div class="row">
            <label>Username</label>
            <input type ="text" name="username" required>
          </div>
          <div class="row">
            <label>Password</label>
            <input type ="password" name="password" required>
          </div>
        </div>
        <div class="Button_Login">
          <input type= "submit"   name="action" value= "login">
        </div>
      </form>
      <div class="Button_Join">
        <form action="/PizzaWeb/Servlet" method="get">
          <input type="hidden" name="action" value="switch">
          <input type="submit" name="name"   value="Registrati">
        </form>
      </div>
    </div>

    <%} else {%>
    <div class="back_button">
      <form action="/PizzaWeb/Servlet" method="get">
        <input type="hidden" name="action" value="switch">
        <input type="submit" name="name"   value="back">
      </form>
    </div>
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
    <%}
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
    <%request.getSession().setAttribute("message", "");%>

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