package mypackage;

import java.sql.*;
import java.util.ArrayList;

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
                st.executeUpdate(   "CREATE TABLE UTENTE(" +
                        
                                    "IDUSER         INT AUTO_INCREMENT      PRIMARY KEY ," +
                                    "USERNAME       VARCHAR(30)             PRIMARY KEY ," +
                                    "PASSWORD       VARCHAR(30)             NOT NULL    ," +
                                    "PERMISSION     VARCHAR(30)             NOT NULL    )");
                
            } catch (SQLException e){System.out.println(e.getMessage());}
            try {
                st.executeUpdate(   "CREATE TABLE PIZZA(" +
                        
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
        return esegui("INSERT INTO UTENTI (NOME, PASSWORD, RUOLO) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')");
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param nome = nome dell'utente
     * @return true oppure false
     * 
     * Rimuove un utente dalla tabella UTENTI
     */
    
    public static boolean remLogin(String nome){
        return esegui("DELETE FROM UTENTI WHERE (NOME='"+nome+"')");
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
        return esegui("UPDATE FROM SET NOME='" + nNome+ "', PASSWORD=" +nPassword+" RUOLO ='" +nRuolo+"'");
    }
    
////////////////////////////////////////////////////////////////////////////////

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! WTF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //da sostituire con una query "select count"
    
    /**
     * @return ArrayList<String[]>
     * 
     * Recupera la lista degli utenti e genera un array di Stringhe con tutti i dati
     */
    
    public static ArrayList<String[]> getAllLogin(){
        return query("SELECT * FROM UTENTI",false);
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! WTF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // vedi sopra
    
    /**
     * @param usr = nome dell'utente
     * @param pwd = password dell'utente
     * @return String[]
     * 
     * Ritorna un array di Stringhe
     */
    
    public static String[] getLogin(String usr,String pwd){
        ArrayList<String[]> temp= query("SELECT * FROM UTENTI WHERE NOME='"+usr+"' AND PASSWORD ='"+pwd+"'",false);
        if(temp.isEmpty())
            return null;
        else
            return temp.get(0);
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
    
    public static void addPrenotazioneanna(String cliente,String data, String pizza, int quantita){

         String sql="INSERT INTO PRENOTAZ (CLIENTE,PIZZA,QUANTITA,DATA,STATO) VALUES ";
        
            sql+="('"+cliente+"', '"+pizza+"',"+quantita+", '"+data+"', 'Ordinato')";
            
        esegui(sql);
        
        
    }
    
    /*
    public static void addPrenotazione(String cliente,String data, String pizza, int quantita){
        String[]pi={pizza};
        int[]qu={quantita};
        addPrenotazione(cliente,data,pi,qu);
    }
    public static void addPrenotazione(String cliente,String data, String[] pizza, int[] quantita){
        String sql="INSERT INTO PRENOTAZ (CLIENTE,PIZZA,QUANTITA,DATA,STATO) VALUES ";
        for(int i=0;i<pizza.length && i<quantita.length;i++){
            sql+="('"+cliente+"', '"+pizza[i]+""
                    + "',"+quantita[i]+", '"+data+"', 'Ordinato')";
            if(i+1<pizza.length)
                sql+=",";
        }
        esegui(sql);
    }*/
    //la prenotazione deve essere rimossa tramite id
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param cliente = cliente dal quale rimuovere la prenotazione 
     * 
     * Rimuove una prenotazione
     */
    
    public static void remPrenotazione(String cliente){
        remPrenotazione(cliente,null,null);
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param cliente = cliente dal quale rimuovere la prenotazione 
     * @param data = data della prenotazione da rimuovere
     * 
     * Rimuove una prenotazione
     */
    
    public static void remPrenotazione(String cliente, String data){
        remPrenotazione(cliente,data,null);
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param cliente = cliente dal quale rimuovere la prenotazione
     * @param data = data della prenotazione da rimuovere
     * @param pizza = pizza da rimuovere
     * 
     * Rimuove una prenotazione
     */
    
    public static void remPrenotazione(String cliente, String data,String pizza){
        String sql="DELETE FROM PRENOTAZ WHERE (CLIENTE= '"+cliente+ "'";
        if(data!=null){
            sql+=" AND DATA= '"+data+ "'";
            if(pizza!=null)
                sql+=" AND PIZZA= '"+pizza+ "'";
        }
        sql+=")";
        esegui(sql);
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

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! WTF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    public static ArrayList<String[]> query(String query,boolean h) {
        ArrayList<String[]> out=new ArrayList<String[]>();
        try{
            Connection conn = DriverManager.getConnection(ur, us, p);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int num = md.getColumnCount();
            String [] temp = new String[num];
            //carica i metadata in riga 0
            if(h){
                for(int i=0;i<num;i++)
                    temp[i]=md.getColumnName(i+1);
                out.add(temp);
            }
            //carica i risultati della query da 1 in poi
            while(rs.next()){
                temp = new String[num];
                for (int i=0; i<num; i++)
                   temp[i]=rs.getString(md.getColumnName(i+1));
                out.add(temp);
            }
            rs.close(); 
            st.close(); 
            conn.close();
       } catch (SQLException e){
            System.out.println(e.getMessage() + ": errore query : "+query);
       }
       return out;
    }
    
////////////////////////////////////////////////////////////////////////////////
    
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! WTF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    public static int numRighe(String query) { //static boolean?
        int i=0;
        try {
            Connection conn = DriverManager.getConnection(ur, us, p);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
                i++;
            rs.close(); 
            st.close(); 
            conn.close();
            return i;
        }catch(SQLException e){
            System.out.println(e.getMessage() + ": errore query : "+query);
            return -1;
        }  
    }
    
////////////////////////////////////////////////////////////////////////////////
    
}
    

