package mvc;

import components.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.*;


////////////////////////////////////////////////////////////////////////////////

public class Model {
    
    private final DBManager dbman;
    
    
////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    public Model() throws SQLException{
        dbman = new DBManager();
    }

    
////////////////////////////////////////////////////////////////////////////////
// CREATORI DI OGGETTI
    
    public Utente login(String username, String password, HttpSession s) throws SQLException{
        Utente login = checkLogin(username, password);
            if (login!=null) {
                s.setAttribute("username", login.getUsername());
                s.setAttribute("permission", login.getPermission());
                s.setAttribute("message", "login effettuato, benvenuto " + login.getUsername() + "!");
            } else {
                s.setAttribute("message", "login errato, sicuro di esserti registrato?");
            }
        return login;
    }

    public boolean register(String username, String password) throws SQLException{
        if (getUtente(username)== null){
            Utente user = new Utente(username, password, "user");
            addUtente(user);
            return true;
        }
        return false;
    }
    
    public boolean creaPizza(String nome, String ingredienti, double prezzo) throws SQLException{
        if (getPizza(nome)== null){
            Pizza pizza = new Pizza(nome, ingredienti, prezzo);
            addPizza(pizza);
            return true;
        }
        return false;
    }
    
    public boolean creaPrenotazione(int idUtente, int idPizza, int quantita, String data) throws SQLException{
        if (getIdPrenotazione(idUtente, idPizza, data) < 0){
            Prenotazione prenotazione = new Prenotazione(idUtente, idPizza, quantita, data);
            addPrenotazione(prenotazione);
            return true;
        }
        return false;
    }

////////////////////////////////////////////////////////////////////////////////
// UTILITY MODEL    
    
    /**
     * Effettua un controllo sul login dell'utente
     * 
     * @param username
     * @param password
     * 
     * @return          ritorna un booleano
     * 
     * @throws java.sql.SQLException
     */
    
    public Utente checkLogin(String username, String password) throws SQLException{
        Utente tmp = null;  
        try{
            if(dbman.checkLogin(username, password) > 0){
            dbman.openConnection();
            ResultSet rs = dbman.getUser(username);
            if(rs.next()){
                tmp = new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
            }
            }
        }finally{
            dbman.closeConnection();
        }
        return tmp;
    }
    
    /**
     * Elimina le tabelle del Database
     * @throws SQLException 
     */
    
    public void drop() throws SQLException{
        dbman.openConnection();
        try{
            dbman.drop();
        }finally{
            dbman.closeConnection();
        }
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
        ArrayList<Pizza> listaPizze = new ArrayList(); 
        try{
            dbman.openConnection();
            ResultSet rs = dbman.query("SELECT * FROM PIZZE");
            while(rs.next())
               listaPizze.add(new Pizza(rs.getInt("IDPIZZA"), rs.getString("NOME"), rs.getString("INGREDIENTI"), rs.getDouble("PREZZO")));            
        }finally{
            dbman.closeConnection();
        }
        return listaPizze;
    }
    
    /**
     *  Fornisce la lista di tutte le prenotazioni
     * 
     * @return 
     * 
     * @throws java.sql.SQLException 
     */
    
    public ArrayList<Prenotazione> getListaPrenotazioni() throws SQLException{
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList(); 
        try{
            dbman.openConnection();
            ResultSet rs = dbman.query("SELECT * FROM PRENOTAZIONI");
            while(rs.next())
               listaPrenotazioni.add(new Prenotazione(rs.getInt("IDPRENOTAZIONE"),rs.getInt("IDUTENTE"),rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA")));            
        }finally{
            dbman.closeConnection();
        }
        return listaPrenotazioni;
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
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList(); 
        try{
            dbman.openConnection();
            ResultSet rs = dbman.query("SELECT * FROM PRENOTAZIONI WHERE IDUTENTE="+idUtente);
            while(rs.next())
               listaPrenotazioni.add(new Prenotazione(rs.getInt("IDPRENOTAZIONE"),rs.getInt("IDUTENTE"),rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA")));            
        }finally{
            dbman.closeConnection();
        }
        return listaPrenotazioni;
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
    
    protected void addUtente(Utente u) throws SQLException{
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
     * Aggiunge una pizza nella tabella PIZZE del database e lo aggiunge in lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    protected void addPizza(Pizza p) throws SQLException {
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
     * Aggiunge una prenotazione ad un cliente e la aggiunge in lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    protected void addPrenotazione(Prenotazione p) throws SQLException{
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
// METODI DI ELIMINAZIONE (OK)
    
    /**
     * Rimuove un utente dalla tabella UTENTI e dalla lista
     * 
     * @param u
     * 
     * @throws java.sql.SQLException
     */
    
    public void remUtente(Utente u) throws SQLException{
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
    
    public void modUtente(Utente u) throws SQLException{
        try{
            dbman.openConnection();
            dbman.modUser(u.getUsername(), u.getPassword(), u.getPermission());
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
    
    public int getIdUtente(String username) throws SQLException{
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
    
    public Utente getUtente(String name) throws SQLException{
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
    
    public Utente getUtente(int id) throws SQLException{
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
