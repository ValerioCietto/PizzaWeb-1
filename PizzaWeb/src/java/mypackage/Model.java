package mypackage;

import java.util.ArrayList;
import javax.servlet.http.*;
import java.lang.*;

public class Model {
    
    /**
     * Estrapola dalla request gli input password e utente
     * Controlla che non siano nulli
     * Crea un'oggetto utente(con parametri vuoti se usr o pwd errati)
     * Controlla la riuscita del punto precedente
     * Carica i parametri nella sessione
     * //////////
     * -da aggiungere il controllo del se è gia connesso
     * -forse passare oggetto invece che stringhe
     * @param req 
     */
    
    public static void login(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if(username != null && password != null && !username.equals("") && !password.equals("")){
            Utente login = new Utente( DBManager.getLogin(username,password) );
            if(login.getNome() != null && !login.getNome().equals("")){
                s.setAttribute("username",login.getNome());
                s.setAttribute("permission", login.getPermission());
                s.setAttribute("message","login effettuato, benvenuto "+login.getNome()+"!");
            }else
                s.setAttribute("message","login errato, sicuro di esserti registrato?");
        }else
            s.setAttribute("message","inserisci il tuo nome utente e la tua password.");
    }
    
    
    /**
     * Invalida i dati di sessione
     * Genera una nuova sessione
     * @param req 
     */
    
    public static void logout(HttpServletRequest req){
        HttpSession s = req.getSession();
        s.invalidate();
        s = req.getSession();
        s.setAttribute("message", "logout effettuato");
    }
    
    /**
     * Estrapola il nome della pizza da eliminare
     * Richiama il DB per rimuovare la pizza
     * ---------------------
     * controllare che pizza non sia nullo
     * @param req 
     */
    
    public static void remPizza(HttpServletRequest req){
        String pizza = req.getParameter("pizza");
        if(!DBManager.remPizza(pizza))
            (req.getSession()).setAttribute("message","Problema sql");
    }
    
    /**
     * Estrapola il nome, ingredienti e prezzo della pizza da aggiungere
     * Crea un'oggetto pizza con i parametri sopra(nel caso non riesca i valori sono vuoti)
     * Controlla la riuscita della creazione della pizza
     * Carica la pizza nel DB
     * @param req 
     */
    
    public static void addPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        if (req.getParameter("pizza") != null && !req.getParameter("pizza").equals("")){
            String n = req.getParameter("pizza");
            String i = req.getParameter("ingredienti");
            Double p = Double.parseDouble(req.getParameter("prezzo"));
            
            if(DBManager.addPizza(n, i, p)){
               s.setAttribute("message","aggiunta pizza "+n);
               Pizza temp = new Pizza(Integer.parseInt(DBManager.getIdPizza(n)), n, i, p);
            }
            else
               s.setAttribute("message","Problema sql");
        }else
            s.setAttribute("message","inserisci un nome, gli ingredienti e il prezzo.");
    } 

    static void modPizza(HttpServletRequest req){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    static void addUser(HttpServletRequest req){
        String n = req.getParameter("username");
        String p = req.getParameter("pwd1");
        String ruolo = "user";
        DBManager.addLogin(n, p, ruolo); //To change body of generated methods, choose Tools | Templates.
    }
    
    static boolean checkLogin(String username){
        if(DBManager.getIdUser(username) != null){
            return false;
        }
        return true;
    }
    
    /**
     * Estrapola il nome della pizza e la quantità, il nome del cliente e l'ora di prenotazione
     * Carica un solo tipo di pizza nel database
     * 
     * ///////////////
     * Manca creazione oggetto Prenotazione
     * Manca controllo dei dati
     * 
     * @param req 
     */
    
    static void addPren(HttpServletRequest req){
        String pizza = req.getParameter("pizza");
        String user =(String)req.getSession().getAttribute("username");
        int quantità = (Integer.parseInt(req.getParameter("quantita"))); //questo gli riempie solo un numero
        String data = req.getParameter("data");
        
        int idUser = Integer.parseInt(DBManager.getIdUser(user));
        int idPizza = Integer.parseInt(DBManager.getIdPizza(pizza));
        
        DBManager.addPrenotazione(idUser, idPizza, quantità, data);
    }
    
    static void remPren(HttpServletRequest req){
        
        String user = req.getParameter("nomecliente");
        String pizza = req.getParameter("nomepizza");
        String data = req.getParameter("data");

        int idUser = Integer.parseInt(DBManager.getIdUser(user));
        int idPizza = Integer.parseInt(DBManager.getIdPizza(pizza));
        
        DBManager.remPrenotazione(idUser, idPizza, data);
    }
        
    
}
                                //FINE CLASSE MODEL//
////////////////////////////////////////////////////////////////////////////////
                                //INIZIO CLASSE PIZZA//

class Pizza{
    private int idPizza;
    private String nome;
    private String ingredienti;
    private double prezzo;
    
    /**
     * Controlla che gli input non siano nulli o non accettabili
     * In caso di non riuscita i parametri sono nulli
     * Altrimenti carica i dati nell'oggetto
     * 
     * @param id
     * @param iNome
     * @param iIngredienti
     * @param iPrezzo
     * 
     * NB: i controlli in input vanno fatti PRIMA DELLA CREAZIONE DELL'OGGETTO
     */
    
    public Pizza(int id, String iNome,String iIngredienti, double iPrezzo){
        this.idPizza = id;
        this.ingredienti = iIngredienti;
        this.nome = iNome;
        this.prezzo = iPrezzo;
    }

    // METODI DI GET
    
    /**Restituisce l'ID della pizza
     * @return <String> nome pizza
     */
    
    public int getId(){
        return this.idPizza;
    }   
    
    /**Restituisce il nome della pizza
     * @return <String> nome pizza
     */
    
    public String getNome(){
        return this.nome;
    }
 
    /**Restituisce gli ingredienti della pizza
     * @return <String> nome pizza
     */
    
    public String getIngredinti(){
        return this.ingredienti;
    }
    
    /**Restituisce il prezzo della pizza
     * @return <double> prezzo
     */
    
    public double getPrezzo(){
        return this.prezzo;
    }

    //METODI DI SET
    
    //Modifica il nome della pizza
    
    public void setNome(String newNome){
        this.nome = newNome;
    }
 
    //Modifica gli ingredienti della pizza
    
    public void setIngredinti( String newIngredienti){
        this.ingredienti = newIngredienti;
    }
    
    //Modifica il prezzo della pizza
    
    public void setPrezzo( double newPrezzo){
        this.prezzo = newPrezzo;
    }
   
}

                                //FINE CLASSE PIZZA//
////////////////////////////////////////////////////////////////////////////////
                                //INIZIO CLASSE UTENTE//

class Utente{
    private int idUtente;
    private String username;
    private String pwd;
    private String permission;
    
    /**
     * Crea l'utente
     * @param iNome
     * @param iPwd
     * @param iRuolo
     * 
     * NB: i controlli in input vanno fatti PRIMA DELLA CREAZIONE DELL'OGGETTO
     */
    
    public Utente(int id, String iNome, String iPwd, String iRuolo){
        this.idUtente = id;
        this.username = iNome;
        this.pwd = iPwd;
        this.permission = iRuolo;
    }
    
    public Utente(String query){
        String[] tmp = query.split(";");
        this.idUtente = Integer.parseInt(tmp[0]);
        this.username = tmp[1];
        this.pwd = tmp[2];
        this.permission = tmp[3];
    }
 
    //METODI DI GET    
    
    /**
     * Restituisce l'ID dell'utente
     * @return int nome Pizza
     */
    
    public int getId(){
        return idUtente;
    }
    
    /**
     * Restituisce il nome dell'utente
     * @return <String> nome Pizza
     */
    
    public String getNome(){
        return username;
    }
    
    /**
     * Restituisce la password dell'utente
     * @return <String>
     */
    
    public String getPwd(){
        return pwd;
    }
    
    /**
     * Restituisce il ruolo dell'utente
     * @return <String>
     */
    
    public String getPermission(){
        return permission;
    }
   
    //METODI DI SET
    
    //Modifica la password dell'utente
    
    public void setPwd( String newPwd){
        this.pwd = newPwd;
    }
    
    //Modifica il ruolo dell'utente
    
    public void setPermission(String newRuolo){
        this.permission = newRuolo;
    }
}



                                //FINE CLASSE UTENTE//
////////////////////////////////////////////////////////////////////////////////
                            //INIZIO CLASSE PRENOTAZIONE//

class Prenotazione{
    private int idPrenotazione;
    private int idUtente;
    private int idPizza;
    private int quantita;
    private String data;
    private String stato;
    
    
    /**
     * Crea la prenotazione
     * 
     * @param idUtente
     * @param iPizza
     * @param quantita
     * @param data
     * 
     * NB: i controlli in input vanno fatti PRIMA DELLA CREAZIONE DELL'OGGETTO
     */ 
    
    
    public Prenotazione(int id, int idU, int idP, int iQuantita, String iData){
        this.idPrenotazione = id;
        this.idUtente = idU;
        this.idPizza = idP;
        this.quantita = iQuantita;
        this.data = iData;
        this.stato = "Ordinata";
    }

/**
     * Restituisce l'ID della prenotazione
     * @return int idPrenotazione
     */
    
    public int getIdPrenotazione(){
        return idPrenotazione;
    }
    
    /**
     * Restituisce l'ID dell'utente che ha effettuato la prenotazione
     * @return <int> idUtente
     */
    
    public int getIdUtente(){
        return idUtente;
    }
    
    /**
     * Restituisce l'ID della pizza prenotata
     * @return <int> idPizza
     */
    
    public int getIdPizza(){
        return idPizza;
    }
    
    /**
     * Restituisce la quantità di pizze prenotate
     * @return <int> quantita
     */
    
    public int getQuantita(){
        return quantita;
    }
    
    /**
     * Restituisce la data della prenotazione
     * @return <String> data
     */
    
    public String getData(){
        return data;
    }

    /**
     * Restituisce lo stato della prenotazione
     * @return <String> stato
     */    
    
    public String getStato(){
        return stato;
    }      
    
}
