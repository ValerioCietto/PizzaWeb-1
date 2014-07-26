package mvc;

import java.sql.*;

public final class DBManager {
    static final String ur = "jdbc:derby://localhost:1527/PizzaWeb";
    static final String us = "pizzeria";
    static final String p = "pizzeria";
    Connection conn;
    Statement st;

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Inizializza il Database
     * @throws java.sql.SQLException
     */
    
    public DBManager() throws SQLException{
        // registrazione driver JDBC da utilizzare
        DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
        openConnection();
        if(!checkDatabase()){
            creaTabelle();
            inizializza();
        }
        closeConnection();
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Genera il database creando tre tabelle:
     * UTENTI, PIZZE e PRENOTAZIONI
     * @throws java.sql.SQLException
     */
    
    public void creaTabelle() throws SQLException{
        st.execute(   "CREATE TABLE UTENTI(" +
                "IDUSER         INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "USERNAME       VARCHAR(33) NOT NULL UNIQUE," +
                "PASSWORD       VARCHAR(33) NOT NULL," +
                "PERMISSION     VARCHAR(5) NOT NULL," +
                "PRIMARY KEY(IDUSER))");

        st.execute(   "CREATE TABLE PIZZE(" +

                "IDPIZZA        INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "NOME           VARCHAR(33) NOT NULL UNIQUE," +
                "INGREDIENTI    VARCHAR(99) NOT NULL," +
                "PREZZO         DOUBLE      NOT NULL," +
                "PRIMARY KEY(IDPIZZA))");

        st.execute(   "CREATE TABLE PRENOTAZIONI(" +
                "IDPRENOTAZIONE INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "IDUTENTE       INT         NOT NULL    ," +
                "IDPIZZA        INT         NOT NULL    ," +
                "QUANTITA       INT         NOT NULL    ," +
                "DATA           VARCHAR(8) NOT NULL    ," +
                "STATO          VARCHAR(10) NOT NULL    ," +
                "PRIMARY KEY(IDPRENOTAZIONE, IDUTENTE, IDPIZZA),"+
                "FOREIGN KEY(IDUTENTE) REFERENCES UTENTI(IDUSER) ON DELETE CASCADE,"+
                "FOREIGN KEY(IDPIZZA) REFERENCES PIZZE(IDPIZZA) ON DELETE CASCADE)");
    }
    
    /**
     * Inizializza il database con 4 utenti e 4 pizze
     * @throws java.sql.SQLException
     */
    
    public void inizializza() throws SQLException{
        addUser("admin", "admin", "admin");
        addUser("alessandro", "password", "user");
        addUser("mirko", "password", "user");
        addUser("anna", "password", "user");
        addPizza("Margherita", "pomodoro, mozzarella, basilico", 5.00);
        addPizza("4 Formaggi", "pomodoro, mozzarella, fontina, gorgonzola, emmenthal", 8.00);
        addPizza("Wurstel", "pomodoro, mozzarella, wurstel", 6.00);
        addPizza("Prosciutto e funghi", "pomodoro, mozzarella, prosciutto, funghi", 7.00);
    }
    
////////////////////////////////////////////////////////////////////////////////      
//UTILITY DATABASE
    
    public int checkLogin(String usr,String pwd){
        try{
        ResultSet rs = st.executeQuery("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+usr+"' AND PASSWORD ='"+pwd+"'");
        rs.next();
        int id = rs.getInt("IDUSER");
        return id;
        }catch(SQLException e){return -1;}
    }
    
    public boolean checkDatabase(){
        try{
            return esegui("select * from UTENTI") && esegui("select * from PIZZE") && esegui("select * from PRENOTAZIONI");
        }catch(SQLException e){return false;}
    } 
    
    public boolean esegui(String sql) throws SQLException {
        boolean tmp;
        tmp = st.execute(sql);
        return tmp;
    }
    
    public ResultSet query(String sql) throws SQLException{
        try{        
        return st.executeQuery(sql);
        }catch(SQLException e){return null;}
    }

    public void openConnection() throws SQLException{
        conn = DriverManager.getConnection(ur, us, p);
        st = conn.createStatement();
        
    }
    
    public void closeConnection() throws SQLException{
        st.close();
        conn.close();
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////      
//METODI DI INSERIMENTO (OK)
    
    /**
     * Inserisce un utente nella tabella UTENTI
     * 
     * @param nome      nome dell'utente
     * @param password  password dell'utente
     * @param ruolo     permessi dell'utente
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public int addUser(String nome, String password, String ruolo) throws SQLException{
        int id = -1;
        esegui("INSERT INTO UTENTI (USERNAME, PASSWORD, PERMISSION) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')");
        ResultSet rs = st.executeQuery("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+nome+"'");
        rs.next();
        id = rs.getInt("IDUSER");
        return id;
    }
    
    /**
     * Aggiunge una pizza nella tabella PIZZE del database
     * 
     * @param nome      nome della pizza
     * @param ingr      ingredienti della pizza
     * @param prezzo    prezzo della pizza
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public int addPizza(String nome, String ingr, double prezzo) throws SQLException{
        int id = -1;
        esegui("INSERT INTO PIZZE (NOME, INGREDIENTI, PREZZO) VALUES ('"+nome+"', '"+ingr+"', "+prezzo+")");
        ResultSet rs = st.executeQuery("SELECT IDPIZZA FROM PIZZE WHERE NOME='"+nome+"'");
        rs.next();
        id = rs.getInt("IDPIZZA");
        return id;
    }
    
    /**
     * Aggiunge una prenotazione ad un cliente
     * 
     * @param cliente   cliente che effettua la prenotazione
     * @param data      data della prenotazione
     * @param pizza     tipo di pizza prenotata
     * @param quantita  quantità di pizze prenotate
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public int addPrenotazione(int cliente, int pizza, int quantita, String data) throws SQLException{
        int id = -1;
        esegui("INSERT INTO PRENOTAZIONI(IDUTENTE,IDPIZZA,QUANTITA,DATA,STATO) VALUES ("+cliente+", "+pizza+", "+quantita+", '"+data+"', 'ordinato')");
        ResultSet rs = st.executeQuery("SELECT IDPRENOTAZIONE FROM PRENOTAZIONI WHERE IDUTENTE="+cliente+" AND IDPIZZA="+pizza+" AND DATA ='"+data+"'");
        rs.next();
        id = rs.getInt("IDPRENOTAZIONE");
        return id;
    }

    
////////////////////////////////////////////////////////////////////////////////      
//METODI DI RIMOZIONE (OK)
    
    /**
     * Elimina un utente nella tabella UTENTI
     * 
     * @param id
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean remUser(int id) throws SQLException{
        return esegui("DELETE FROM UTENTI WHERE (IDUSER="+id+")");
    }
       
    /**
     * Rimuove una pizza dalla tabella PIZZE
     * 
     * @param id
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean remPizza(int id) throws SQLException{
        return esegui("DELETE FROM PIZZE WHERE (IDPIZZA="+id+")");
    }
       
    /**
     * Rimuove una prenotazione
     * 
     * @param idP
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean remPrenotazione(int idP) throws SQLException{
        return esegui("DELETE FROM PRENOTAZIONI WHERE (IDPRENOTAZIONE="+idP+")");
    }
    
    
////////////////////////////////////////////////////////////////////////////////      
//METODI DI MODIFICA (OK)
    
    /**
     * Modifica un utente nella tabella UTENTI
     * 
     * @param nome      nome attuale dell'utente
     * @param nPassword nuova password dell'utente
     * @param nRuolo    nuovi permessi dell'utente;
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean modUser(String nome, String nPassword, String nRuolo) throws SQLException{
        return esegui("UPDATE UTENTI SET PASSWORD='" +nPassword+"', PERMISSION ='" +nRuolo+"' WHERE USERNAME = '"+ nome +"'");
    }
    
    /**
     * Modifica una pizza nella tabella PIZZE
     * 
     * @param nome      nome della pizza
     * @param nIngr     nuovi ingredienti della pizza
     * @param nPrezzo   nuovo prezzo della pizza
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean modPizza(String nome, String nIngr, double nPrezzo) throws SQLException{
        return esegui("UPDATE PIZZE SET INGREDIENTI='" + nIngr+ "', PREZZO=" +nPrezzo+" WHERE NOME ='" +nome+"'");
    }
        
    /**
     * Modifica una pizza nella tabella PIZZE
     * 
     * 
     * @param idPrenotazione
     * @param quantita
     * @param data
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean modPrenotazione(int idPrenotazione,int quantita, String data) throws SQLException{
        return esegui("UPDATE PRENOTAZIONI SET QUANTITA=" + quantita+ ", DATA='" +data+"' WHERE IDPRENOTAZIONE =" +idPrenotazione);
    }
    
    
////////////////////////////////////////////////////////////////////////////////      
//METODI DI GET ID (OK)   
    
    /**
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     * 
     * @param username  nome utente
     * 
     * @return          ritorna un valore intero che indica l'ID
     */
    
    public int getIdUser(String username){
        try{
            ResultSet rs = st.executeQuery("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+username+"'");
            rs.next();
            int id = rs.getInt("IDUSER");
            return id;
        }catch(SQLException e){return -1;}
        
    }

    /**
     * Prende in input il nome della pizza e restituisce l'ID della pizza
     * 
     * @param nome      nome della pizza
     * 
     * @return          ritorna un valore intero che indica l'ID
     */
    
    public int getIdPizza(String nome){
        try{
        ResultSet rs = st.executeQuery("SELECT IDPIZZA FROM PIZZE WHERE NOME='"+nome+"'");
        rs.next();
        int id = rs.getInt("IDPIZZA");
        return id;
        }catch(SQLException e){return -1;}
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
        try(ResultSet rs = st.executeQuery("SELECT IDPRENOTAZIONE FROM PRENOTAZIONI WHERE IDUTENTE="+username+" AND IDPIZZA ="+ pizza+" AND DATA='"+data+"'")){
            rs.next();
            int id = rs.getInt(1);
            return id;
        }catch(SQLException e){return -1;}
    }

    
////////////////////////////////////////////////////////////////////////////////      
//METODI DI GET (OK)
    
    /**
     * Ritorna una Stringa contenente l'utente
     * 
     * @param usr       nome dell'utente
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public ResultSet getUser(String usr) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"'");
        return rs;
    }
    
    public ResultSet getUser(int id) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM UTENTI WHERE IDUSER="+ id);
        return rs;
    }
 
    /**
     * Ritorna una Stringa contenente la pizza
     * 
     * @param usr       nome della pizza
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public ResultSet getPizza(String usr) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE NOME='"+usr+"'");
        return rs;
    }
    
    public ResultSet getPizza(int id) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE IDPIZZA="+ id);
        return rs;
    }

    /**
     * Ritorna una Stringa contenente la prenotazione
     * 
     * @param id       
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public ResultSet getPrenotazione(int id) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM PRENOTAZIONI WHERE IDPRENOTAZIONE="+ id);
        return rs;
    }


////////////////////////////////////////////////////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////        
}