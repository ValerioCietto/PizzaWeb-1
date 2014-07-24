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
        Connection conn = DriverManager.getConnection(ur,us,p);
        drop();
            try (Statement st = conn.createStatement()) {   
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
                
            }         } catch (SQLException e){
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
    
    public int addLogin(String nome, String password, String ruolo) throws SQLException{
        int id = -1;
        esegui("INSERT INTO UTENTI (USERNAME, PASSWORD, PERMISSION) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')");
        try(ResultSet rs = st.executeQuery("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+nome+"'")){
            id = rs.getInt("IDUSER");
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
    
    public boolean remLogin(int id) throws SQLException{
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
    
    public boolean modLogin(String nome, String nPassword, String nRuolo) throws SQLException{
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
    
    public ResultSet getLogin(String usr) throws SQLException{
        ResultSet rs = null;
        rs = st.executeQuery("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"'");
        return rs;
    }
    
    public ResultSet getLogin(int id) throws SQLException{
        ResultSet rs = null;
        rs = st.executeQuery("SELECT * FROM UTENTI WHERE IDUSER='"+ id +"'");
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
     */
    
    public static boolean controllaLogin(String usr,String pwd){
        return !((query("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"' AND PASSWORD ='"+pwd+"'").get(0)).equals(""));
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
        try{
            return Integer.parseInt(query("SELECT IDPIZZA FROM PIZZE WHERE NOME='"+nome+"'").get(0));
        }catch(NumberFormatException e){
            return -1;
        }
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
        try{
            return Integer.parseInt(query("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+username+"'").get(0));
        }catch(NumberFormatException e){
            return -1;
        }
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
        try{
            return Integer.parseInt(query("SELECT IDPRENOTAZIONE FROM PRENOTAZIONI WHERE IDUTENTE="+username+" AND IDPIZZA ="+ pizza+" AND DATA='"+data+"'").get(0));
        }catch(NumberFormatException e){
            return -1;
        }
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
    
    public boolean addPrenotazione(int cliente, int pizza, int quantita, String data) throws SQLException{
        
        String sql="INSERT INTO PRENOTAZIONI(IDUTENTE,IDPIZZA,QUANTITA,DATA,STATO) VALUES ";
        sql+="("+cliente+", "+pizza+","+quantita+", '"+data+"', 'Ordinato')";
        return esegui(sql);
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
     * @throws java.sql.SQLException
     */
    
    public boolean remPrenotazione(int cliente, int pizza, String data) throws SQLException{
        return esegui("DELETE FROM PRENOTAZIONI WHERE (IDUTENTE= "+cliente+ "AND IDPIZZA= "+pizza+ " AND DATA= '"+data+ "')");
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
        addLogin("admin","admin","admin");
        addLogin("user","user","user");
        addLogin("mario","mario","user");
        addLogin("b","b","user");
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