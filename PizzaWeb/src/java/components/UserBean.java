package components;

import mvc.*;
import java.util.ArrayList;

/**
 *
 * da mettere sotto <!Doctype...> a ogni jsp
 * <jsp:useBean id="user" scope="session" class="UserBean"/>
 *
 * la riga seguente esegue setXxx(request.getParameter(xxx)) di tutti i set
 * della classe essendoci
 * <jsp:setProperty name="user" property="*"/>
 * 
 * <jsp:getProperty name="user" property="*"/>
 *
 * evitando i codici strani, per richiamare il metodo Xxx usa
 * <% user.Xxx(...) %>
 */
public class UserBean {

  private String username;
  private String ruolo;
  private String view;
  private ArrayList<String> message = new ArrayList<>();
  private ArrayList<Pizza> carrello = new ArrayList<Pizza>();

  public UserBean() {
  }

  public String getUsername() {
    return username;
  }

  public String getRuolo() {
    return ruolo;
  }

  
  
  public void setUsername(String username) {
    this.username = username;
  }

  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }

  public void setView(String view) {
    this.view = view;
  }

  public void setMessage(String message) {
    this.message.add(message);
  }

  public String getMessage() {
    String listString = "";
    for (String s : message) {
      listString += s;
    }
    //clear message
    message.clear();
    return listString;
  }

  public String getView() {
    return view;
  }

  public void addPizza(Pizza p) {
    carrello.add(p);
  }

  public void svuota() {
    carrello.clear();
  }

  public ArrayList<Pizza> getCarrello() {
    return carrello;
  }
}
