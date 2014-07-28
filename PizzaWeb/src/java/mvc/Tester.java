package mvc;

import components.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.handler.MessageContext;

////////////////////////////////////////////////////////////////////////////////
// MAIN

public class Tester {
    public static void main(String[]args) throws SQLException{
        testDBManager(true,true,true,true);
        Model db = creaFreshDB();
        testDatabase(db);
        dropDB(db);
    }

////////////////////////////////////////////////////////////////////////////////
// UTILITY
    
    /**
     * Stampa il contenuto di un oggetto Utente
     * 
     * @param rs
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static String stampaUtente(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="ID: "+rs.getInt("IDUSER")+";\t Username: "+rs.getString("USERNAME")+";\t Password: "+rs.getString("PASSWORD")+";\t Permission: "+rs.getString("PERMISSION")+";";
        return out;
    }
    
    /**
     * Stampa il contenuto di un oggetto Pizza
     * 
     * @param rs
     * 
     * @return
     * 
     * @throws SQLException 
     */
        
    public static String stampaPizza(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="ID: "+rs.getInt("IDPIZZA")+";\t Nome: "+rs.getString("NOME")+";\t Ingredienti: "+rs.getString("INGREDIENTI")+";\t Prezzo: "+rs.getString("PREZZO")+";";
        return out;
    }
    
    /**
     * Stampa il contenuto di un oggetto Prenotazione
     * 
     * @param rs
     * 
     * @return
     * 
     * @throws SQLException 
     */
    
    public static String stampaPrenotazione(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="ID: "+rs.getInt("IDPRENOTAZIONE")+";\t IDUser: "+rs.getString("IDUTENTE")+";\t IDPizzA: "+rs.getString("IDPIZZA")+";\t Quantità: "+rs.getString("QUANTITA")+";\t Data"+rs.getString("DATA")+";";
        return out;
    }
    
    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DI TEST

        
    /**
     * Crea un oggetto DBManager ed inizializza il database;
     * esegue i test di add, rem, get e mod su DBManager;
     * esegue il drop del database
     * 
     * @param add
     * @param get
     * @param mod
     * @param rem
     * 
     * @throws SQLException 
     */
    
    public static void testDBManager(boolean add,boolean get,boolean mod, boolean rem) throws SQLException{
        
        DBManager dbman = new DBManager();
        
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
        ResultSet rs;
        
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        // START ADD
        
        if(add){
            
            ////////////////////////////////////////////////////////////////////
            // UTENTI
            
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
            
            ////////////////////////////////////////////////////////////////////
            // PIZZE
            
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
            
            ////////////////////////////////////////////////////////////////////
            // PRENOTAZIONI
            
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
            
            System.out.println("\n\n");  
        }
        
        
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        // START GET
        
        if(get){
            
            ////////////////////////////////////////////////////////////////////
            // ID UTENTI
            
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
            
            ////////////////////////////////////////////////////////////////////
            // UTENTI
            
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
            
            ////////////////////////////////////////////////////////////////////
            // ID PIZZE
            
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
            
            ////////////////////////////////////////////////////////////////////
            // PIZZE
            
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
            
            ////////////////////////////////////////////////////////////////////
            // ID PRENOTAZIONI
            
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
            
            ////////////////////////////////////////////////////////////////////
            // PRENOTAZIONI
            
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
            
            System.out.println("\n\n");
        }
        
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        // START MOD
        
        if(mod){
            
            ////////////////////////////////////////////////////////////////////
            // UTENTI
            
            //test modUser
            dbman.openConnection();
            dbman.modUser(user,pwd+"mod"+user,ruolo+"mod1");
            System.out.println("OK");
            dbman.closeConnection();
            
            //test modUser con user inesistente
            dbman.openConnection();
            dbman.modUser(user+"papapa",pwd+"mod"+user,ruolo+"mod2");
            dbman.closeConnection();
            System.out.println("KO utente");
            
            ////////////////////////////////////////////////////////////////////
            // PIZZE
            
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
            
            ////////////////////////////////////////////////////////////////////
            // PRENOTAZIONI
            
            //test modPrenotazione
            dbman.openConnection();
            dbman.modPrenotazione(1, quantita+9, data+"1");
            System.out.println("OK");
            dbman.closeConnection();
            
            //test modPrenotazione con user inesistente
            dbman.openConnection();
            dbman.modPrenotazione(999999, quantita+9999, data+"1");
            dbman.closeConnection();
            System.out.println("KO prenotazione");
        }
        dbman.drop();
    }
    
    /**
     * Crea un Database pulito
     * 
     * @return 
     * 
     * @throws SQLException 
     */
    
    public static Model creaFreshDB() throws SQLException{
        return new Model();
    }

    /**
     * Esegue i test di add, mod e rem su Database
     * 
     * @param db
     * 
     * @throws SQLException 
     */
    
    private static void testDatabase(Model db) throws SQLException {
        
        ////////////////////////////////////////////////////////////////////////
        // START ADD
        
        db.addUtente(new Utente("username", "password", "user"));
        db.addUtente(new Utente("username", "password", "user"));
        Utente ut = db.getUtente("username");
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
        
        ////////////////////////////////////////////////////////////////////////
        // START MOD
        
        pr.setQuantita(90);
        pz.setIngredienti("caccadura");
        ut.setPwd("gnagna");
        
        db.modPrenotazione(pr);
        db.modPizza(pz);
        db.modUtente(ut);
        
        System.out.println("\n\n");
        System.out.println(ut);
        System.out.println(pz);
        System.out.println(pr);
        
        ////////////////////////////////////////////////////////////////////////
        // START REM
        
        db.remPrenotazione(pr);
        db.remPizza(pz);
        db.remUtente(ut);
        
        System.out.println("\n\n");
        System.out.println(ut);
        System.out.println(pz);
        System.out.println(pr);
    }
    
    /**
     * Elimina le tabelle dal database in questione
     * 
     * @param db
     * @throws java.sql.SQLException
     */
    
    public static void dropDB(Model db) throws SQLException{
        db.drop();
    }

    public static void testController(Controller ctl){
        String username="username";
        String password="password";
        HttpSession s = req.getSession();
        ctl.logout(req);//errore non sei loggato
        ctl.login(req);//errore mancano parametri
        s.setAttribute("username", username);
        s.setAttribute("password", password);
        ctl.login(req);//ok
        ctl.login(req);//errore sei gia loggato
        ctl.register(req);//errore sei gia loggato
        ctl.logout(req);//ok
        ctl.register(req);//ok
        ctl.login(req);//ok
        
    
    }
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

}

