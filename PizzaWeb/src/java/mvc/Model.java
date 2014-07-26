package mvc;

import components.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.*;

/**
 * 
 * @author Alessandro
 */

public class Model {
    private final Database db;
    private Pizza pizza;
    private Utente utente;
    private Prenotazione prenotazione;
    
    private ArrayList<Pizza> catalogo;
    private ArrayList<Pizza> listaPrenotazioni;
    
    public Model() throws SQLException{
        db = new Database();
    }

    public void loginModel(String username, String password, HttpSession s) throws SQLException{
        if (username != null && password != null && !username.equals("") && !password.equals("")) {
            Utente login = db.checkLogin(username, password);
            if (login!=null) {
                s.setAttribute("username", login.getUsername());
                s.setAttribute("permission", login.getPermission());
                s.setAttribute("message", "login effettuato, benvenuto " + login.getUsername() + "!");
            } else {
                s.setAttribute("message", "login errato, sicuro di esserti registrato?");
            }
        } else {
            s.setAttribute("message", "inserisci il tuo nome utente e la tua password.");
        }
    }

    public void remPizza(HttpServletRequest req) throws SQLException{
        String nomePizza = req.getParameter("pizza");
        Pizza tmp = db.getPizza(nomePizza);
        try {
        db.remPizza(tmp);
            
        } catch (SQLException e) {
            (req.getSession()).setAttribute("message", "Problema sql");
        }
    }

    public void addPizza(HttpServletRequest req) {
        HttpSession s = req.getSession();
        if (req.getParameter("pizza") != null && !req.getParameter("pizza").equals("")) {
            String n = req.getParameter("pizza");
            String i = req.getParameter("ingredienti");
            Double p = Double.parseDouble(req.getParameter("prezzo"));
            
            pizza = new Pizza (n, i, p);
            
            try {
                db.addPizza(pizza);
                s.setAttribute("message", "aggiunta pizza " + n);
            } catch (SQLException e){s.setAttribute("message", "Problema sql");}
                
        }
        else {
            s.setAttribute("message", "inserisci un nome, gli ingredienti e il prezzo.");
        }
    }

    void modPizza(HttpServletRequest req) {

        HttpSession s = req.getSession();
        if (req.getParameter("pizza") != null && !req.getParameter("pizza").equals("")) {
            String n = req.getParameter("pizza");
            String i = req.getParameter("ingredienti");
            Double p = Double.parseDouble(req.getParameter("prezzo"));

            pizza = new Pizza (n, i, p);
            try{
            db.modPizza(pizza);
                s.setAttribute("message", "Pizza modificata");
               
            } catch (SQLException e){s.setAttribute("message", "Problema sql");}
        } else {
            s.setAttribute("message", "inserisci un nome, gli ingredienti e il prezzo.");
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void addUser(HttpServletRequest req) throws SQLException {
        String n = req.getParameter("username");
        String p = req.getParameter("pwd1");
        String ruolo = "user";
        utente = new Utente(n, p, ruolo);
        db.addUser(utente); 
    }

    void addPren(HttpServletRequest req) throws SQLException {
        String nomePizza = req.getParameter("pizza");
        String user = (String) req.getSession().getAttribute("username");
        int quantita = (Integer.parseInt(req.getParameter("quantita"))); //questo gli riempie solo un numero
        String data = req.getParameter("data");
       
        Pizza tmp = db.getPizza(nomePizza);
        int idPizza= tmp.getId();
        int idUser = db.getIdUser(user);
        prenotazione= new Prenotazione(idUser, idPizza, quantita,  data);

        db.addPrenotazione(prenotazione);
    }

    void remPren(HttpServletRequest req) throws SQLException {

        String user = req.getParameter("nomecliente");
        String nomePizza = req.getParameter("nomepizza");
        String data = req.getParameter("data");
        
        

        int idUser = db.getIdUser(user);
        int idPizza = db.getIdPizza(nomePizza);
        int idPren = db.getIdPrenotazione(idUser, idPizza, data);
        if (idPren > 0) {
            Prenotazione tmp = db.getPrenotazione(idPren);
            db.remPrenotazione(tmp);
        }
    }

}
