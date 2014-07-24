package mvc;

import mvc.model.*;

import java.util.ArrayList;
import javax.servlet.http.*;
import java.lang.*;

public class Model {

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
    public static void login(HttpServletRequest req) {
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username != null && password != null && !username.equals("") && !password.equals("")) {
            if (DBManager.controllaLogin(username, password)) {
                Utente login = new Utente(DBManager.getLogin(username));
                //if(login.getNome() != null && !login.getNome().equals("")){
                s.setAttribute("username", login.getNome());
                s.setAttribute("permission", login.getPermission());
                s.setAttribute("message", "login effettuato, benvenuto " + login.getNome() + "!");
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
    public static void logout(HttpServletRequest req) {
        HttpSession s = req.getSession();
        s.invalidate();
        s = req.getSession();
        s.setAttribute("message", "logout effettuato");
    }

    /**
     * Estrapola il nome della pizza da eliminare Richiama il DB per rimuovare
     * la pizza --------------------- controllare che pizza non sia nullo
     *
     * @param req
     */
    public static void remPizza(HttpServletRequest req) {
        String pizza = req.getParameter("pizza");
        if (!DBManager.remPizza(pizza)) {
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
    public static void addPizza(HttpServletRequest req) {
        HttpSession s = req.getSession();
        if (req.getParameter("pizza") != null && !req.getParameter("pizza").equals("")) {
            String n = req.getParameter("pizza");
            String i = req.getParameter("ingredienti");
            Double p = Double.parseDouble(req.getParameter("prezzo"));

            if (DBManager.addPizza(n, i, p)) {
                s.setAttribute("message", "aggiunta pizza " + n);
                Pizza temp = new Pizza(DBManager.getIdPizza(n), n, i, p);
            } else {
                s.setAttribute("message", "Problema sql");
            }
        } else {
            s.setAttribute("message", "inserisci un nome, gli ingredienti e il prezzo.");
        }
    }

    static void modPizza(HttpServletRequest req) {

        HttpSession s = req.getSession();
        if (req.getParameter("pizza") != null && !req.getParameter("pizza").equals("")) {
            String n = req.getParameter("pizza");
            String i = req.getParameter("ingredienti");
            Double p = Double.parseDouble(req.getParameter("prezzo"));

            if (DBManager.modPizza(n, i, p)) {
                s.setAttribute("message", "Pizza modificata");
                Pizza temp = new Pizza(DBManager.getIdPizza(n), n, i, p);
            } else {
                s.setAttribute("message", "Problema sql");
            }
        } else {
            s.setAttribute("message", "inserisci un nome, gli ingredienti e il prezzo.");
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static void addUser(HttpServletRequest req) {
        String n = req.getParameter("username");
        String p = req.getParameter("pwd1");
        String ruolo = "user";
        DBManager.addLogin(n, p, ruolo); //To change body of generated methods, choose Tools | Templates.
    }

    static boolean checkLogin(String username) {
        if (DBManager.getIdUser(username) > 0) {
            return false;
        }
        return true;
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
    static void addPren(HttpServletRequest req) {
        String pizza = req.getParameter("pizza");
        String user = (String) req.getSession().getAttribute("username");
        int quantità = (Integer.parseInt(req.getParameter("quantita"))); //questo gli riempie solo un numero
        String data = req.getParameter("data");

        int idUser = DBManager.getIdUser(user);
        int idPizza = DBManager.getIdPizza(pizza);

        DBManager.addPrenotazione(idUser, idPizza, quantità, data);
    }

    static void remPren(HttpServletRequest req) {

        String user = req.getParameter("nomecliente");
        String pizza = req.getParameter("nomepizza");
        String data = req.getParameter("data");

        int idUser = DBManager.getIdUser(user);
        int idPizza = DBManager.getIdPizza(pizza);
        if (DBManager.getIdPrenotazione(idUser, idPizza, data) > 0) {
            DBManager.remPrenotazione(idUser, idPizza, data);
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
