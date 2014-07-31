package components;

import java.sql.*;

public final class DBManager {

    

    
    
////////////////////////////////////////////////////////////////////////////////
// CREAZIONE DATABASE
    
    /**
     * Genera il database creando tre tabelle:
     * UTENTI, PIZZE e PRENOTAZIONI
     * @param st
     * @throws java.sql.SQLException
     */
    
    public static void creaTabelle(Statement st) throws SQLException{
        st.execute(   "CREATE TABLE UTENTI(" +
                "IDUSER         INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "USERNAME       VARCHAR(33) NOT NULL UNIQUE," +
                "PASSWORD       VARCHAR(33) NOT NULL," +
                "PERMISSION     VARCHAR(10) NOT NULL," +
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
                "DATA           VARCHAR(10) NOT NULL    ," +
                "STATO          VARCHAR(10) NOT NULL    ," +
                "PRIMARY KEY(IDPRENOTAZIONE, IDUTENTE, IDPIZZA),"+
                "FOREIGN KEY(IDUTENTE) REFERENCES UTENTI(IDUSER) ON DELETE CASCADE,"+
                "FOREIGN KEY(IDPIZZA) REFERENCES PIZZE(IDPIZZA) ON DELETE CASCADE)");
    }
    
    /**
     * Inizializza il database con 4 utenti e 4 pizze
     * @param st
     * @throws java.sql.SQLException
     */
    
    public static void inizializza(Statement st) throws SQLException{
        addUser("admin", "admin", "admin", st);
        addUser("alessandro", "password", "user", st);
        addUser("mirko", "password", "user", st);
        addUser("anna", "password", "user", st);
        addPizza("Margherita", "pomodoro, mozzarella, basilico", 5.00, st);
        addPizza("4 Formaggi", "pomodoro, mozzarella, fontina, gorgonzola, emmenthal", 8.00, st);
        addPizza("Wurstel", "pomodoro, mozzarella, wurstel", 6.00, st);
        addPizza("Prosciutto e funghi", "pomodoro, mozzarella, prosciutto, funghi", 7.00, st);
    }

    
////////////////////////////////////////////////////////////////////////////////      
// UTILITY DATABASE
    
    /**
     * Controlla se esiste un utente con quell'username e password
     * 
     * @param usr
     * @param pwd
     * @param st
     * 
     * @return 
     */
    
    public static int checkLogin(String usr,String pwd, Statement st){
        try{
            ResultSet rs = st.executeQuery("SELECT IDUSER FROM UTENTI WHERE USERNAME='"+usr+"' AND PASSWORD ='"+pwd+"'");
            rs.next();
            int id = rs.getInt("IDUSER");
            return id;
        }catch(SQLException e){return -1;}
    }
    
    /**
     * Controlla se esiste il database
     * 
     * @param st
     * @return 
     */
    
    public static boolean checkDatabase(Statement st){
        try{
            return esegui("select * from UTENTI", st) && esegui("select * from PIZZE", st) && esegui("select * from PRENOTAZIONI", st);
        }catch(SQLException e){return false;}
    } 
    
    /**
     * Elimina tutte le tabelle del database
     * 
     * @param st
     * @throws SQLException 
     */
    
    public static void drop(Statement st) throws SQLException{
        if(checkDatabase(st)){
            esegui("DROP TABLE PRENOTAZIONI", st);
            esegui("DROP TABLE PIZZE", st);
            esegui("DROP TABLE UTENTI", st);
        }
    }
        
    /**
     * Esegue una query SQL ritornando un booleano
     * 
     * @param sql
     * @param st
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static boolean esegui(String sql, Statement st) throws SQLException {
        boolean tmp;
        tmp = st.execute(sql);
        return tmp;
    }
    
    /**
     * Esegue una query SQL ritornando un ResultSet
     * 
     * @param sql
     * @param st
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static ResultSet query(String sql, Statement st) throws SQLException{
        try{        
        return st.executeQuery(sql);
        }catch(SQLException e){return null;}
    }

    /**
     * Apre una Connection con il Database e crea uno Statement
     * 
     * @param conn
     * @return 
     * @throws SQLException 
     */
    
    public static Statement openStatement(Connection conn) throws SQLException{
        Statement st = conn.createStatement();
        return st;
        
    }
    
    /**
     * Apre una connessione
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static Connection openConnection() throws SQLException{
        String ur = "jdbc:derby://localhost:1527/PizzaWeb";
        String us = "pizzeria";
        String p = "pizzeria";
        Connection conn = DriverManager.getConnection(ur, us, p);
        return conn;     
    }
    
    /**
     * Chiude uno Statement precedentemente creato
     * @param st
     * @throws SQLException 
     */
    
    public static void closeStatement(Statement st) throws SQLException{
        st.close();
    }
    
    /**
     * Chiude la Connection precedentemente creata
     * 
     * @param conn
     * @throws SQLException 
     */
    
    public static void closeConnection(Connection conn) throws SQLException{
        conn.close();
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////      
// METODI DI INSERIMENTO (OK)
    
    /**
     * Inserisce un utente nella tabella UTENTI
     * 
     * @param nome      nome dell'utente
     * @param password  password dell'utente
     * @param ruolo     permessi dell'utente
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static int addUser(String nome, String password, String ruolo, Statement st) throws SQLException{
        int id = -1;
        esegui("INSERT INTO UTENTI (USERNAME, PASSWORD, PERMISSION) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')", st);
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
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static int addPizza(String nome, String ingr, double prezzo, Statement st) throws SQLException{
        int id = -1;
        esegui("INSERT INTO PIZZE (NOME, INGREDIENTI, PREZZO) VALUES ('"+nome+"', '"+ingr+"', "+prezzo+")", st);
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
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static int addPrenotazione(int cliente, int pizza, int quantita, String data, Statement st) throws SQLException{
        int id = -1;
        esegui("INSERT INTO PRENOTAZIONI(IDUTENTE,IDPIZZA,QUANTITA,DATA,STATO) VALUES ("+cliente+", "+pizza+", "+quantita+", '"+data+"', 'Ordinato')", st);
        ResultSet rs = st.executeQuery("SELECT IDPRENOTAZIONE FROM PRENOTAZIONI WHERE IDUTENTE="+cliente+" AND IDPIZZA="+pizza+" AND DATA ='"+data+"'");
        rs.next();
        id = rs.getInt("IDPRENOTAZIONE");
        return id;
    }

    
////////////////////////////////////////////////////////////////////////////////      
// METODI DI RIMOZIONE (OK)
    
    /**
     * Elimina un utente nella tabella UTENTI
     * 
     * @param id
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static boolean remUser(int id, Statement st) throws SQLException{
        return esegui("DELETE FROM UTENTI WHERE (IDUSER="+id+")", st);
    }
       
    /**
     * Rimuove una pizza dalla tabella PIZZE
     * 
     * @param id
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static boolean remPizza(int id, Statement st) throws SQLException{
        return esegui("DELETE FROM PIZZE WHERE (IDPIZZA="+id+")",st);
    }
       
    /**
     * Rimuove una prenotazione
     * 
     * @param idP
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static boolean remPrenotazione(int idP, Statement st) throws SQLException{
        return esegui("DELETE FROM PRENOTAZIONI WHERE (IDPRENOTAZIONE="+idP+")", st);
    }
    
    
////////////////////////////////////////////////////////////////////////////////      
// METODI DI MODIFICA (OK)
    
    /**
     * Modifica un utente nella tabella UTENTI
     * 
     * @param nome      nome attuale dell'utente
     * @param nNome
     * @param nPassword nuova password dell'utente
     * @param nRuolo    nuovi permessi dell'utente;
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static boolean modUser(String nome, String nNome, String nPassword, String nRuolo, Statement st) throws SQLException{
        return esegui("UPDATE UTENTI SET USERNAME ='"+nNome+"', PASSWORD='" +nPassword+"', PERMISSION ='" +nRuolo+"' WHERE USERNAME = '"+ nome +"'", st);
    }
    
    /**
     * Modifica una pizza nella tabella PIZZE
     * 
     * @param nome      nome della pizza
     * @param nIngr     nuovi ingredienti della pizza
     * @param nPrezzo   nuovo prezzo della pizza
     * @param st
     * 
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static boolean modPizza(String nome, String nIngr, double nPrezzo, Statement st) throws SQLException{
        return esegui("UPDATE PIZZE SET INGREDIENTI='" + nIngr+ "', PREZZO=" +nPrezzo+" WHERE NOME ='" +nome+"'", st);
    }
        
    /**
     * Modifica una prenotqazione nella tabella PRENOTAZIONI
     * 
     * 
     * @param idPrenotazione
     * @param idUtente
     * @param idPizza
     * @param quantita
     * @param data
     * @param st
     * @return          ritorna un booleano
     * @throws java.sql.SQLException
     */
    
    public static boolean modPrenotazione(int idPrenotazione,int idUtente,int idPizza,int quantita, String data, Statement st) throws SQLException{
        return esegui("UPDATE PRENOTAZIONI SET IDUTENTE="+idUtente+", IDPIZZA="+idPizza+", QUANTITA=" + quantita+ ", DATA='" +data+"' WHERE IDPRENOTAZIONE =" +idPrenotazione, st);
    }
    
    public static boolean modStatoPrenotazione(int idPrenotazione, String stato, Statement st) throws SQLException{
        return esegui("UPDATE PRENOTAZIONI SET STATO='"+stato+"' WHERE IDPRENOTAZIONE =" +idPrenotazione, st);
    }
    
    
////////////////////////////////////////////////////////////////////////////////      
// METODI DI GET ID (OK)   
    
    /**
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     * 
     * @param username  nome utente
     * @param st
     * 
     * @return          ritorna un valore intero che indica l'ID
     */
    
    public static int getIdUser(String username, Statement st){
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
     * @param st
     * 
     * @return          ritorna un valore intero che indica l'ID
     */
    
    public static int getIdPizza(String nome, Statement st){
        try{
        ResultSet rs = st.executeQuery("SELECT IDPIZZA FROM PIZZE WHERE NOME='"+nome+"'");
        rs.next();
        int id = rs.getInt("IDPIZZA");
        return id;
        }catch(SQLException e){return -1;}
    }

    /**
     * Prende in input l'ID utente, l'ID pizza e la data e restituisce l'ID della prenotazione associata
     * 
     * @param username  ID dell'utenteutente
     * @param pizza     ID della pizza
     * @param data      Data della prenotazione
     * @param st
     * 
     * @return          ritorna un valore intero che indica l'ID
     * @throws java.sql.SQLException
     */
    
    public static int getIdPrenotazione(int username, int pizza, String  data, Statement st) throws SQLException{
        try(ResultSet rs = st.executeQuery("SELECT IDPRENOTAZIONE FROM PRENOTAZIONI WHERE IDUTENTE="+username+" AND IDPIZZA ="+ pizza+" AND DATA='"+data+"'")){
            rs.next();
            int id = rs.getInt(1);
            return id;
        }catch(SQLException e){return -1;}
    }

    
////////////////////////////////////////////////////////////////////////////////      
// METODI DI GET (OK)
    
    /**
     * Ritorna ResultSet contenente l'utente
     * 
     * @param usr       nome dell'utente
     * @param st
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public static ResultSet getUser(String usr, Statement st) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"'");
        return rs;
    }
    
    public static ResultSet getUser(int id, Statement st) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM UTENTI WHERE IDUSER="+ id);
        return rs;
    }
 
    /**
     * Ritorna un ResultSet contenente la pizza
     * 
     * @param usr       nome della pizza
     * @param st
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public static ResultSet getPizza(String usr, Statement st) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE NOME='"+usr+"'");
        return rs;
    }
    
    public static ResultSet getPizza(int id, Statement st) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE IDPIZZA="+ id);
        return rs;
    }

    /**
     * Ritorna un ResultSet contenente la prenotazione
     * 
     * @param id       
     * @param st       
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     * @throws java.sql.SQLException
     */
    
    public static ResultSet getPrenotazione(int id, Statement st) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM PRENOTAZIONI WHERE IDPRENOTAZIONE="+ id);
        return rs;
    }


////////////////////////////////////////////////////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////        
}