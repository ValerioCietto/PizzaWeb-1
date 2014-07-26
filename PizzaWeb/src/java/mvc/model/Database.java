package mvc.model;
import java.sql.*;
import java.util.ArrayList;
import mvc.*;

public class Database {
    
    private final DBManager dbman;
    
    
    public Database() throws SQLException{
        dbman = new DBManager();
    }
    
////////////////////////////////////////////////////////////////////////////////
    // UTILITY DATABASE
    
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
            if(dbman.checkLogin(usr,pwd) > 0){
            dbman.openConnection();
            ResultSet rs = dbman.getUser(usr);
            if(rs.next())
                tmp= new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
            }
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }

    /**
     * Popola il Database con dati di default
     */
    
    public void startDati(){}
    
    /**
     * Fornisce il catalogo delle pizze
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public ArrayList<Pizza> getCatalogo() throws SQLException{
        ArrayList<Pizza> catalogo = new ArrayList(); 
        try{
            dbman.openConnection();
            ResultSet rs = dbman.query("SELECT * FROM PIZZE");
            while(rs.next())
               catalogo.add(new Pizza(rs.getInt("IDPIZZA"), rs.getString("NOME"), rs.getString("INGREDIENTI"), rs.getDouble("PREZZO")));            
        }finally{
            dbman.closeConnection();
        }
        return catalogo;
    }
    
    /**
     *  Fornisce la lista di prenotazioni associate ad uno specifico utente
     * @param idUtente
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public ArrayList<Prenotazione> getPrenotazioni(int idUtente) throws SQLException{
        ArrayList<Prenotazione> listaPren = new ArrayList(); 
        try{
            dbman.openConnection();
            ResultSet rs = dbman.query("SELECT * FROM PRENOTAZIONI WHERE IDUTENTE="+idUtente);
            while(rs.next())
               listaPren.add(new Prenotazione(rs.getInt("IDPRENOTAZIONE"),rs.getInt("IDUTENTE"),rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA")));            
        }finally{
            dbman.closeConnection();
        }
        return listaPren;
    }
    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    // METODI DI INSERIMENTO (OK)
    
    /**
     * Inserisce un utente nella tabella UTENTI
     * 
     * @param u
     * @throws java.sql.SQLException
     */
    
    public void addUser(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            if(dbman.getIdUser(u.getUsername()) < 0){
                //aggiungi utente
                int tmp = dbman.addUser(u.getUsername(), u.getPassword(), u.getPermission());
                if(tmp >= 0){
                    u.setId(tmp);
                }
            }
            else
                System.out.println("Username non valido");
        }finally{
            dbman.closeConnection();
        }
    }
    
    /**
     * Aggiunge una pizza nella tabella PIZZE del database
     * 
     * @param p
     * @throws java.sql.SQLException
     */
    
    public void addPizza(Pizza p) throws SQLException {
        try{
            dbman.openConnection();
            if(dbman.getIdPizza(p.getNome()) < 0){
                //aggiungi pizza
                int tmp = dbman.addPizza(p.getNome(), p.getIngredinti(), p.getPrezzo());
                if(tmp >= 0){
                    p.setId(tmp);
                }
            }
            else
                System.out.println("Pizza già presente");
        }finally{
            dbman.closeConnection();
        }
    }      
    
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
            if(dbman.getIdPrenotazione(p.getIdUtente(), p.getIdPizza(), p.getData()) < 0){
                //aggiungi prenotazione
                int tmp = dbman.addPrenotazione(p.getIdUtente(), p.getIdPizza(), p.getQuantita(), p.getData());
                if(tmp >= 0){
                    p.setIdPrenotazione(tmp);
                }
            }
            else
                System.out.println("Prenotazione non valida");
        }finally{
            dbman.closeConnection();
        }
    }
    
    
////////////////////////////////////////////////////////////////////////////////
    // METODI DI RIMOZIONE (OK)
    
    /**
     * Rimuove un utente dalla tabella UTENTI
     * 
     * @param u
     * @throws java.sql.SQLException
     */
    
    public void remUser(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //rimuovi utente
            dbman.remUser(u.getId());
            u.setId(-1);
        }finally{
            dbman.closeConnection();
        }
    }
   
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
    
    /**
     * Rimuove una prenotazione
     * 
     * @param p
     * @throws java.sql.SQLException
     */
    
    public void remPrenotazione(Prenotazione p) throws SQLException{
        try{
            dbman.openConnection();
            dbman.remPrenotazione(p.getIdPrenotazione());
            p.setIdPrenotazione(-1);
        }finally{
            dbman.closeConnection();
        }
    }
    
    
////////////////////////////////////////////////////////////////////////////////
    // METODI DI MODIFICA (OK)
    
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
    // METODI DI GET ID (OK)
    
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
    // METODI DI GET (OK)
    
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
            if(rs.next())
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
            if(rs.next())
                tmp= new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }
    
    
    public Pizza getPizza(String name) throws SQLException {
        Pizza result = null;
        try{
            dbman.openConnection();
            ResultSet rs = dbman.getPizza(name);
            if(rs.next())
                result = new Pizza( rs.getInt("IDPIZZA"), rs.getString("NOME"), rs.getString("INGREDIENTI"), rs.getDouble("PREZZO"));
        }finally{
            dbman.closeConnection();
        }
        return result;
    }
    
    public Prenotazione getPrenotazione(int id) throws SQLException{
        Prenotazione tmp = null;  
        try{
            dbman.openConnection();
            ResultSet rs = dbman.getPrenotazione(id);
            if(rs.next())
                tmp= new Prenotazione(rs.getInt("IDPRENOTAZIONE"), rs.getInt("IDUTENTE"), rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA"));
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}