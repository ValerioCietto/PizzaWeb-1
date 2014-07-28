package mvc;

import components.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.*;


////////////////////////////////////////////////////////////////////////////////

public class Model {
    
    private final Database db;
    
    private Pizza pizza;
    private Utente utente;
    private Prenotazione prenotazione;
    
////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    public Model() throws SQLException{
        db = new Database();
    }

    
////////////////////////////////////////////////////////////////////////////////
// UTILITY MODEL
    
    public Utente login(String username, String password, HttpSession s) throws SQLException{
        Utente login = null;
        if (username != null && password != null && !username.equals("") && !password.equals("")) {
            login = db.checkLogin(username, password);
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
        return login;
    }

    public void register(String username, String password, HttpSession s){}
    
    public ArrayList<Pizza> getCatalogo() throws SQLException{
        return db.getCatalogo();
    }
    
    public ArrayList<Prenotazione> ottieniPrenotazioni(int id) throws SQLException{
        return db.getListaPrenotazioni(id);
    }
    
    
    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DI INSERIMENTO
    
    protected void addUtente(HttpServletRequest req) throws SQLException {
        String n = req.getParameter("username");
        String p = req.getParameter("pwd1");
        String ruolo = "user";
        utente = new Utente(n, p, ruolo);
        db.addUtente(utente); 
    }

    protected void addPizza(HttpServletRequest req) {
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

    protected void addPrenotazione(HttpServletRequest req) throws SQLException {
        String nomePizza = req.getParameter("pizza");
        String user = (String) req.getSession().getAttribute("username");
        int quantita = (Integer.parseInt(req.getParameter("quantita"))); //questo gli riempie solo un numero
        String data = req.getParameter("data");
       
        Pizza tmp = db.getPizza(nomePizza);
        int idPizza= tmp.getId();
        int idUser = db.getIdUtente(user);
        prenotazione= new Prenotazione(idUser, idPizza, quantita,  data);

        db.addPrenotazione(prenotazione);
    }

    
////////////////////////////////////////////////////////////////////////////////
// METODI DI ELIMINAZIONE
 
    protected void remUtente(HttpServletRequest req) throws SQLException {

        String user = req.getParameter("username");
        
        int idUser = db.getIdUtente(user);
        
        if (idUser > 0) {
            Utente tmp = db.getUtente(idUser);
            db.remUtente(tmp);
        }
    }

    protected void remPizza(HttpServletRequest req) throws SQLException{
        String nomePizza = req.getParameter("pizza");
        Pizza tmp = db.getPizza(nomePizza);
        try {
        db.remPizza(tmp);
            
        } catch (SQLException e) {
            (req.getSession()).setAttribute("message", "Problema sql");
        }
    }

    protected void remPrenotazione(HttpServletRequest req) throws SQLException {

        String user = req.getParameter("username");
        String nomePizza = req.getParameter("nomepizza");
        String data = req.getParameter("data");
        
        

        int idUser = db.getIdUtente(user);
        int idPizza = db.getIdPizza(nomePizza);
        int idPren = db.getIdPrenotazione(idUser, idPizza, data);
        if (idPren > 0) {
            Prenotazione tmp = db.getPrenotazione(idPren);
            db.remPrenotazione(tmp);
        }
    }

    
////////////////////////////////////////////////////////////////////////////////
// METODI DI MODIFICA
    
    protected void modUtente(HttpServletRequest req) throws SQLException {

        HttpSession s = req.getSession();
        if (req.getParameter("username") != null && !req.getParameter("username").equals("") &&
                req.getParameter("password") != null && !req.getParameter("password").equals("")) {
            
            int idUser = Integer.parseInt(req.getParameter("idUtente"));
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String permission = req.getParameter("permission");
            

            utente = db.getUtente(idUser);
            utente.setUsername(username);
            utente.setPwd(password);
            utente.setPermission(permission);
            try{
            db.modUtente(utente);
                s.setAttribute("message", "Utente modificato");
               
            } catch (SQLException e){s.setAttribute("message", "Problema sql");}
        } else {
            s.setAttribute("message", "inserisci i dati dell'utente.");
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected void modPizza(HttpServletRequest req) {

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

    protected void modPrenotazione(HttpServletRequest req) throws SQLException {

        HttpSession s = req.getSession();
        if (req.getParameter("pizza") != null && !req.getParameter("pizza").equals("") &&
                req.getParameter("username") != null && !req.getParameter("username").equals("") &&
                    req.getParameter("data") != null && !req.getParameter("data").equals("")) {
            
            String username = req.getParameter("username");
            String nomePizza = req.getParameter("nomepizza");
            String data = req.getParameter("data");

            prenotazione = db.getPrenotazione(db.getIdPrenotazione(db.getIdUtente(username), db.getIdPizza(nomePizza), data));
            
            String newData = req.getParameter("newData");
            int quantita = Integer.parseInt(req.getParameter("quantità"));
            String stato = req.getParameter("stato");
            
            prenotazione.setData(newData);
            prenotazione.setQuantita(quantita);
            prenotazione.setStato(stato);
            
            try{
            db.modPrenotazione(prenotazione);
                s.setAttribute("message", "Prenotazione modificata");
               
            } catch (SQLException e){s.setAttribute("message", "Problema sql");}
        } else {
            s.setAttribute("message", "inserisci un nome, gli ingredienti e il prezzo.");
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}
