package mypackage;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    
    static String ur = "jdbc:derby://localhost:1527/sample";
    static String us= "app";
    static String p= "app";
    static int idordine=0;

    /**
     * @param args the command line arguments
     */
    public static void main(String []args){
        inizializza();
        ArrayList<String[]> login=query("SELECT * FROM UTENTI");
        System.out.println(login.size());
    }   
    public static void inizializza(){
       try { // registrazione driver JDBC da utilizzare
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            //creaTabella();
            //startDati();
       } catch (SQLException e) {System.out.println(e.getMessage());}
    }
    public static void creaTabella(){
        try {
            Connection conn = DriverManager.getConnection(ur,us,p);
            Statement st = conn.createStatement();
            //////////////////////////////////////////////////////////

                //st.execute("DROP TABLE PRENOTAZ");
                //st.execute("DROP TABLE UTENTI");
                //st.execute("DROP TABLE PIZZE");
            st.executeUpdate("CREATE TABLE UTENTI" +
                    "(NOME VARCHAR(30)PRIMARY KEY, " +
                    "PASSWORD VARCHAR(30) NOT NULL, " + 
                    "RUOLO VARCHAR(30) NOT NULL)");
            st.executeUpdate("CREATE TABLE PIZZE(" +
                    "NOME VARCHAR(30) PRIMARY KEY, " +
                    "INGREDIENTI VARCHAR(40) NOT NULL, " +
                    "PREZZO INT)" );
            st.executeUpdate("CREATE TABLE PRENOTAZ(" +
                    "IDPRENOT INT NOT NULL, " +
                    "CLIENTE VARCHAR(30) NOT NULL, " +
                    "PIZZA VARCHAR(30) NOT NULL, " +
                    "QUANTITA INT NOT NULL," +
                    "STATO VARCHAR(30) NOT NULL" +
                    "CONSTRAINT PRKEY PRIMARY KEY (IDPRENOT, PIZZA)"+
                    ")" );
            st.close();
         } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        //chiudo statement (non serve più)        
    }
    public static void addPizza(String nome, String ingr, int prezzo){
        esegui("INSERT INTO PIZZE (NOME, INGREDIENTI, PREZZO) VALUES ('"+nome+"', '"+ingr+"', "+prezzo+")");
    }
    public static void removePizza(String nome){
        esegui("DELETE FROM PIZZE WHERE (NOME='"+nome+"')");
    }
    
    public static void updatePizza(String nome, String nIngr, double nPrezzo){
        esegui("UPDATE PIZZE SET INGREDIENTI='" + nIngr+ "', PREZZO=" +nPrezzo+" WHERE NOME ='" +nome+"'");
    }
    
    //UPDATE PIZZE SET INGREDIENTI='VAL',... WHERE NOME='KEY'
    public static void addLogin(String nome, String password, String ruolo){
        esegui("INSERT INTO UTENTI (NOME, PASSWORD, RUOLO) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')");
    }
    public static void removeLogin(String nome){
        esegui("DELETE FROM UTENTI WHERE (NOME='"+nome+"')");
    }
    public static void addPrenotazione(String Cliente, String[] pizza, int[] quantita){
        idordine+=1;
        String sql="INSERT INTO PRENOTAZ (IDPRENOT,CLIENTE,PIZZA,QUANTITA,STATO) VALUES ";
        for(int i=0;i<pizza.length && i<quantita.length;i++){
            sql+="("+ idordine +", "+Cliente+", '"+pizza[i]+"', "+quantita[i]+", Ordinato)";
            if(i-1<pizza.length)
                sql+=",";
        }
        esegui(sql);
    }
    /*public static void editSqlAdd(String tab, String nome, String mezzo, String fine){
        if (tab.equals("utenti"))
            addLogin(nome, mezzo, fine);
        else if(tab.equals("pizze"))
            addPizza(nome, mezzo, Integer.parseInt(fine));
    }*/
    public static void esegui(String sql) {
        try{
            Connection conn = DriverManager.getConnection(ur, us, p);
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.close();
            conn.close();
            System.out.println("Ho eseguito: "+sql);
        } catch (SQLException e){
            System.out.println(e.getMessage() + ": errore carica : "+sql);
        }
    }
    public static void startDati() { //startDati(String tab, String nome, String mezzo, String fine)
        addLogin("admin","admin","admin");
        addLogin("user","user","user");
        addPizza("Margherita","pomodoro e mozzarella", 15);
        addPizza("Funghi","pomodoro e funghi", 6);
    }
    public static ArrayList<String[]> query(String query) {
        ArrayList<String[]> out=new ArrayList<String[]>();
        try{
            Connection conn = DriverManager.getConnection(ur, us, p);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int num = md.getColumnCount();
            String [] temp = new String[num];
            //carica i metadata in riga 0
            for(int i=0;i<num;i++)
                temp[i]=md.getColumnName(i+1);
            out.add(temp);
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
    /*public static void queryMetadata(String tabella){
       try {
           Connection conn = DriverManager.getConnection(ur, us, p);
           Statement st = conn.createStatement();
           ResultSet rs = st.executeQuery("SELECT * FROM "+tabella);
           ResultSetMetaData md = rs.getMetaData();
           int num = md.getColumnCount();
           System.out.println("Numero di colonne: " + num);
           for (int i=1; i<=num; i++)
               System.out.println(md.getColumnName(i) +" " + md.getColumnType(i));
           rs.close(); st.close();
       }catch(SQLException e){
           System.out.println(e.getMessage() + ": errore metadata : "+tabella);
       }
    }*/
}
    

