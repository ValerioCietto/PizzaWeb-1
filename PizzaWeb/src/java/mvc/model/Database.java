/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mvc.model;
import java.sql.*;
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
            p.setId(-1);
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
     * @param u
     * @throws java.sql.SQLException
     */
    
    public void addLogin(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //aggiungi pizza
            int tmp = dbman.addLogin(u.getUsername(), u.getPassword(), u.getPermission());
            if(tmp >= 0){
                u.setId(tmp);
            }
        }finally{
            dbman.closeConnection();
        }
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Rimuove un utente dalla tabella UTENTI
     * 
     * @param u
     * @throws java.sql.SQLException
     */
    
    public void remLogin(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //rimuovi utente
            dbman.remLogin(u.getId());
            u.setId(-1);
        }finally{
            dbman.closeConnection();
        }
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Modifica un utente nella tabella UTENTI
     * 
     * @param u
     * @throws java.sql.SQLException
     */
    
    public void modLogin(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //aggiungi pizza
            dbman.modLogin(u.getUsername(), u.getPassword(), u.getPermission());
        }finally{
            dbman.closeConnection();
        }
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Ritorna una Stringa contenente l'utente
     * 
     * @param name
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public Utente getLogin(String name) throws SQLException{
        Utente tmp = null;
        try{
            dbman.openConnection();
            ResultSet rs = dbman.getLogin(name);
            if(rs.getRow()>0)
                tmp= new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }
    
    public Utente getLogin(int id) throws SQLException{
        Utente tmp = null;  
        try{
            dbman.openConnection();
            ResultSet rs = dbman.getLogin(id);
            if(rs.getRow()>0)
                tmp= new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
        }finally{
            dbman.closeConnection();
        }
        return tmp;
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