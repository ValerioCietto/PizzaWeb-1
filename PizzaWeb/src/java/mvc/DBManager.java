package mvc;

import java.sql.*;

public final class DBManager {
    static final String ur = "jdbc:derby://localhost:1527/PizzaWeb";
    static final String us= "pizzeria";
    static final String p= "pizzeria";
    Connection conn;
    Statement st;

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Inizializza il Database
     */
    
    public DBManager(){
       try {
            // registrazione driver JDBC da utilizzare
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            creaTabelle();
       } catch (SQLException e) {System.out.println(e.getMessage());}
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Genera il database creando tre tabelle:
     * UTENTI, PIZZE e PRENOTAZIONI
     */
    
    public void creaTabelle(){
        try {
        conn = DriverManager.getConnection(ur,us,p);
            try{ 
                st = conn.createStatement();
                try{
                    drop();
                }catch(SQLException e){System.out.println(e.getMessage());}
                try {
                    st.executeUpdate(   "CREATE TABLE UTENTI(" +
                            "IDUSER         INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                            "USERNAME       VARCHAR(30) NOT NULL UNIQUE," +
                            "PASSWORD       VARCHAR(30) NOT NULL," +
                            "PERMISSION     VARCHAR(30) NOT NULL," +
                            "PRIMARY KEY(IDUSER))");
                    
                } catch (SQLException e){System.out.println(e.getMessage());}
                try {
                    st.executeUpdate(   "CREATE TABLE PIZZE(" +
                            
                            "IDPIZZA        INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                            "NOME           VARCHAR(30) NOT NULL UNIQUE," +
                            "INGREDIENTI    VARCHAR(65) NOT NULL," +
                            "PREZZO         DOUBLE      NOT NULL," +
                            "PRIMARY KEY(IDPIZZA))");
                    
                } catch (SQLException e){System.out.println(e.getMessage());}
                try {
                    st.executeUpdate(   "CREATE TABLE PRENOTAZIONI(" +
                            
                            "IDPRENOTAZIONE INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                            "IDUTENTE       INT         NOT NULL    ," +
                            "IDPIZZA        INT         NOT NULL    ," +
                            "QUANTITA       INT         NOT NULL    ," +
                            "DATA           VARCHAR(30) NOT NULL    ," +
                            "STATO          VARCHAR(30) NOT NULL    ," +
                            "PRIMARY KEY(IDPRENOTAZIONE, IDUTENTE, IDPIZZA),"+
                            "FOREIGN KEY(IDUTENTE) REFERENCES UTENTI(IDUSER),"+
                            "FOREIGN KEY(IDPIZZA) REFERENCES PIZZE(IDPIZZA))");
                    
                } catch (SQLException e){System.out.println(e.getMessage());}
                
            } catch (SQLException e){System.out.println(e.getMessage());}
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }    
    }
    
////////////////////////////////////////////////////////////////////////////////
    
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
        try(ResultSet rs = st.executeQuery("SELECT IDPIZZA FROM PIZZE WHERE NOME='"+nome+"'")) {
            id = rs.getInt("IDPIZZA");
        }
        return id;
    }

////////////////////////////////////////////////////////////////////////////////
   
    /**
     * Rimuove una pizza dalla tabella PIZZE
     * 
     * @param id
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean remPizza(int id) throws SQLException{
        return esegui("DELETE FROM PIZZE WHERE (ID='"+id+"')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
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
    
////////////////////////////////////////////////////////////////////////////////
    
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
        try(ResultSet rs = st.executeQuery("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+nome+"'")){
            rs.next();
            id = rs.getInt(1);
        }
        return id;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Elimina un utente nella tabella UTENTI
     * 
     * @param id
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean remUser(int id) throws SQLException{
        return esegui("DELETE FROM UTENTI WHERE (ID='"+id+"')");
    }
    
    
////////////////////////////////////////////////////////////////////////////////
    
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
    

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Ritorna una Stringa contenente l'utente
     * 
     * @param usr       nome dell'utente
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public ResultSet getUser(String usr) throws SQLException{
        ResultSet rs = null;
        rs = st.executeQuery("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"'");
        return rs;
    }
    
    public ResultSet getUser(int id) throws SQLException{
        ResultSet rs = null;
        rs = st.executeQuery("SELECT * FROM UTENTI WHERE IDUSER="+ id);
        return rs;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Effettua un controllo sul login dell'utente
     * 
     * @param usr       nome dell'utente
     * @param pwd       password dell'utente
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean checkLogin(String usr,String pwd) throws SQLException{
        return esegui("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"' AND PASSWORD ='"+pwd+"'");
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
        int id = -1;
        try(ResultSet rs = st.executeQuery("SELECT IDPIZZA FROM PIZZE WHERE NOME='"+nome+"'")){
            id = rs.getInt("IDPIZZA");
        }
        return id;
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
        int id = -1;
        try(ResultSet rs = st.executeQuery("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+username+"'")){
            rs.next();
            id = rs.getInt("IDUSER");
        }
        return id;
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
        int id = -1;
        try(ResultSet rs = st.executeQuery("SELECT IDPRENOTAZIONE FROM PRENOTAZIONI WHERE IDUTENTE="+username+" AND IDPIZZA ="+ pizza+" AND DATA='"+data+"'")){
            id = rs.getInt("IDPRENOTAZIONE");
        }
        return id;
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
     * @throws java.sql.SQLException
     */
    
    public int addPrenotazione(int cliente, int pizza, int quantita, String data) throws SQLException{
        int id = -1;
        esegui("INSERT INTO PRENOTAZIONI(IDUTENTE,IDPIZZA,QUANTITA,DATA,STATO) VALUES ('"+cliente+"', '"+pizza+"', '"+quantita+"', '"+data+"', 'ordinato')");
        try(ResultSet rs = st.executeQuery("SELECT IDPRENOTAZIONE FROM PRENOTAZIONE WHERE IDUTENTE='"+cliente+"' AND IDPIZZA='"+pizza+"' AND DATA ='"+data+"'")){
            id = rs.getInt("IDPRENOTAZIONE");
        }
        return id;
    }

////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * Rimuove una prenotazione
     * 
     * @param idP
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean remPrenotazione(int idP) throws SQLException{
        return esegui("DELETE FROM PRENOTAZIONE WHERE (IDPRENOTAZIONE='"+idP+"')");
    }

////////////////////////////////////////////////////////////////////////////////
    
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
        return esegui("UPDATE PRENOTAZIONE SET QUANTITA='" + quantita+ "', DATA=" +data+" WHERE IDPRENOTAZIONE ='" +idPrenotazione+"'");
    }
    
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Esegue una query SQL
     * 
     * @param sql       query sql da eseguire
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public boolean esegui(String sql) throws SQLException {
        boolean tmp;
        tmp = st.execute(sql);
        return tmp;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Popola il database
     * @throws java.sql.SQLException
     */
    
    public void startDati() throws SQLException { //startDati(String tab, String nome, String mezzo, String fine)
        addUser("admin","admin","admin");
        addUser("user","user","user");
        addUser("mario","mario","user");
        addUser("b","b","user");
        addPizza("Margherita","pomodoro e mozzarella", 15);
        addPizza("Funghi","pomodoro e funghi", 6);
       // addPrenotazione("user","210491", "Margherita",3);
       // addPrenotazione("user","220591", "funghi",4);
       // addPrenotazione("mario","100291", "bianca",2);
       // addPrenotazione("mario","100291", "rossa",2);
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Elimina le tabelle del Database
     * @throws java.sql.SQLException
     */
    
    public void drop() throws SQLException{
        
        System.out.println(esegui("DROP TABLE PRENOTAZIONI"));
        System.out.println(esegui("DROP TABLE UTENTI"));
        System.out.println(esegui("DROP TABLE PIZZE"));
    
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
                                    //TEST//
    
    
    //Utente OK
    //modifica di una pizza,prenot,user inesistente ritorna true... errore!... da gestire in model
    //effettuare controlli per non ppermettere nomi duplicati nel model
    //gestire da model get login che richieda permessi
    
/*
    public static void main(String[] args){
        drop();
        inizializza();
    }
*/

}