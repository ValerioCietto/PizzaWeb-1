package components;

import java.sql.*;
import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////

public final class Database {

    private final DBManager dbman;
    private  ArrayList<Utente> listaUtenti;
    private  ArrayList<Pizza> listaPizze;
    private  ArrayList<Prenotazione> listaPrenotazioni;

////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    public Database() throws SQLException{
        dbman = new DBManager();
        listaUtenti = getListaUtenti();
        listaPizze = getCatalogo();
        listaPrenotazioni = getListaPrenotazioni();   
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
     * 
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
     * Fornisce la lista di tutti gli utenti
     * 
     * @return 
     * 
     * @throws java.sql.SQLException 
     */
    
    protected ArrayList<Utente> getListaUtenti() throws SQLException{
        ArrayList<Utente> listaUtenti = new ArrayList(); 
        try{
            dbman.openConnection();
            ResultSet rs = dbman.query("SELECT * FROM UTENTI");
            while(rs.next())
               listaUtenti.add(new Utente(rs.getInt("IDUSER"),rs.getString("USERNAME"),rs.getString("PASSWORD"), rs.getString("PERMISSION")));            
        }finally{
            dbman.closeConnection();
        }
        return listaUtenti;
    }
    
    /**
     * Fornisce il catalogo delle pizze
     * 
     * @return 
     * 
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
     *  Fornisce la lista di tutte le prenotazioni
     * 
     * @return 
     * 
     * @throws java.sql.SQLException 
     */
    
    public ArrayList<Prenotazione> getListaPrenotazioni() throws SQLException{
        ArrayList<Prenotazione> listaPren = new ArrayList(); 
        try{
            dbman.openConnection();
            ResultSet rs = dbman.query("SELECT * FROM PRENOTAZIONI");
            while(rs.next())
               listaPren.add(new Prenotazione(rs.getInt("IDPRENOTAZIONE"),rs.getInt("IDUTENTE"),rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA")));            
        }finally{
            dbman.closeConnection();
        }
        return listaPren;
    }
    
    /**
     *  Fornisce la lista di prenotazioni associate ad uno specifico utente
     * 
     * @param idUtente
     * 
     * @return 
     * 
     * @throws java.sql.SQLException 
     */
    
    public ArrayList<Prenotazione> getListaPrenotazioni(int idUtente) throws SQLException{
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
     * Inserisce un utente nella tabella UTENTI e lo aggiunge in lista
     * 
     * @param u
     * 
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
                    listaUtenti.add(u);
                }
            }
            else
                System.out.println("Username non valido");
        }finally{
            dbman.closeConnection();
        }
    }
    
    /**
     * Aggiunge una pizza nella tabella PIZZE del database e lo aggiunge in lista
     * 
     * @param p
     * 
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
                    listaPizze.add(p);
                }
            }
            else
                System.out.println("Pizza già presente");
        }finally{
            dbman.closeConnection();
        }
    }      
    
    /**
     * Aggiunge una prenotazione ad un cliente e la aggiunge in lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public void addPrenotazione(Prenotazione p) throws SQLException{
        try{
            dbman.openConnection();
            if(dbman.getIdPrenotazione(p.getIdUtente(), p.getIdPizza(), p.getData()) < 0){
                //aggiungi prenotazione
                int tmp = dbman.addPrenotazione(p.getIdUtente(), p.getIdPizza(), p.getQuantita(), p.getData());
                if(tmp >= 0){
                    p.setIdPrenotazione(tmp);
                    listaPrenotazioni.add(p);
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
     * Rimuove un utente dalla tabella UTENTI e dalla lista
     * 
     * @param u
     * 
     * @throws java.sql.SQLException
     */
    
    public void remUser(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            //rimuovi utente
            dbman.remUser(u.getId());
            u.setId(-1);
            listaUtenti.remove(u);
        }finally{
            dbman.closeConnection();
        }
    }
   
    /**
     * Rimuove una pizza dalla tabella PIZZE e dalla lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public void remPizza(Pizza p) throws SQLException{
        try{
            dbman.openConnection();
            //rimuovi pizza
            dbman.remPizza(p.getId());
            p.setId(-1);
            listaPizze.remove(p);
        }finally{
            dbman.closeConnection();
        }
    }   
    
    /**
     * Rimuove una prenotazione e dalla lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public void remPrenotazione(Prenotazione p) throws SQLException{
        try{
            dbman.openConnection();
            dbman.remPrenotazione(p.getIdPrenotazione());
            p.setIdPrenotazione(-1);
            listaPrenotazioni.remove(p);
        }finally{
            dbman.closeConnection();
        }
    }
    
    
////////////////////////////////////////////////////////////////////////////////
// METODI DI MODIFICA (OK)
    
    /**
     * Modifica un utente nella tabella UTENTI e ricrea la lista
     * 
     * @param u
     * 
     * @throws java.sql.SQLException
     */
    
    public void modUser(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            dbman.modUser(u.getUsername(), u.getPassword(), u.getPermission());
            listaUtenti = getListaUtenti();
        }finally{
            dbman.closeConnection();
        }
    }
    
    /**
     * Modifica una pizza nella tabella PIZZE e ricrea la lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public void modPizza(Pizza p) throws SQLException{
        try{
            dbman.openConnection();
            dbman.modPizza(p.getNome(), p.getIngredinti(), p.getPrezzo());
            listaPizze = getCatalogo();
        }finally{
            dbman.closeConnection();
        }
    }
    
    /**
     * Modifica una prenotazione e ricrea la lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public void modPrenotazione(Prenotazione p) throws SQLException{
        try{
            dbman.openConnection();
            dbman.modPrenotazione(p.getIdPrenotazione(), p.getQuantita(), p.getData());
            listaPrenotazioni = getListaPrenotazioni();
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
     * 
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
     * 
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
     * Prende in input l'ID utente, l'ID pizza e la data e restituisce l'ID della prenotazione
     * 
     * @param username  ID dell'utenteutente
     * @param pizza     ID della pizza
     * @param data      Data della prenotazione
     * 
     * @return          ritorna un valore intero che indica l'ID
     * 
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
     * Ritorna un oggetto Utente partendo dall'username
     * 
     * @param name
     * 
     * @return          ritorna un Oggetto contenente il risultato della query
     * 
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
    
    /**
     * Ritorna un oggetto Utente partendo dall'ID
     * 
     * @param id
     * 
     * @return          ritorna un Oggetto contenente il risultato della query
     * 
     * @throws java.sql.SQLException
     */
    
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
    
    /**
     * Ritorna un oggetto Pizza partendo dal nome
     * 
     * @param name
     * 
     * @return          ritorna un Oggetto contenente il risultato della query
     * 
     * @throws java.sql.SQLException
     */
    
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
    
    /**
     * Ritorna un oggetto Prenotazione partendo dall'ID
     * 
     * @param id
     * 
     * @return          ritorna un Oggetto contenente il risultato della query
     * 
     * @throws java.sql.SQLException
     */
    
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