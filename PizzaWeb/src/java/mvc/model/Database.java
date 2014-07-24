/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mvc.model;
import java.sql.SQLException;
import mvc.*;

/**
 *
 * @author jorkut
 */

public class Database {
    
    private final DBManager dbman;
    
    public Database(){
        dbman = new DBManager();
    }
    ////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Aggiunge una pizza nella tabella PIZZE del database
     * 
     * @param p
     * @throws java.sql.SQLException
     */
    
    public void addPizza(Pizza p) throws SQLException {
        try{
            dbman.openConnection();
            //aggiungi pizza
            int tmp = dbman.addPizza(p.getNome(), p.getIngredinti(), p.getPrezzo());
            if(tmp >= 0){
                p.setId(tmp);
            }
        }finally{
            dbman.closeConnection();
        }
    }

////////////////////////////////////////////////////////////////////////////////
   
    /**
     * Rimuove una pizza dalla tabella PIZZE
     * 
     * @param p
     * @throws java.sql.SQLException
     */
    
    public void remPizza(Pizza p) throws SQLException{
        try{
            dbman.openConnection();
            //rimuovi pizza
            dbman.remPizza(p.getId());
        }finally{
            dbman.closeConnection();
        }
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Modifica una pizza nella tabella PIZZE
     * 
     * @param p
     * @throws java.sql.SQLException
     */
    
    public void modPizza(Pizza p) throws SQLException{
        try{
            dbman.openConnection();
            //aggiungi pizza
            dbman.modPizza(p.getNome(), p.getIngredinti(), p.getPrezzo());
        }finally{
            dbman.closeConnection();
        }
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Inserisce un utente nella tabella UTENTI
     * 
     * @param nome      nome dell'utente
     * @param password  password dell'utente
     * @param ruolo     permessi dell'utente
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean addLogin(String nome, String password, String ruolo){
        return false;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Rimuove un utente dalla tabella UTENTI
     * 
     * @param nome      nome dell'utente
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean remLogin(String nome){
        return false;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Modifica un utente nella tabella UTENTI
     * 
     * @param nome      nome attuale dell'utente
     * @param nNome     nuovo nome dell'utente
     * @param nPassword nuova password dell'utente
     * @param nRuolo    nuovi permessi dell'utente;
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean modLogin(String nome, String nNome, String nPassword, String nRuolo){
        return false;
    }
    
////////////////////////////////////////////////////////////////////////////////

    /**
     * Recupera la lista degli utenti e genera un array di Stringhe con tutti i dati
     * 
     * @return          ritorna un ArrayList \<String\> contenente i risultati della query
     */
    
    public static boolean getAllLogin(){
        return false;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Ritorna una Stringa contenente l'utente
     * 
     * @param usr       nome dell'utente
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     */
    
    public static boolean getLogin(String usr){
        return false;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Effettua un controllo sul login dell'utente
     * 
     * @param usr       nome dell'utente
     * @param pwd       password dell'utente
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean controllaLogin(String usr,String pwd){
        return false;
    }

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Prende in input il nome della pizza e restituisce l'ID della pizza
     * 
     * @param nome      nome della pizza
     * 
     * @return          ritorna un valore intero che indica l'ID
     */
    
    public static int getIdPizza(String nome){
        return -1;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     * 
     * @param username  nome utente
     * 
     * @return          ritorna un valore intero che indica l'ID
     */
    
    public static int getIdUser(String username){
        return -1;
    }
    
////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     * 
     * @param username  ID dell'utenteutente
     * @param pizza     ID della pizza
     * @param data      Data della prenotazione
     * 
     * @return          ritorna un valore intero che indica l'ID
     */
    
    public static int getIdPrenotazione(int username, int pizza, String  data){
        return -1;
    }
    
////////////////////////////////////////////////////////////////////////////////       
    
    /**
     * Aggiunge una prenotazione ad un cliente
     * 
     * @param cliente   cliente che effettua la prenotazione
     * @param data      data della prenotazione
     * @param pizza     tipo di pizza prenotata
     * @param quantita  quantità di pizze prenotate
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean addPrenotazione(int cliente, int pizza, int quantita, String data){
        return false;
    }

////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * Rimuove una prenotazione
     * 
     * @param cliente   cliente dal quale rimuovere la prenotazione
     * @param data      data della prenotazione da rimuovere
     * @param pizza     pizza da rimuovere
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean remPrenotazione(int cliente, int pizza, String data){
        return false;
    }

////////////////////////////////////////////////////////////////////////////////    

}