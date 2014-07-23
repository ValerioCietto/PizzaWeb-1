package mypackage;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    
    static String ur = "jdbc:derby://localhost:1527/PizzaWeb";
    static String us= "pizzeria";
    static String p= "pizzeria";

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Inizializza il Database
     */
    
    public static void inizializza(){
       try {
            // registrazione driver JDBC da utilizzare
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            creaTabelle();
            startDati();
       } catch (SQLException e) {System.out.println(e.getMessage());}
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Genera il database creando tre tabelle:
     * UTENTI, PIZZE e PRENOTAZIONI
     */
    
    public static void creaTabelle(){
        try {
        Connection conn = DriverManager.getConnection(ur,us,p);
            try (Statement st = conn.createStatement()) {   
                try {
                    st.executeUpdate(   "CREATE TABLE UTENTI(" +
                            "IDUSER         INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                            "USERNAME       VARCHAR(30) NOT NULL," +
                            "PASSWORD       VARCHAR(30) NOT NULL," +
                            "PERMISSION     VARCHAR(30) NOT NULL," +
                            "PRIMARY KEY(IDUSER))");
                    
                } catch (SQLException e){System.out.println(e.getMessage());}
                try {
                    st.executeUpdate(   "CREATE TABLE PIZZE(" +
                            
                            "IDPIZZA        INT         NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                            "NOME           VARCHAR(30) NOT NULL," +
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
     */
    
    public static boolean addPizza(String nome, String ingr, double prezzo){
        return esegui("INSERT INTO PIZZE (NOME, INGREDIENTI, PREZZO) VALUES ('"+nome+"', '"+ingr+"', "+prezzo+")");
    }

////////////////////////////////////////////////////////////////////////////////
   
    /**
     * Rimuove una pizza dalla tabella PIZZE
     * 
     * @param nome      nome della pizza
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean remPizza(String nome){
        return esegui("DELETE FROM PIZZE WHERE (NOME='"+nome+"')");
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
     */
    
    public static boolean modPizza(String nome, String nIngr, double nPrezzo){
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
     */
    
    public static boolean addLogin(String nome, String password, String ruolo){
        return esegui("INSERT INTO UTENTI (USERNAME, PASSWORD, PERMISSION) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Rimuove un utente dalla tabella UTENTI
     * 
     * @param nome      nome dell'utente
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean remLogin(String nome){
        return esegui("DELETE FROM UTENTI WHERE (USERNAME='"+nome+"')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Modifica un utente nella tabella UTENTI
     * 
     * @param nome      nome attuale dell'utente
     * @param nNome     nuovo nome dell'utente
     * @param nPassword nuova password dell'utente
     * @param nRuolo    nuovi permessi dell'utente;
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean modLogin(String nome, String nNome, String nPassword, String nRuolo){
        return esegui("UPDATE UTENTI SET USERNAME='" + nNome+ "', PASSWORD='" +nPassword+"', PERMISSION ='" +nRuolo+"' WHERE USERNAME = '"+ nome +"'");
    }
    
////////////////////////////////////////////////////////////////////////////////

    /**
     * Recupera la lista degli utenti e genera un array di Stringhe con tutti i dati
     * 
     * @return          ritorna un ArrayList \<String\> contenente i risultati della query
     */
    
    public static ArrayList<String> getAllLogin(){
        return query("SELECT * FROM UTENTI");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Ritorna una Stringa contenente l'utente
     * 
     * @param usr       nome dell'utente
     * 
     * @return          ritorna una Stringa contenente il risultato della query
     */
    
    public static String getLogin(String usr){
        return query("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"'").get(0);
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
     */
    
    public static boolean addPrenotazione(int cliente, int pizza, int quantita, String data){
        
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
     */
    
    public static boolean remPrenotazione(int cliente, int pizza, String data){
        return esegui("DELETE FROM PRENOTAZIONI WHERE (IDUTENTE= "+cliente+ "AND IDPIZZA= "+pizza+ " AND DATA= '"+data+ "')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Esegue una query SQL
     * 
     * @param sql       query sql da eseguire
     * 
     * @return          ritorna un booleano
     */
    
    public static boolean esegui(String sql) {
        try{
            try (Connection conn = DriverManager.getConnection(ur, us, p); Statement st = conn.createStatement()) {
                st.executeUpdate(sql);
            }
            System.out.println("Ho eseguito: "+sql);
        } catch (SQLException e){
            System.out.println(e.getMessage() + ": errore carica : "+sql);
            return false;
        }
        return true;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Popola il database
     */
    
    public static void startDati() { //startDati(String tab, String nome, String mezzo, String fine)
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
      * Salva i dati ottenuti da una query in un \<ArrayList\> di Stringhe
      * @param query    Stringa di una query SQL
      * 
      * @return ArrayList \<String\>
      */
    
    public static ArrayList<String> query(String query){
        Connection conn=null;
        Statement st=null;
        ResultSet rs = null;
        @SuppressWarnings("UnusedAssignment")
        ResultSetMetaData rsmd = null;
        ArrayList<String> output=null;
        try{
            conn = DriverManager.getConnection(ur, us, p);
            st = conn.createStatement();
            rs = st.executeQuery(query);
            rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount()+1;
            output = new ArrayList<>();
            while(rs.next()){
                String temp = "";
                for(int i = 1; i < n; i++){
                    if(i != 1)
                        temp += "-";
                    temp += rs.getString(i);
                }
                output.add(temp);
            }
            if(output.isEmpty())
                output.add("");
            
        }catch(SQLException e){
                System.out.println(e.getMessage() + ": errore query : "+query);
        } finally {
            try{
                if(rs!=null)    rs.close();
                if(st!=null)    st.close(); 
                if(conn!=null)  conn.close();
            }catch(SQLException e){
                System.out.println(e.getMessage() + ": errore close");
            }
        }
        
        return output;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Elimina le tabelle del Database
     */
    
    public static void drop(){
        
        System.out.println(esegui("DROP TABLE PRENOTAZIONI"));
        System.out.println(esegui("DROP TABLE UTENTI"));
        System.out.println(esegui("DROP TABLE PIZZE"));
    
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
        System.out.println("Create login: " + addLogin("puppa", "Puppafava1", "user"));
        System.out.println("Create login: " + addPizza("pezza", "Puppafava1", 1.0));
        System.out.println("Create pren1: " + addPrenotazione(getIdUser("puppa"), getIdPizza("pezza"),3, "oggi"));
        System.out.println("Create pren2: " + addPrenotazione(getIdUser("puppe"), getIdPizza("pezza"),3, "oggi"));
        System.out.println("Create pren3: " + addPrenotazione(getIdUser("puppa"), getIdPizza("paullo"),3, "oggi"));
        System.out.println("id="+getIdPrenotazione(getIdUser("puppa"), getIdPizza("pezza"), "oggi"));
        System.out.println("id="+getIdPrenotazione(getIdUser("puppa2"), getIdPizza("pezza"), "oggi"));
        System.out.println("id="+getIdPrenotazione(getIdUser("puppa"), getIdPizza("pezza2"), "oggi"));
        System.out.println("id="+getIdPrenotazione(getIdUser("puppa"), getIdPizza("pezza"), "oggi2"));
        System.out.println("rem: "+remPrenotazione(getIdUser("puppa"), getIdPizza("pezza"), "oggi"));
        System.out.println("rem: "+remPrenotazione(getIdUser("puppa"), getIdPizza("pezza"), "oggi"));
    }
*/

}