package mvc;

import components.Pizza;
import java.util.ArrayList;

/**
 *
 * da mettere sotto <!Doctype...> a ogni jsp
 * <jsp:useBean id="user" scope="session" class="User"/>
 *
 * la riga seguente esegue setXxx(request.getParameter(xxx)) di tutti i set
 * della classe essendoci
 * <jsp:setProperty name="user" property="*"/>
 *
 * evitando i codici strani, per richiamare il metodo Xxx usa
 * <% user.Xxx(...) %>
 */
public class User {

    private String username;
    private String password;
    private String ruolo;
    private String view;
    private String message;
    private ArrayList<Pizza> carrello = new ArrayList<Pizza>();

    public User() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getView() {
        return "ritorna html adatto";
    }

    public String getMessage() {
        return message;
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
