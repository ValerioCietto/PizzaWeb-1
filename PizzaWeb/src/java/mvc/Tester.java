/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mvc;

import components.Prenotazione;
import components.Pizza;
import components.Utente;
import components.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author mirko
 */

public class Tester {
    public static void main(String[]args) throws SQLException{
        DBManager dbman=new DBManager();
        //testDB(dbman,true,true,true,true);
        testObj();
    }
    
    public static void testDB(DBManager dbman,boolean add,boolean get,boolean mod, boolean rem) throws SQLException{
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
            rs = dbman.getUser(1);
            outS = stampaUtente(rs);
            System.out.println("test getIdUser(int) = "+outS);
            dbman.closeConnection();
            
            //test getUser(int) con id inesistente
            try{
                dbman.openConnection();
                rs = dbman.getUser(99999999);
                outS = stampaUtente(rs);
                System.out.println("test getIdUser(int) con id inesistente = "+outS);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            //test getUser(String)
            dbman.openConnection();
            rs = dbman.getUser(user);
            outS = stampaUtente(rs);
            System.out.println("test getUser(String) = "+outS);
            dbman.closeConnection();
            
            //test getUser(String) con id inesistente
            try{
                dbman.openConnection();
                rs = dbman.getUser(user+"gnagna");
                outS = stampaUtente(rs);
                System.out.println("test getUser(String) con user inesistente = "+outS);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            
            //////GET PIZZA /////////
            
            //test getIdpizza(String)
            dbman.openConnection();
            outI=dbman.getIdPizza(pizza);
            System.out.println("test getIdPizza(>0) = "+outI);
            dbman.closeConnection();
            
             //test getIdpizza(String)  inesistente
            try{
                dbman.openConnection();
                outI = dbman.getIdPizza(pizza+"k");
                System.out.println("test getIdpizza con pizza inesistente(-1) = "+outI);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID di pizza inesistente!!!!!!!");}
            
            
            //test getPizza(int)
            dbman.openConnection();
            rs = dbman.getPizza(1);
            outS = stampaPizza(rs);
            System.out.println("test getPizza(int) = "+outS);
            dbman.closeConnection();
            
            //test getPizza(int) con id inesistente
            try{
                dbman.openConnection();
                rs = dbman.getPizza(99999999);
                outS = stampaPizza(rs);
                System.out.println("test getPizza(int) con id inesistente = "+outS);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            //test getPizza(String)
            dbman.openConnection();
            rs = dbman.getPizza(pizza);
            outS = stampaPizza(rs);
            System.out.println("test getPizza(String) = "+outS);
            dbman.closeConnection();
            
            //test getPizza(String) con id inesistente
            try{
                dbman.openConnection();
                rs = dbman.getPizza(pizza+"gnagna");
                outS = stampaPizza(rs);
                System.out.println("test getPizza(String) con pizza inesistente = "+outS);
                dbman.closeConnection();
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            
            ////GET PRENOTAZIONE//////////////////////////////////////////////////////
            
            int a,b;
             //test getIdprenotazione(int, int, String)
            dbman.openConnection();
            a = dbman.getIdUser(user); 
            b = dbman.getIdPizza(pizza);
            outI = dbman.getIdPrenotazione(a, b, data);
            System.out.println("test getIdPrenotazione(>0) = "+outI);
            dbman.closeConnection();
            
            //test getIdprenotazione(int inesistente, int inesistente, String)
            try{
            dbman.openConnection();
            a = dbman.getIdUser(user+77); 
            b = dbman.getIdPizza(pizza + 33);
            outI = dbman.getIdPrenotazione(a, b, data);
            System.out.println("test getIdPrenotazione(>0) = "+outI);
            dbman.closeConnection();
             }catch(SQLException e){System.out.println("ERRORE: user e/o pizza inesistente!!!!!!!");}
            
            
            //test getPrenotazione(int)
            dbman.openConnection();
            rs = dbman.getPrenotazione(1);
            outS = stampaPrenotazione(rs);
            System.out.println("test getPrenotazione(int) = "+outS);
            dbman.closeConnection();
            
            //test getPrenotazione(int) con id inesistente
            try{
                dbman.openConnection();
                rs = dbman.getPrenotazione(99999999);
                outS = stampaPrenotazione(rs);
                System.out.println("test getPrenotazione(int) con id inesistente = "+outS);
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
            System.out.println("KO utente");
            
            ///////////////test pizza///////////////////////
            
            //test modPizza
            dbman.openConnection();
            dbman.modPizza(pizza,ingr+ "pippa", prezzo+21);
            System.out.println("OK");
            dbman.closeConnection();
            
            //test modUser con user inesistente
            dbman.openConnection();
            dbman.modPizza(pizza+"ciccio",ingr+ "pippa", prezzo+21);
            dbman.closeConnection();
            System.out.println("KO pizza");
            
            ///////////////test prenotazione///////////////////////
            
            //test modPrenotazione
            dbman.openConnection();
            dbman.modPrenotazione(1, quantita+9, data+"MOD");
            System.out.println("OK");
            dbman.closeConnection();
            
            //test modPrenotazione con user inesistente
            dbman.openConnection();
            dbman.modPrenotazione(999999, quantita+9999, data+"MOD");
            dbman.closeConnection();
            System.out.println("KO prenotazione");
        }
        //////////////////////////////////////////////////////////////////////////////        
    }
    
    public static void testObj() throws SQLException {
        Database db = new Database();
        
        db.addUser(new Utente("username", "password", "user"));
        db.addUser(new Utente("username", "password", "user"));
        Utente ut = db.getUser("username");
        System.out.println(ut);
        
        //ut = db.getUser("puppa");
        //System.out.println(ut);
        
        db.addPizza( new Pizza("margherita", "mozzarella, basilico, origano", 12.5d) );
        db.addPizza( new Pizza("margherita", "mozzarella, basilico, origano", 12.5d) );
        Pizza pz = db.getPizza("margherita");
        System.out.println(pz);
        
        //pz = db.getPizza("napoli");
        //System.out.println(pz);
        
        db.addPrenotazione( new Prenotazione(ut.getId(),pz.getId(), 5 , "oggi") );
        db.addPrenotazione( new Prenotazione(ut.getId(),pz.getId(), 5 , "oggi") );
        Prenotazione pr = db.getPrenotazione(db.getIdPrenotazione(ut.getId(), pz.getId(), "oggi"));
        System.out.println(pr);
        
        //pr = db.getPrenotazione(5);
        //System.out.println(pr);
        
        pr.setQuantita(90);
        pz.setIngredienti("caccadura");
        ut.setPwd("gnagna");
        
        db.modPrenotazione(pr);
        db.modPizza(pz);
        db.modUser(ut);
        
        System.out.println("\n\n");
        System.out.println(ut);
        System.out.println(pz);
        System.out.println(pr);
        
        //db.remPrenotazione(pr);
        //db.remPizza(pz);
        //db.remUser(ut);
        
        //System.out.println("\n\n");
        //System.out.println(ut);
        //System.out.println(pz);
        //System.out.println(pr);
        
        
    }
    
    public static String stampaUtente(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="ID: "+rs.getInt("IDUSER")+";\t Username: "+rs.getString("USERNAME")+";\t Password: "+rs.getString("PASSWORD")+";\t Permission: "+rs.getString("PERMISSION")+";";
        return out;
    }
    
    public static String stampaPizza(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="ID: "+rs.getInt("IDPIZZA")+";\t Nome: "+rs.getString("NOME")+";\t Ingredienti: "+rs.getString("INGREDIENTI")+";\t Prezzo: "+rs.getString("PREZZO")+";";
        return out;
    }
    
    public static String stampaPrenotazione(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="ID: "+rs.getInt("IDPRENOTAZIONE")+";\t IDUser: "+rs.getString("IDUTENTE")+";\t IDPizzA: "+rs.getString("IDPIZZA")+";\t Quantità: "+rs.getString("QUANTITA")+";\t Data"+rs.getString("DATA")+";";
        return out;
    }
}

