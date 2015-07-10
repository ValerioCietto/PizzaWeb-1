<%@page import="mvc.View"%>
<%@page import="mvc.Controller"%>
<%@page import="mvc.Model"%>
<%@page session = "true" contentType="text/html" pageEncoding="UTF-8"%>
<nav>
  <script src="js/account.js"></script>
  <div id="account_system">

    <%if (!Controller.checkLogin(request)) { %>
    <%String checkView = (user != null ) ? user.getView() : " "; %>

    <div id="Login">
      <form name="login_form" action="/PizzaWeb/Servlet" method="post" >
        <div class="input">
          <div class="row">
            <label>Username</label>
            <input type ="text" name="username" required value="<%= (user != null &&  user.getUsername() != null) ? user.getUsername()  : ""%>">
          </div>
          <div class="row">
            <label>Password</label>
            <input type ="password" name="password" required>
          </div>
        </div>
          <input type= "submit"   name="login_button" value= "login" onclick=" return Account.checkLogin();">
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
            <label>Password:</label> <input id='field_pwd1' type='password' required pattern='(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}' name='password1' title="Nota: La password deve contenere 6 caratteri di cui almeno un numero, una maiuscola e una minuscola">
          </div>
          <div class="row">
            <label>Confirm Password:</label> <input id='field_pwd2' type='password' required pattern='(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}' name='password2' title="Nota: La password deve contenere 6 caratteri di cui almeno un numero, una maiuscola e una minuscola">
          </div>
          <div>
            <input type='hidden' name='action' value= 'register'>
            <input type='submit'  name = "register" value= 'register' onclick="return Account.checkJoin()">
          </div>
        </form>
      </div>
    </div>

    <script>
      Account.updateForm('Login');
    </script>     
    <%
    } else {%>
    <h1>Benvenuto <jsp:getProperty name="user" property="username" /></h1>
    <div>
      <form action="/PizzaWeb/Servlet" method="get">
        <input type="submit" name="action" value="logout">
      </form>
    </div>
    <%}%>
    <%if (Controller.checkLogin(request) && Controller.checkAdmin(request)) {%>
  <%--  <div id="terminale">
      <%=request.getSession().getAttribute("message")%>
    </div>
  --%>
    <%}%>

  </div>
  <div id="operazioni">  
    <h1>Operazioni disponibili</h1>
    <div class="menu_button button_catalogo" onclick="Menu.sendCatalogoRequest()" onmouseover="Menu.addEmpatize(event, this)" onmouseout="Menu.removeEmpatize(event,this)">Catalogo</div>
    <%if (Controller.checkLogin(request)) { %>   
    <div class="menu_button button_prenotazioni" onclick="Menu.sendPrenotazioniRequest()" onmouseover="Menu.addEmpatize(event, this)" onmouseout="Menu.removeEmpatize(event,this)" >Prenotazioni</div>
    <%if (Controller.checkAdmin(request)) { %>
    <div class="menu_button button_utenti" onclick="Menu.sendViewUtentiRequest()" onmouseover="Menu.addEmpatize(event, this)" onmouseout="Menu.removeEmpatize(event,this)" >Lista Utenti</div>
    <% }%>
    <%}%>
  </div>
</nav>