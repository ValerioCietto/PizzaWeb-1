package mvc;

import mvc.model.*;

import javax.servlet.http.*;
import java.sql.SQLException;

public class Model {
    private Database db;
    private Pizza pizza;
    private Utente utente;
    private Prenotazione prenotazione;
    
    public Model() throws SQLException{
        db = new Database();
    }

    /**
     * Estrapola dalla request gli input password e utente Controlla che non
     * siano nulli Crea un'oggetto utente(con parametri vuoti se usr o pwd
     * errati) Controlla la riuscita del punto precedente Carica i parametri
     * nella sessione ////////// -da aggiungere il controllo del se è gia
     * connesso -forse passare oggetto invece che stringhe
     *
     * @param req
     */
    // FACOLTATIVO : GESTIRE LOGIN CON POST
  
    
    
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

    /**
     * Invalida i dati di sessione Genera una nuova sessione
     *
     * @param req
     */


    /**
     * Estrapola il nome della pizza da eliminare Richiama il DB per rimuovare
     * la pizza --------------------- controllare che pizza non sia nullo
     *
     * @param req
     */
    public void remPizza(HttpServletRequest req) throws SQLException{
        String pizza = req.getParameter("pizza");
        Pizza tmp = db.getPizza(pizza);
        int idpizza= tmp.getId();
        try {
        db.remPizza(tmp);
            
        } catch (SQLException e) {
            (req.getSession()).setAttribute("message", "Problema sql");
        }
    }

    /**
     * Estrapola il nome, ingredienti e prezzo della pizza da aggiungere Crea
     * un'oggetto pizza con i parametri sopra(nel caso non riesca i valori sono
     * vuoti) Controlla la riuscita della creazione della pizza Carica la pizza
     * nel DB
     *
     * @param req
     */
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

   

    /**
     * Estrapola il nome della pizza e la quantità, il nome del cliente e l'ora
     * di prenotazione Carica un solo tipo di pizza nel database
     *
     * /////////////// Manca creazione oggetto Prenotazione Manca controllo dei
     * dati
     *
     * @param req
     */
    void addPren(HttpServletRequest req) throws SQLException {
        String pizza = req.getParameter("pizza");
        String user = (String) req.getSession().getAttribute("username");
        int quantita = (Integer.parseInt(req.getParameter("quantita"))); //questo gli riempie solo un numero
        String data = req.getParameter("data");
       
        Pizza tmp = db.getPizza(pizza);
        int idPizza= tmp.getId();
        int idUser = db.getIdUser(user);
        prenotazione= new Prenotazione(idUser, idPizza, quantita,  data);

        db.addPrenotazione(prenotazione);
    }

    void remPren(HttpServletRequest req) throws SQLException {

        String user = req.getParameter("nomecliente");
        String pizza = req.getParameter("nomepizza");
        String data = req.getParameter("data");
        
        

        int idUser = db.getIdUser(user);
        int idPizza = db.getIdPizza(pizza);
        int idPren = db.getIdPrenotazione(idUser, idPizza, data);
        if (idPren > 0) {
            Prenotazione tmp = db.getPrenotazione(idPren);
            db.remPrenotazione(tmp);
        }
    }

}

//TEST
/*    
 public static void main(String[] args){
 Utente user;
 Pizza pizza;
 Prenotazione ordine;
        
 pizza = new Pizza(1, "margherita" , "tanta cacca al formaggio" , 4.67);
 user = new Utente(7, "gigimarzullo", "Puccicucci9", "utente");
 ordine = new Prenotazione(pizza.getId(), user.getId(), 5, 71, "34/14/2034");
        
 System.out.println(pizza.toString());
 System.out.println(user.toString());
 System.out.println(ordine.toString());
        
 }
 */
