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
    
    public void addUser(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //aggiungi pizza
            int tmp = dbman.addUser(u.getUsername(), u.getPassword(), u.getPermission());
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
    
    public void remUseer(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //rimuovi utente
            dbman.remUser(u.getId());
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
    
    public void modUser(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //aggiungi pizza
            dbman.modUser(u.getUsername(), u.getPassword(), u.getPermission());
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
    
    public Utente getUser(String name) throws SQLException{
        Utente tmp = null;
        try{
            dbman.openConnection();
            ResultSet rs = dbman.getUser(name);
            if(rs.getRow()>0)
                tmp= new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }
    
    public Utente getUser(int id) throws SQLException{
        Utente tmp = null;  
        try{
            dbman.openConnection();
            ResultSet rs = dbman.getUser(id);
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
     * @param pwd
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public Utente checkLogin(String usr, String pwd) throws SQLException{
        Utente tmp = null;  
        try{
            if(dbman.checkLogin(usr,pwd)){
            dbman.openConnection();
            ResultSet rs = dbman.getUser(usr);
            if(rs.getRow()>0)
                tmp= new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
            }
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Prende in input il nome della pizza e restituisce l'ID della pizza
     * 
     * @param nome      nome della pizza
     * 
     * @return          ritorna un valore intero che indica l'ID
     * @throws java.sql.SQLException
     */
    
    public int getIdPizza(String nome) throws SQLException{
        int tmp = -1;
        try{
            dbman.openConnection();
            tmp = dbman.getIdPizza(nome);
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     * 
     * @param username  nome utente
     * 
     * @return          ritorna un valore intero che indica l'ID
     * @throws java.sql.SQLException
     */
    
    public int getIdUser(String username) throws SQLException{
        int tmp = -1;
        try{
            dbman.openConnection();
            tmp = dbman.getIdUser(username);
        }finally{
            dbman.closeConnection();
        }
        return tmp;
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
     * @throws java.sql.SQLException
     */
    
    public int getIdPrenotazione(int username, int pizza, String  data) throws SQLException{
        int tmp = -1;
        try{
            dbman.openConnection();
            tmp = dbman.getIdPrenotazione(username, pizza, data);
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }
    
////////////////////////////////////////////////////////////////////////////////       
    
    /**
     * Aggiunge una prenotazione ad un cliente
     * 
     * @param p
     * @throws java.sql.SQLException
     * 
     */
    
    public void addPrenotazione(Prenotazione p) throws SQLException{
        try{
            dbman.openConnection();
            //aggiungi pizza
            int tmp = dbman.addPrenotazione(p.getIdUtente(), p.getIdPizza(), p.getQuantita(), p.getData());
            if(tmp >= 0){
                p.setIdPrenotazione(tmp);
            }
        }finally{
            dbman.closeConnection();
        }
    }

////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * Rimuove una prenotazione
     * 
     * @param p
     * @throws java.sql.SQLException
     */
    
    public void remPrenotazione(Prenotazione p) throws SQLException{
        try{
            dbman.openConnection();
            
            dbman.remPizza(p.getIdPrenotazione());
            p.setIdPrenotazione(-1);
        }finally{
            dbman.closeConnection();
        }
    }

////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * Rimuove una prenotazione
     * 
     * @param p
     * @throws java.sql.SQLException
     */
    
    public void modPrenotazione(Prenotazione p) throws SQLException{
        try{
            dbman.openConnection();

            dbman.modPrenotazione(p.getIdPrenotazione(), p.getQuantita(), p.getData());
        }finally{
            dbman.closeConnection();
        }
    }

////////////////////////////////////////////////////////////////////////////////  
}