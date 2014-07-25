/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mvc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mirko
 */
public class Tester {
    public static void main(String[]args) throws SQLException{
        DBManager dbman=new DBManager();
        testUser(dbman,true,true,true,true);
    }
    public static void testUser(DBManager dbman,boolean add,boolean get,boolean mod, boolean rem) throws SQLException{
        String user="user";
        String pwd="pwd";
        String ruolo="ruolo";
        
        String pizza = "pizza";
        String ingr = "ingredienti";
        double prezzo = 0.0;
        
        int quantita = 1;
        String data = "oggi";
        
        int outI;
        String outS;
        boolean outB;
        ResultSet rs;
        
        
        
       
        
        if(add){
            
            //////////////////////////////////////////////////////////////////////////////
            //UTENTI
            

            //test addUser
            dbman.openConnection();
            outI=dbman.addUser(user,pwd,ruolo);
            System.out.println("test addUser(>0) = "+outI);
            dbman.closeConnection();
            
            try{
                //test addUser con user esistente
                outI = -1;
                dbman.openConnection();
                if(dbman.getIdUser(user)<0)
                    outI=dbman.addUser(user,pwd+"1",ruolo+"1");
                System.out.println("test addUser con user esistente(-1) = "+outI);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: Utente doppio!!!!!!!");}
            
            //test addUser con pwd esistente
            dbman.openConnection();
            outI=dbman.addUser(user+"2",pwd,ruolo+"2");
            System.out.println("test addUser con pwd esistente(>0) = "+outI);
            dbman.closeConnection();
            
            //test addUser con ruolo esistente
            dbman.openConnection();
            outI=dbman.addUser(user+"3",pwd+"3",ruolo);
            System.out.println("test addUser con ruolo esistente(>0) = "+outI);
            dbman.closeConnection();
            
            System.out.println("\n\n");
            
            //PIZZE
            
            //test addPizza
            dbman.openConnection();
            outI=dbman.addPizza(pizza,ingr,prezzo);
            System.out.println("test addPizza(>0) = "+outI);
            dbman.closeConnection();
            
            try{
                //test addPizza con nome esistente
                outI = -1;
                dbman.openConnection();
                if(dbman.getIdUser(user)<0)
                    outI=dbman.addPizza(pizza,ingr+"1",prezzo+1);
                System.out.println("test addPizza con nome esistente(-1) = "+outI);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: Pizza doppia!!!!!!!");}
            
            //test addPizza con ingr esistente
            dbman.openConnection();
            outI=dbman.addPizza(pizza+"2",ingr,prezzo+2);
            System.out.println("test addPizza con ingr esistente(>0) = "+outI);
            dbman.closeConnection();
            
            //test addPizza con prezzo esistente
            dbman.openConnection();
            outI=dbman.addPizza(pizza+"3",ingr+"3",prezzo);
            System.out.println("test addPizza con prezzo esistente(>0) = "+outI);
            dbman.closeConnection();
            
            System.out.println("\n\n");
            
            //PRENOTAZIONI
            
            //test addPrenotazione
            dbman.openConnection();
            outI=dbman.addPrenotazione(dbman.getIdUser(user),dbman.getIdPizza(pizza),quantita,data);
            System.out.println("test addPrenotazione(>0) = "+outI);
            dbman.closeConnection();
            
            try{
                //test addPrenotazione con utente e pizza esistenti
                outI = -1;
                dbman.openConnection();
                if(dbman.getIdPrenotazione(dbman.getIdUser(user), dbman.getIdPizza(pizza), data) < 0)
                    outI=dbman.addPrenotazione(dbman.getIdUser(user),dbman.getIdPizza(pizza),quantita,data);
                System.out.println("test addPrenotazione con nome esistente(-1) = "+outI);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: Prenotazione doppia!!!!!!!");}
            
            //test addPrenotazione con utente esistente
            dbman.openConnection();
            outI=dbman.addPrenotazione(dbman.getIdUser(user),dbman.getIdPizza(pizza)+1,quantita+1,data+"1");
            System.out.println("test addPrenotazione con ingr esistente(>0) = "+outI);
            dbman.closeConnection();
            
            //test addPizza con pizza esistente
            dbman.openConnection();
            outI=dbman.addPrenotazione(dbman.getIdUser(user)+1,dbman.getIdPizza(pizza),quantita+2,data+"2");
            System.out.println("test addPrenotazione con prezzo esistente(>0) = "+outI);
            dbman.closeConnection();
            
            //test addPrenotazione con utente e pizza esistente
            dbman.openConnection();
            outI=dbman.addPrenotazione(dbman.getIdUser(user)+2,dbman.getIdPizza(pizza)+2,quantita,data+"3");
            System.out.println("test addPrenotazione con ingr esistente(>0) = "+outI);
            dbman.closeConnection();
            
            //test addPrenotazione con data esistente
            dbman.openConnection();
            outI=dbman.addPrenotazione(dbman.getIdUser(user)+1,dbman.getIdPizza(pizza)+2,quantita,data);
            System.out.println("test addPrenotazione con ingr esistente(>0) = "+outI);
            dbman.closeConnection();
            
            //////////////////////////////////////////////////////////////////////////////
            
            System.out.println("\n\n");
        }
        if(get){
            
            //////////////////////////////////////////////////////////////////////////////
            
            //test getIdUser(String)
            dbman.openConnection();
            outI=dbman.getIdUser(user);
            System.out.println("test getIdUser(>0) = "+outI);
            dbman.closeConnection();
            
            //test getIdUser(String)  inesistente
            try{
                dbman.openConnection();
                outI=dbman.getIdUser(user+"k");
                System.out.println("test getIdUser con user inesistente(-1) = "+outI);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            //////////////////////////////////////////////////////////////////////////////
            
            //test getUser(int)
            dbman.openConnection();
            rs=dbman.getUser(1);
            outS=stampaUtente(rs);
            System.out.println("test getIdUser(int) = "+outS);
            dbman.closeConnection();
            
            //test getUser(int) con id inesistente
            try{
                dbman.openConnection();
                rs=dbman.getUser(99999999);
                outS=stampaUtente(rs);
                System.out.println("test getIdUser(int) con id inesistente = "+outS);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            //test getUser(String)
            dbman.openConnection();
            rs=dbman.getUser(user);
            outS=stampaUtente(rs);
            System.out.println("test getUser(String) = "+outS);
            dbman.closeConnection();
            
            //test getUser(String) con id inesistente
            try{
                dbman.openConnection();
                rs=dbman.getUser(user+"gnagna");
                outS=stampaUtente(rs);
                System.out.println("test getUser(String) con user inesistente = "+outS);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            //////////////////////////////////////////////////////////////////////////////
            
            System.out.println("\n\n");
        }

        if(mod){
            
            //test modUser
            dbman.openConnection();
            dbman.modUser(user,pwd+"mod"+user,ruolo+"mod"+user);
            System.out.println("OK");
            dbman.closeConnection();
            
            //test modUser con user inesistente
            dbman.openConnection();
            dbman.modUser(user+"papapa",pwd+"mod"+user,ruolo+"mod"+user);
            dbman.closeConnection();
            System.out.println("KO");

        }
        
    }
    public static String stampaUtente(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="ID: "+rs.getInt("IDUSER")+";\t Username: "+rs.getString("USERNAME")+"\t Password: "+rs.getString("PASSWORD")+";\t Permission: "+rs.getString("PERMISSION")+";";
        return out;
    }
}

