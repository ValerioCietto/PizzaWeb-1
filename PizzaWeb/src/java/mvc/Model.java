package mvc;

import components.*;
import java.sql.*;
import java.util.ArrayList;


////////////////////////////////////////////////////////////////////////////////

public final class Model {
    
////////////////////////////////////////////////////////////////////////////////
// CREATORI DI OGGETTI
    
    /**
     * Crea un oggetto Utente e lo inserisce nel database
     * @param username
     * @param password
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static boolean creaUtente(String username, String password) throws SQLException{
        if (getIdUtente(username)<0){
            Utente user = new Utente(username, password, "user");
            addUtente(user);
            return true;
        }
        return false;
    }
    
    /**
     * Crea un oggetto Pizza e lo inserisce nel Database
     * @param nome
     * @param ingredienti
     * @param prezzo
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static boolean creaPizza(String nome, String ingredienti, double prezzo) throws SQLException{
        if (getIdPizza(nome)<0){
            Pizza pizza = new Pizza(nome, ingredienti, prezzo);
            addPizza(pizza);
            return true;
        }
        return false;
    }
    
    /**
     * Crea un oggetto Prenotazione e lo inserisce nel Database
     * 
     * @param idUtente
     * @param idPizza
     * @param quantita
     * @param data
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static boolean creaPrenotazione(int idUtente, int idPizza, int quantita, String data) throws SQLException{
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
     * Controlla l'esistenza dell'utente ed effettua il set degli attributi della sessione in uso
     * 
     * @param username
     * @param password
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static boolean login(String username, String password) throws SQLException{
        Utente login = checkLogin(username, password);
        if(login!=null)
            return true;
        else
            return false;
    }
    
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
    
    public static Utente checkLogin(String username, String password) throws SQLException{
        Utente tmp = null;
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        if(DBManager.checkLogin(username, password, st) > 0){
            ResultSet rs = DBManager.getUser(username, st);
            if(rs.next()){
                tmp = new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
            }
        }
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
        return tmp;
    }
    
    /**
     * Elimina le tabelle del Database
     * @throws SQLException 
     */
    
    public static void drop() throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        DBManager.drop(st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
    }
    
    
////////////////////////////////////////////////////////////////////////////////
// FORNITORI DI LISTE
    
    /**
     * Fornisce la lista di tutti gli utenti
     * 
     * @param id
     * @return 
     * 
     * @throws java.sql.SQLException 
     */
    
    public static ArrayList<Utente> getListaUtenti(int id) throws SQLException{
        ArrayList<Utente> listaUtenti = new ArrayList(); 
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.query("SELECT * FROM UTENTI WHERE IDUSER <> "+id+"",st);
            while(rs.next())
               listaUtenti.add(new Utente(rs.getInt("IDUSER"),rs.getString("USERNAME"),rs.getString("PASSWORD"), rs.getString("PERMISSION")));            
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
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
    
    public static ArrayList<Pizza> getCatalogo() throws SQLException{
        ArrayList<Pizza> listaPizze = new ArrayList(); 
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.query("SELECT * FROM PIZZE",st);
            while(rs.next())
               listaPizze.add(new Pizza(rs.getInt("IDPIZZA"), rs.getString("NOME"), rs.getString("INGREDIENTI"), rs.getDouble("PREZZO")));            
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
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
    
    public static ArrayList<Prenotazione> getListaPrenotazioni() throws SQLException{
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList();
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.query("SELECT * FROM PRENOTAZIONI",st);
            while(rs.next())
               listaPrenotazioni.add(new Prenotazione(rs.getInt("IDPRENOTAZIONE"),rs.getInt("IDUTENTE"),rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA")));            
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
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
    
    public static ArrayList<Prenotazione> getListaPrenotazioni(int idUtente) throws SQLException{
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList(); 
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.query("SELECT * FROM PRENOTAZIONI WHERE IDUTENTE="+idUtente,st);
            while(rs.next())
               listaPrenotazioni.add(new Prenotazione(rs.getInt("IDPRENOTAZIONE"),rs.getInt("IDUTENTE"),rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA")));            
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
        }
        return listaPrenotazioni;
    }
    
    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DI INSERIMENTO NEL DATABASE(OK)
    
    /**
     * Inserisce un utente nella tabella UTENTI e lo aggiunge in lista
     * 
     * @param u
     * 
     * @throws java.sql.SQLException
     */
    
    protected static void addUtente(Utente u) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            if(DBManager.getIdUser(u.getUsername(), st) < 0){
                //aggiungi utente
                int tmp = DBManager.addUser(u.getUsername(), u.getPassword(), u.getPermission(),st);
                if(tmp >= 0){
                    u.setId(tmp);
                }
            }
            else
                System.out.println("Username non valido");
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
        }
    }
    
    /**
     * Aggiunge una pizza nella tabella PIZZE del database e lo aggiunge in lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    protected static void addPizza(Pizza p) throws SQLException {
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            if(DBManager.getIdPizza(p.getNome(),st) < 0){
                //aggiungi pizza
                int tmp = DBManager.addPizza(p.getNome(), p.getIngredinti(), p.getPrezzo(),st);
                if(tmp >= 0){
                    p.setId(tmp);
                }
            }
            else
                System.out.println("Pizza già presente");
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
        }
    }      
    
    /**
     * Aggiunge una prenotazione ad un cliente e la aggiunge in lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    protected static void addPrenotazione(Prenotazione p) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            if(DBManager.getIdPrenotazione(p.getIdUtente(), p.getIdPizza(), p.getData(), st) < 0){
                //aggiungi prenotazione
                int tmp = DBManager.addPrenotazione(p.getIdUtente(), p.getIdPizza(), p.getQuantita(), p.getData(),st);
                if(tmp >= 0){
                    p.setIdPrenotazione(tmp);
                }
            }
            else
                System.out.println("Prenotazione non valida");
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
        }
    }
    
    
////////////////////////////////////////////////////////////////////////////////
// METODI DI ELIMINAZIONE DAL DATABASE(OK)
    
    /**
     * Rimuove un utente dalla tabella UTENTI e dalla lista
     * 
     * @param u
     * 
     * @throws java.sql.SQLException
     */
    
    public static void remUtente(Utente u) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        //rimuovi utente
        DBManager.remUser(u.getId(),st);
        u.setId(-1);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
    }
   
    /**
     * Rimuove una pizza dalla tabella PIZZE e dalla lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public static void remPizza(Pizza p) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        //rimuovi pizza
        DBManager.remPizza(p.getId(),st);
        p.setId(-1);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
    }   
    
    /**
     * Rimuove una prenotazione e dalla lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public static void remPrenotazione(Prenotazione p) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        DBManager.remPrenotazione(p.getIdPrenotazione(),st);
        p.setIdPrenotazione(-1);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
    }
    
    
////////////////////////////////////////////////////////////////////////////////
// METODI DI MODIFICA NEL DATABASE(OK)
    
    /**
     * Modifica un utente nella tabella UTENTI e ricrea la lista
     * 
     * @param user
     * @param u
     * 
     * @throws java.sql.SQLException
     */
    
    public static void modUtente(String user, Utente u) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        DBManager.modUser(user, u.getUsername(), u.getPassword(), u.getPermission(),st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
    }
    
    /**
     * Modifica una pizza nella tabella PIZZE e ricrea la lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public static void modPizza(Pizza p) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        DBManager.modPizza(p.getNome(), p.getIngredinti(), p.getPrezzo(), st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
    }
    
    /**
     * Modifica una prenotazione e ricrea la lista
     * 
     * @param p
     * 
     * @throws java.sql.SQLException
     */
    
    public static void modPrenotazione(Prenotazione p) throws SQLException{
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        DBManager.modPrenotazione(p.getIdPrenotazione(),p.getIdUtente(),p.getIdPizza(), p.getQuantita(), p.getData(), st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);

    }

    
////////////////////////////////////////////////////////////////////////////////
// METODI DI GET ID DAL DATABASE(OK)
    
    /**
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     * 
     * @param username  nome utente
     * 
     * @return          ritorna un valore intero che indica l'ID
     * 
     * @throws java.sql.SQLException
     */
    
    public static int getIdUtente(String username) throws SQLException{
        int tmp = -1;
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        tmp = DBManager.getIdUser(username, st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
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
    
    public static int getIdPizza(String nome) throws SQLException{
        int tmp = -1;
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        tmp = DBManager.getIdPizza(nome, st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
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
    
    public static int getIdPrenotazione(int username, int pizza, String  data) throws SQLException{
        int tmp = -1;
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        tmp = DBManager.getIdPrenotazione(username, pizza, data, st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
        return tmp;
    }

    
////////////////////////////////////////////////////////////////////////////////
// METODI DI GET DAL DATABASE(OK)
    
    /**
     * Ritorna un oggetto Utente partendo dall'username
     * 
     * @param name
     * 
     * @return          ritorna un Oggetto contenente il risultato della query
     * 
     * @throws java.sql.SQLException
     */
    
    public static Utente getUtente(String name) throws SQLException{
        Utente tmp = null;
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.getUser(name, st);
            if(rs.next())
                tmp= new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
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
    
    public static Utente getUtente(int id) throws SQLException{
        Utente tmp = null;  
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.getUser(id, st);
            if(rs.next())
                tmp = new Utente(rs.getInt("IDUSER"), rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("PERMISSION"));
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
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
    
    public static Pizza getPizza(String name) throws SQLException {
        Pizza result = null;
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.getPizza(name,st);
            if(rs.next())
                result = new Pizza( rs.getInt("IDPIZZA"), rs.getString("NOME"), rs.getString("INGREDIENTI"), rs.getDouble("PREZZO"));
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
        }
        return result;
    }
    
     /**
     * Ritorna un oggetto Pizza partendo dal nome
     * 
     * @param id
     * 
     * @return          ritorna un Oggetto contenente il risultato della query
     * 
     * @throws java.sql.SQLException
     */
    
    public static Pizza getPizza(int id) throws SQLException {
        Pizza result = null;
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.getPizza(id,st);
            if(rs.next())
                result = new Pizza( rs.getInt("IDPIZZA"), rs.getString("NOME"), rs.getString("INGREDIENTI"), rs.getDouble("PREZZO"));
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
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
    
    public static Prenotazione getPrenotazione(int id) throws SQLException{
        Prenotazione tmp = null;  
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        try{
            ResultSet rs = DBManager.getPrenotazione(id, st);
            if(rs.next())
                tmp= new Prenotazione(rs.getInt("IDPRENOTAZIONE"), rs.getInt("IDUTENTE"), rs.getInt("IDPIZZA"), rs.getInt("QUANTITA"), rs.getString("DATA"));
        }finally{
            DBManager.closeStatement(st);
            DBManager.closeConnection(conn);
        }
        return tmp;
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}
