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
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        DBManager.creaTabelle(st);
        DBManager.inizializza(st);
        //testDBManager(true,true,true,true, st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
        //testModel();
        //dropDB();
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
     * @param st
     * 
     * @throws SQLException 
     */
    
    public static void testDBManager(boolean add,boolean get,boolean mod, boolean rem, Statement st) throws SQLException{
        
        DBManager.creaTabelle(st);
        DBManager.inizializza(st);
        
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
            outI=DBManager.addUser(user,pwd,ruolo,st);
            System.out.println("test addUser(>0) = "+outI);
            
            try{
                //test addUser con user esistente
                outI = -1;
                if(DBManager.getIdUser(user,st)<0)
                    outI=DBManager.addUser(user,pwd+"1",ruolo+"1",st);
                System.out.println("test addUser con user esistente(-1) = "+outI);
            }catch(SQLException e){System.out.println("ERRORE: Utente doppio!!!!!!!");}
            
            //test addUser con pwd esistente
            outI=DBManager.addUser(user+"2",pwd,ruolo+"2",st);
            System.out.println("test addUser con pwd esistente(>0) = "+outI);
            
            //test addUser con ruolo esistente
            outI=DBManager.addUser(user+"3",pwd+"3",ruolo,st);
            System.out.println("test addUser con ruolo esistente(>0) = "+outI);
            
            System.out.println("\n\n");
            
            ////////////////////////////////////////////////////////////////////
            // PIZZE
            
            //test addPizza
            outI=DBManager.addPizza(pizza,ingr,prezzo,st);
            System.out.println("test addPizza(>0) = "+outI);
            
            try{
                //test addPizza con nome esistente
                outI = -1;
                if(DBManager.getIdUser(user,st)<0)
                    outI=DBManager.addPizza(pizza,ingr+"1",prezzo+1,st);
                System.out.println("test addPizza con nome esistente(-1) = "+outI);
            }catch(SQLException e){System.out.println("ERRORE: Pizza doppia!!!!!!!");}
            
            //test addPizza con ingr esistente
            outI=DBManager.addPizza(pizza+"2",ingr,prezzo+2,st);
            System.out.println("test addPizza con ingr esistente(>0) = "+outI);
            
            //test addPizza con prezzo esistente
            outI=DBManager.addPizza(pizza+"3",ingr+"3",prezzo,st);
            System.out.println("test addPizza con prezzo esistente(>0) = "+outI);
            
            System.out.println("\n\n");
            
            ////////////////////////////////////////////////////////////////////
            // PRENOTAZIONI
            
            //test addPrenotazione
            outI=DBManager.addPrenotazione(DBManager.getIdUser(user,st),DBManager.getIdPizza(pizza,st),quantita,data,st);
            System.out.println("test addPrenotazione(>0) = "+outI);
            
            try{
                //test addPrenotazione con utente e pizza esistenti
                outI = -1;
                if(DBManager.getIdPrenotazione(DBManager.getIdUser(user,st), DBManager.getIdPizza(pizza,st), data,st) < 0)
                    outI=DBManager.addPrenotazione(DBManager.getIdUser(user,st),DBManager.getIdPizza(pizza,st),quantita,data,st);
                System.out.println("test addPrenotazione con nome esistente(-1) = "+outI);
            }catch(SQLException e){System.out.println("ERRORE: Prenotazione doppia!!!!!!!");}
            
            //test addPrenotazione con utente esistente
            outI=DBManager.addPrenotazione(DBManager.getIdUser(user,st),DBManager.getIdPizza(pizza,st)+1,quantita+1,data+"1",st);
            System.out.println("test addPrenotazione con ingr esistente(>0) = "+outI);
            
            //test addPizza con pizza esistente
            outI=DBManager.addPrenotazione(DBManager.getIdUser(user,st)+1,DBManager.getIdPizza(pizza,st),quantita+2,data+"2",st);
            System.out.println("test addPrenotazione con prezzo esistente(>0) = "+outI);
            
            //test addPrenotazione con utente e pizza esistente
            outI=DBManager.addPrenotazione(DBManager.getIdUser(user,st)+2,DBManager.getIdPizza(pizza,st)+2,quantita,data+"3",st);
            System.out.println("test addPrenotazione con ingr esistente(>0) = "+outI);
            
            //test addPrenotazione con data esistente
            outI=DBManager.addPrenotazione(DBManager.getIdUser(user,st)+1,DBManager.getIdPizza(pizza,st)+2,quantita,data,st);
            System.out.println("test addPrenotazione con ingr esistente(>0) = "+outI);
            
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
            outI=DBManager.getIdUser(user,st);
            System.out.println("test getIdUser(>0) = "+outI);
            
            outI=DBManager.getIdUser(user+"k",st);
            System.out.println("test getIdUser con user inesistente(-1) = "+outI);
            
            ////////////////////////////////////////////////////////////////////
            // UTENTI
            
            //test getUser(int)
            rs = DBManager.getUser(1,st);
            outS = stampaUtente(rs);
            System.out.println("test getIdUser(int) = "+outS);
            
            //test getUser(int) con id inesistente
            try{
                rs = DBManager.getUser(99999999,st);
                outS = stampaUtente(rs);
                System.out.println("test getIdUser(int) con id inesistente = "+outS);
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            //test getUser(String)
            rs = DBManager.getUser(user,st);
            outS = stampaUtente(rs);
            System.out.println("test getUser(String) = "+outS);
            
            //test getUser(String) con id inesistente
            try{
                rs = DBManager.getUser(user+"gnagna",st);
                outS = stampaUtente(rs);
                System.out.println("test getUser(String) con user inesistente = "+outS);
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            ////////////////////////////////////////////////////////////////////
            // ID PIZZE
            
            //test getIdpizza(String)
            outI=DBManager.getIdPizza(pizza,st);
            System.out.println("test getIdPizza(>0) = "+outI);
            
            outI = DBManager.getIdPizza(pizza+"k",st);
            System.out.println("test getIdpizza con pizza inesistente(-1) = "+outI);
            
            ////////////////////////////////////////////////////////////////////
            // PIZZE
            
            //test getPizza(int)
            rs = DBManager.getPizza(1,st);
            outS = stampaPizza(rs);
            System.out.println("test getPizza(int) = "+outS);
            
            //test getPizza(int) con id inesistente
            try{
                rs = DBManager.getPizza(99999999,st);
                outS = stampaPizza(rs);
                System.out.println("test getPizza(int) con id inesistente = "+outS);
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            //test getPizza(String)
            rs = DBManager.getPizza(pizza,st);
            outS = stampaPizza(rs);
            System.out.println("test getPizza(String) = "+outS);
            
            //test getPizza(String) con id inesistente
            try{
                rs = DBManager.getPizza(pizza+"gnagna",st);
                outS = stampaPizza(rs);
                System.out.println("test getPizza(String) con pizza inesistente = "+outS);
            }catch(SQLException e){System.out.println("ERRORE: ID inesistente!!!!!!!");}
            
            ////////////////////////////////////////////////////////////////////
            // ID PRENOTAZIONI
            
            int a,b;
             //test getIdprenotazione(int, int, String)
            a = DBManager.getIdUser(user,st); 
            b = DBManager.getIdPizza(pizza,st);
            outI = DBManager.getIdPrenotazione(a, b, data,st);
            System.out.println("test getIdPrenotazione(>0) = "+outI);
            
            //test getIdprenotazione(int inesistente, int inesistente, String)
            try{
            a = DBManager.getIdUser(user+77,st); 
            b = DBManager.getIdPizza(pizza + 33,st);
            outI = DBManager.getIdPrenotazione(a, b, data,st);
            System.out.println("test getIdPrenotazione(>0) = "+outI);
            }catch(SQLException e){System.out.println("ERRORE: user e/o pizza inesistente!!!!!!!");}
            
            ////////////////////////////////////////////////////////////////////
            // PRENOTAZIONI
            
            //test getPrenotazione(int)
            rs = DBManager.getPrenotazione(1,st);
            outS = stampaPrenotazione(rs);
            System.out.println("test getPrenotazione(int) = "+outS);
            
            //test getPrenotazione(int) con id inesistente
            try{
                rs = DBManager.getPrenotazione(99999999,st);
                outS = stampaPrenotazione(rs);
                System.out.println("test getPrenotazione(int) con id inesistente = "+outS);
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
            DBManager.modUser(user,pwd+"mod"+user,ruolo+"mod1",st);
            System.out.println("OK");
            
            //test modUser con user inesistente
            DBManager.modUser(user+"papapa",pwd+"mod"+user,ruolo+"mod2",st);
            System.out.println("KO utente");
            
            ////////////////////////////////////////////////////////////////////
            // PIZZE
            
            //test modPizza
            DBManager.modPizza(pizza,ingr+ "pippa", prezzo+21,st);
            System.out.println("OK");
            
            //test modUser con user inesistente
            DBManager.modPizza(pizza+"ciccio",ingr+ "pippa", prezzo+21,st);
            System.out.println("KO pizza");
            
            ////////////////////////////////////////////////////////////////////
            // PRENOTAZIONI
            
            //test modPrenotazione
            DBManager.modPrenotazione(1, quantita+9, data+"1",st);
            System.out.println("OK");
            
            //test modPrenotazione con user inesistente
            DBManager.modPrenotazione(999999, quantita+9999, data+"1",st);
            System.out.println("KO prenotazione");
        }
        DBManager.drop(st);
    }
 
    
    /**
     * Esegue i test di add, mod e rem su Database
     * 
     * @param db
     * 
     * @throws SQLException 
     */
    
    private static void testModel() throws SQLException {
        
        ////////////////////////////////////////////////////////////////////////
        // START ADD
        
        Connection conn = DBManager.openConnection();
        Statement st = DBManager.openStatement(conn);
        DBManager.creaTabelle(st);
        DBManager.inizializza(st);
        DBManager.closeStatement(st);
        DBManager.closeConnection(conn);
        
        Model.addUtente(new Utente("username", "password", "user"));
        Model.addUtente(new Utente("username", "password", "user"));
        Utente ut = Model.getUtente("username");
        System.out.println(ut);
        
        //ut = Model.getUser("puppa");
        //System.out.println(ut);
        
        Model.addPizza( new Pizza("margherita", "mozzarella, basilico, origano", 12.5d) );
        Model.addPizza( new Pizza("margherita", "mozzarella, basilico, origano", 12.5d) );
        Pizza pz = Model.getPizza("margherita");
        System.out.println(pz);
        
        //pz = Model.getPizza("napoli");
        //System.out.println(pz);
        
        Model.addPrenotazione( new Prenotazione(ut.getId(),pz.getId(), 5 , "oggi") );
        Model.addPrenotazione( new Prenotazione(ut.getId(),pz.getId(), 5 , "oggi") );
        Prenotazione pr = Model.getPrenotazione(Model.getIdPrenotazione(ut.getId(), pz.getId(), "oggi"));
        System.out.println(pr);
        
        //pr = Model.getPrenotazione(5);
        //System.out.println(pr);
        
        ////////////////////////////////////////////////////////////////////////
        // START MOD
        
        pr.setQuantita(90);
        pz.setIngredienti("caccadura");
        ut.setPwd("gnagna");
        
        Model.modPrenotazione(pr);
        Model.modPizza(pz);
        Model.modUtente(ut);
        
        System.out.println("\n\n");
        System.out.println(ut);
        System.out.println(pz);
        System.out.println(pr);
        
        ////////////////////////////////////////////////////////////////////////
        // START REM
        
        Model.remPrenotazione(pr);
        Model.remPizza(pz);
        Model.remUtente(ut);
        
        System.out.println("\n\n");
        System.out.println(ut);
        System.out.println(pz);
        System.out.println(pr);
    }
    
    /**
     * Elimina le tabelle dal database in questione
     * 
     * @throws java.sql.SQLException
     */
    
    public static void dropDB() throws SQLException{
        Model.drop();
    }
    
    /**
     * Testa l'interazione tra le jsp e i controller
     * 
     * @param req 
     */
    
    public static void testController(HttpServletRequest req){
        String username="username";
        String password="password";
        HttpSession s = req.getSession();
        Controller.logout(req);//errore non sei loggato
        Controller.login(req);//errore mancano parametri
        s.setAttribute("username", username);
        s.setAttribute("password", password);
        Controller.login(req);//ok
        Controller.login(req);//errore sei gia loggato
        Controller.register(req);//errore sei gia loggato
        Controller.logout(req);//ok
        Controller.register(req);//ok
        Controller.login(req);//ok
        
    
    }


////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

}

