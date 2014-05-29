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
    /*public static void main(String[]args)
    {
        inizializza();
    }*/
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
        /*
            try {   
                st.executeUpdate("CREATE TABLE UTENTI" +
                        "(NOME VARCHAR(30)PRIMARY KEY, " +
                        "PASSWORD VARCHAR(30) NOT NULL, " + 
                        "RUOLO VARCHAR(30) NOT NULL)");
            } catch (SQLException e){System.out.println(e.getMessage());}
            try {
                st.executeUpdate("CREATE TABLE PIZZE(" +
                        "NOME VARCHAR(30) PRIMARY KEY, " +
                        "INGREDIENTI VARCHAR(40) NOT NULL, " +
                        "PREZZO DOUBLE)" );
            } catch (SQLException e){System.out.println(e.getMessage());}*/
            try {
                st.executeUpdate("CREATE TABLE PRENOTAZ(" +
                        "CLIENTE VARCHAR(30) NOT NULL, " +
                        "PIZZA VARCHAR(30) NOT NULL, " +
                        "QUANTITA INT NOT NULL," +
                        "DATA VARCHAR(30) NOT NULL,"+
                        "STATO VARCHAR(30) NOT NULL" +
                        //"CONSTRAINT prk PRIMARY KEY(CLIENTE,DATA,PIZZA)"+ non funziona
                        ")" );
            } catch (SQLException e){System.out.println(e.getMessage());}
            st.close();
         } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        //chiudo statement (non serve più)        
    }
    public static boolean addPizza(String nome, String ingr, double prezzo){
        return esegui("INSERT INTO PIZZE (NOME, INGREDIENTI, PREZZO) VALUES ('"+nome+"', '"+ingr+"', "+prezzo+")");
    }
    //DA VERIFICARE vvvvvv
   
    
    public static boolean remPizza(String nome){
        return esegui("DELETE FROM PIZZE WHERE (NOME='"+nome+"')");
    }   
    public static boolean modPizza(String nome, String nIngr, double nPrezzo){
        return esegui("UPDATE PIZZE SET INGREDIENTI='" + nIngr+ "', PREZZO=" +nPrezzo+" WHERE NOME ='" +nome+"'");
    }
    public static boolean addLogin(String nome, String password, String ruolo){
        return esegui("INSERT INTO UTENTI (NOME, PASSWORD, RUOLO) VALUES ('"+nome+"', '"+password+"', '"+ruolo+"')");
    }
    public static boolean remLogin(String nome){
        return esegui("DELETE FROM UTENTI WHERE (NOME='"+nome+"')");
    }
    public static boolean modLogin(String nome, String nNome, String nPassword, String nRuolo){
        return esegui("UPDATE FROM SET NOME='" + nNome+ "', PASSWORD=" +nPassword+" RUOLO ='" +nRuolo+"'");
    }
    public static ArrayList<String[]> getAllLogin(){
        return query("SELECT * FROM UTENTI",false);
    }
    public static String[] getLogin(String usr,String pwd){
        ArrayList<String[]> temp= query("SELECT * FROM UTENTI WHERE NOME='"+usr+"' AND PASSWORD ='"+pwd+"'",false);
        if(temp.isEmpty())
            return null;
        else
            return temp.get(0);
    }
    
    public static void addPrenotazioneanna(String cliente,String data, String pizza, int quantita){

         String sql="INSERT INTO PRENOTAZ (CLIENTE,PIZZA,QUANTITA,DATA,STATO) VALUES ";
        
            sql+="('"+cliente+"', '"+pizza+"',"+quantita+", '"+data+"', 'Ordinato')";
            
        esegui(sql);
        
        
    }/*
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
    public static void remPrenotazione(String cliente){
        remPrenotazione(cliente,null,null);
    }
    public static void remPrenotazione(String cliente, String data){
        remPrenotazione(cliente,data,null);
    }
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
}
    

