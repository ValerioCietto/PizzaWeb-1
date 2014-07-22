package mypackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBManager {
    
    static String ur = "jdbc:derby://localhost:1527/sample";
    static String us= "app";
    static String p= "app";
    static int idordine=0;

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param args the command line arguments
     */
    
    /*
    public static void main(String[]args){
        inizializza();
    }*/
    
    public static void inizializza(){
       try {
            // registrazione driver JDBC da utilizzare
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            ///se la tabella non esiste o ha i metadata diversi{
            creaTabelle();
            startDati();
            ///}
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
        Statement st = conn.createStatement();
            try {   
                st.executeUpdate(   "CREATE TABLE UTENTI(" +
                        
                                    "IDUSER         INT AUTO_INCREMENT      PRIMARY KEY ," +
                                    "USERNAME       VARCHAR(30)             PRIMARY KEY ," +
                                    "PASSWORD       VARCHAR(30)             NOT NULL    ," +
                                    "PERMISSION     VARCHAR(30)             NOT NULL    )");
                
            } catch (SQLException e){System.out.println(e.getMessage());}
            try {
                st.executeUpdate(   "CREATE TABLE PIZZE(" +
                        
                                    "IDPIZZA        INT AUTO_INCREMENT      PRIMARY KEY ," +
                                    "NOME           VARCHAR(30)             PRIMARY KEY ," +
                                    "INGREDIENTI    LONGTEXT                NOT NULL    ," +
                                    "PREZZO         DOUBLE                  NOT NULL    )");
                
            } catch (SQLException e){System.out.println(e.getMessage());}
            try {
                st.executeUpdate(   "CREATE TABLE PRENOTAZIONI(" +
                        
                                    "IDPRENOTAZIONE INT AUTO_INCREMENT      PRIMARY KEY ," +
                                    "IDUTENTE       INT                     NOT NULL    ," +
                                    "IDPIZZA        INT                     NOT NULL    ," +
                                    "QUANTITA       INT UNSIGNED            NOT NULL    ," +
                                    "DATA           VARCHAR(30)             NOT NULL    ," +
                                    "STATO          VARCHAR(30)             NOT NULL    ," +
                        
                                    "FOREIGN KEY(IDUTENTE) REFERENCES UTENTE(IDUTENTE)  ," +
                                    "FOREIGN KEY(IDPIZZA) REFERENCES PIZZA(IDPIZZA)     )");
                
            } catch (SQLException e){System.out.println(e.getMessage());}
            st.close();
         } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        //chiudo statement (non serve più)        
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param nome = nome della pizza
     * @param ingr = ingredienti della pizza
     * @param prezzo = prezzo della pizza
     * @return true oppure false
     * 
     * Aggiunge una pizza nella tabella PIZZE del database
     */
    
    public static boolean addPizza(String nome, String ingr, double prezzo){
        return esegui("INSERT INTO PIZZE (NOME, INGREDIENTI, PREZZO) VALUES ('"+nome+"', '"+ingr+"', "+prezzo+")");
    }

////////////////////////////////////////////////////////////////////////////////
   
    /**
     * @param nome = nome della pizza
     * @return true oppure false
     * 
     * Rimuove una pizza dalla tabella PIZZE
     */
    
    public static boolean remPizza(String nome){
        return esegui("DELETE FROM PIZZE WHERE (NOME='"+nome+"')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param nome = nome della pizza
     * @param nIngr = nuovi ingredienti della pizza
     * @param nPrezzo = nuovo prezzo della pizza
     * @return true oppure false
     * 
     * Modifica una pizza nella tabella PIZZE
     */
    
    public static boolean modPizza(String nome, String nIngr, double nPrezzo){
        return esegui("UPDATE PIZZE SET INGREDIENTI='" + nIngr+ "', PREZZO=" +nPrezzo+" WHERE NOME ='" +nome+"'");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param nome = nome dell'utente
     * @param password = password dell'utente
     * @param ruolo = permessi dell'utente
     * @return true oppure false
     * 
     * Inserisce un utente nella tabella UTENTI
     */
    
    public static boolean addLogin(String nome, String password, String ruolo){
        return esegui("INSERT INTO UTENTI (USERNAME, PASSWORD, PERMISSION) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param nome = nome dell'utente
     * @return true oppure false
     * 
     * Rimuove un utente dalla tabella UTENTI
     */
    
    public static boolean remLogin(String nome){
        return esegui("DELETE FROM UTENTI WHERE (USERNAME='"+nome+"')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param nome = nome attuale dell'utente
     * @param nNome = nuovo nome dell'utente
     * @param nPassword = nuova password dell'utente
     * @param nRuolo = nuovi permessi dell'utente;
     * @return true oppure false
     * 
     * Modifica un utente nella tabella UTENTI
     */
    
    public static boolean modLogin(String nome, String nNome, String nPassword, String nRuolo){
        return esegui("UPDATE FROM UTENTI SET USERNAME='" + nNome+ "', PASSWORD=" +nPassword+" PERMISSION ='" +nRuolo+"' WHERE USERNAME = '"+ nome +"'");
    }
    
////////////////////////////////////////////////////////////////////////////////

    /**
     * @return ArrayList<String>
     * 
     * Recupera la lista degli utenti e genera un array di Stringhe con tutti i dati
     */
    
    public static ArrayList<String> getAllLogin(){
        ArrayList<String> output = null;
        String tmp;
        ResultSet results = query("SELECT * FROM UTENTI");
        try {
            while(results.next()){
                tmp = "" + results.getString("IDUSER") + ";" + results.getString("USERNAME") + ";" + results.getString("PASSWORD") + ";" + results.getString("PERMISSION")+ ";";
                output.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param usr = nome dell'utente
     * @param pwd = password dell'utente
     * @return String
     * 
     * Ritorna una Stringa contenente l'utente
     */
    
    public static String getLogin(String usr,String pwd){
        String output = null;
        ResultSet results = query("SELECT * FROM UTENTI WHERE USERNAME='"+usr+"' AND PASSWORD ='"+pwd+"'");
        try {
            output += "" + results.getString("IDUSER") + ";" + results.getString("USERNAME") + ";" + results.getString("PASSWORD") + ";" + results.getString("PERMISSION")+ ";";
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param nome = nome della pizza
     * @return String;
     * 
     * Prende in input il nome della pizza e restituisce l'ID della pizza
     */
    
    public static int getIdPizza(String nome){
        String output;
        ResultSet results = query("SELECT ID FROM PIZZE WHERE NOME='"+nome+"'");
        try {
            output = results.getString("ID");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            output = "-1";
        }
        return Integer.parseInt(output);
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param username = nome utente
     * @param password = password utente
     * @return String;
     * 
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     */
    
    public static int getIdUser(String username){
        String output;
        ResultSet results = query("SELECT ID FROM UTENTI WHERE USERNAME='"+username+"'");
        try {
            output = results.getString("ID");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            output = "-1";
        }
        return Integer.parseInt(output);
    }
    
////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * @param username = nome utente
     * @param password = password utente
     * @return String;
     * 
     * Prende in input il nome utente e password e restituisce l'ID dell'utente
     */
    
    public static int getIdPrenotazione(int username, int pizza, String  data){
        String output;
        ResultSet results = query("SELECT ID FROM PRENOTAZIONI WHERE IDUTENTE='"+username+"' AND IDPIZZA ='"+ pizza+"' AND DATA='"+data+"'");
        try {
            output = results.getString("ID");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            output = "-1";
        }
        return Integer.parseInt(output);
    }
    
////////////////////////////////////////////////////////////////////////////////       
    
    /**
     * @param cliente = cliente che effettua la prenotazione
     * @param data = data della prenotazione
     * @param pizza = tipo di pizza prenotata
     * @param quantita = quantità di pizze prenotate
     * 
     * Aggiunge una prenotazione ad un cliente
     */
    
    public static void addPrenotazione(int cliente, int pizza, int quantita, String data){
        
        String sql="INSERT INTO PRENOTAZIONI(IDUSER,IDPIZZA,QUANTITA,DATA,STATO) VALUES ";
        
            sql+="('"+cliente+"', '"+pizza+"',"+quantita+", '"+data+"', 'Ordinato')";
            
        esegui(sql);
        
        
    }

////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * @param cliente = cliente dal quale rimuovere la prenotazione
     * @param data = data della prenotazione da rimuovere
     * @param pizza = pizza da rimuovere
     * 
     * Rimuove una prenotazione
     */
    
    public static void remPrenotazione(int cliente, int pizza, String data){
        esegui("DELETE FROM PRENOTAZ WHERE (IDUSER= '"+cliente+ "' AND DATA= '"+data+ "' AND IDPIZZA= '"+pizza+ "')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param sql = query sql da eseguire
     * @return true oppure false
     * 
     * Esegue una query SQL
     */
    
    public static boolean esegui(String sql) {
        try{
            Connection conn = DriverManager.getConnection(ur, us, p);
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.close();
            conn.close();
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

    public static ResultSet query(String query){
        Connection conn;
        Statement st;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection(ur, us, p);
            st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.close(); 
            st.close(); 
            conn.close();
        }catch(SQLException e){
                System.out.println(e.getMessage() + ": errore query : "+query);
            }
        
        return rs;
    }
////////////////////////////////////////////////////////////////////////////////
}