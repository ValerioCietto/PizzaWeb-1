package mypackage;

import java.util.ArrayList;
import javax.servlet.http.*;
import java.lang.*;

public class Model {
    /**
     * Estrapola dalla request gli input password e utente
     * Controlla che non siano nulli
     * Crea un'oggetto utente(con parametri vuoti se usr o pwd errati)
     * Controlla la riuscita del punto precedente
     * Carica i parametri nella sessione
     * //////////
     * -da aggiungere il controllo del se è gia connesso
     * -forse passare oggetto invece che stringhe
     * @param req 
     */
    public static void login(HttpServletRequest req){
        HttpSession s=req.getSession();
        String name = req.getParameter("login");
        String password = req.getParameter("password");
        if(name!=null && password!=null && !name.equals("") && !password.equals("")){
            Utente login = new Utente( DBManager.getLogin(name,password) );
            if(login.getNome()!=null && !login.getNome().equals("")){
                s.setAttribute("username",login.getNome());
                s.setAttribute("ruolo", login.getRuolo());
                s.setAttribute("message","login effettuato, benvenuto "+login.getNome()+"!");
            }else
                s.setAttribute("message","login errato, sicuro di esserti registrato?");
        }else
            s.setAttribute("message","inserisci il tuo nome utente e la tua password.");
    }
    /**
     * Invalida i dati di sessione
     * Genera una nuova sessione
     * @param req 
     */
    public static void logout(HttpServletRequest req){
        HttpSession s=req.getSession();
        s.invalidate();
        s=req.getSession();
        s.setAttribute("message", "logout effettuato");
    }
    /**
     * Estrapola il nome della pizza da eliminare
     * Richiama il DB per rimuovare la pizza
     * ---------------------
     * controllare che pizza non sia nullo
     * @param req 
     */
    public static void remPizza(HttpServletRequest req){
        String pizza= req.getParameter("pizza");
        if(!DBManager.remPizza(pizza))
            (req.getSession()).setAttribute("message","Problema sql");
    }
    /**
     * Estrapola il nome, ingredienti e prezzo della pizza da aggiungere
     * Crea un'oggetto pizza con i parametri sopra(nel caso non riesca i valori sono vuoti)
     * Controlla la riuscita della creazione della pizza
     * Carica la pizza nel DB
     * @param req 
     */
    public static void addPizza(HttpServletRequest req){
        HttpSession s=req.getSession();
        Pizza temp=new Pizza(req.getParameter("pizza"),req.getParameter("ingredienti"),req.getParameter("prezzo"));
        if (temp.getNome()!=null && !temp.getNome().equals("")){
            String n= temp.getNome();
            String i= req.getParameter("ingredienti");
            Double p= temp.getPrezzo();
            if(DBManager.addPizza(n, i, p))
               s.setAttribute("message","aggiunta pizza "+n);
            else
               s.setAttribute("message","Problema sql");
        }else
            s.setAttribute("message","inserisci un nome, gli ingredienti e il prezzo.");
    } 

    static void modPizza(HttpServletRequest req) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    static void addUser(HttpServletRequest req) {
        String n=req.getParameter("username");
        String p=req.getParameter("pwd1");
        String ruolo= "user";
        DBManager.addLogin(n,p, ruolo); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * Estrapola il nome della pizza e la quantità, il nome del cliente e l'ora di prenotazione
     * Carica un solo tipo di pizza nel database
     * 
     * ///////////////
     * Manca creazione oggetto Prenotazione
     * Manca controllo dei dati
     * 
     * NOTA ANNA: ALLORA, QUANDO SI CLICCA IL PRENOTA VIENE INVIATO SOLO LA PIZZA CORRISPONDENTE QUINDI NON BISOGNA USARE ARRAY PORCA TROTA!!!!!!
     * @param req 
     */
    static void addPren(HttpServletRequest req) {
        String p=req.getParameter("pizza");
        String clName=(String)req.getSession().getAttribute("username");
        int q= (Integer.parseInt(req.getParameter("quant"))); //questo gli riempie solo un numero
        String d= req.getParameter("data");
        
        DBManager.addPrenotazioneanna(clName,d, p, q);
    }
    
    static void remPren(HttpServletRequest req){
        
        //in teoria name non dovrebbe mai essere nullo se lo facciamo vedere dalla pagina prenot, accessibile solo dall'utente registrato
        
        String name=req.getParameter("nomecliente");
        String pizza= req.getParameter("nomepizza");
        String data= req.getParameter("data");

        
        if (name!=null)
            DBManager.remPrenotazione(name, data, pizza);
        
        else System.out.println("specificare parametri");
    }
        
    
}
class Pizza{
    String nome="";
    String ingredienti="";
    double prezzo=0;
    /**
     * Controlla che gli input non siano nulli o non accettabili
     * In caso di non riuscita i parametri sono nulli
     * Altrimenti carica i dati nell'oggetto
     * 
     * @param iNome
     * @param iIngredienti
     * @param iPrezzo 
     */
    public Pizza(String iNome,String iIngredienti,double iPrezzo){
        if(iNome!=null && !iNome.equals("")){
            if(iIngredienti!=null && !iIngredienti.equals("")){
                if (iPrezzo>0){
                    ingredienti=iIngredienti;
                    nome=iNome;
                    prezzo=iPrezzo;
                }}}
    }
    /**
     * Parsifica la stringa del testo
     * Controlla che gli input non siano nulli o non accettabili
     * In caso di non riuscita i parametri sono nulli
     * Altrimenti carica i dati nell'oggetto
     * 
     * @param iNome
     * @param iIngredienti
     * @param iPrezzo 
     */
    public Pizza(String iNome,String iIngredienti,String iPrezzo){
        try{
            double tempPrezzo= Double.parseDouble(iPrezzo);
            if(iNome!=null && !iNome.equals("")){
                if(iIngredienti!=null && !iIngredienti.equals("")){
                    if (tempPrezzo>0){
                        ingredienti=iIngredienti;
                        nome=iNome;
                        prezzo=tempPrezzo;
                    }}}
                  
        }catch(NumberFormatException e){
            System.out.println(e);
        }
    }
    /**Restituisce il prezzo della pizza
     * @return <double> prezzo
     */
    public double getPrezzo(){
        return prezzo;
    }
    /**Restituisce il nome della pizza
     * @return <String> nome pizza
     */
    public String getNome(){
    return nome;
    }
    /**
     * Controlla che la pizza su cui stai lavorando esista
     * Controlla che i dati di input siano accettabili
     * Aggiorna i dati della pizza nel DB
     * @param nIngredienti
     * @param nPrezzo 
     */
    public void setPizza(String nIngredienti,double nPrezzo){
        String oNome=this.nome;
        if(oNome!=null && !oNome.equals(""))
            if(nIngredienti!=null && !nIngredienti.equals("") && nPrezzo>0)
                DBManager.modPizza(oNome, nIngredienti, nPrezzo);
    }
   
}
class Utente{
    String nome="";
    String pwd="";
    String ruolo="";
    /**
     * NON USATO
     * @param iNome
     * @param iPwd
     * @param iRuolo 
     */
    /*public Utente(String iNome,String iPwd, String iRuolo){
        nome=iNome;
        pwd=iPwd;
        ruolo=iRuolo;
    }*/
    /**
     * Crea l'utente
     * @param input array con nome,pwd e ruolo 
     */
    public Utente(String []input){
        if(input==null)
            return;
        nome=input[0];
        pwd=input[1];
        ruolo=input[2];
    }
    /**
     * Restituisce il nome dell'utente
     * @return <String> nome Pizza
     */
    public String getNome(){
        return nome;
    }
    /**
     * Restituisce la password dell'utente
     * @return <String>
     */
    public String getPwd(){
        return pwd;
    }
    /**
     * Restituisce il ruolo dell'utente
     * @return <String>
     */
    public String getRuolo(){
        return ruolo;
    }
}
/**
 * da finire e adeguare
 * @author mirko
 */
class Prenotazione{
    String cliente="";
    String data="";
    String pizza="";
    int quantita=0;
    String stato="";
    public Prenotazione(String iCliente,String iData, String iPizza, int iQuantita){
        if(iCliente!=null && !iCliente.equals("") && iData!=null && !iData.equals("") && iPizza!=null && !iPizza.equals("") && iQuantita>0){
            cliente=iCliente;
            data=iData;
            pizza=iPizza;
            quantita=iQuantita;
            stato="Ordinata";
        }
    }
}
/*class Ordine extends Pizza{
    int quantita=1;   
    public Ordine(String iNome,String iIngredienti, double iPrezzo){
        super(iNome, iIngredienti, iPrezzo);
    }
    public void setQuantita(int iQuantita){
        quantita=iQuantita;
    }
    public int getQuantita(){
        return quantita;
    }
}
class Prenotazione{
    Utente cliente;
    String data;
    Ordine [] prenotaz;
    String stato;
    public Prenotazione(Utente iCliente,String iData, Ordine[] iPrenotaz, String iStato){
        data=iData;
        cliente=iCliente;
        for(int i=0;i<iPrenotaz.length;i++)
            prenotaz[i]=iPrenotaz[i];
        stato=iStato;
    }
    public Utente getUtente(){
        return cliente;
    }
    public String getStato(){
        return stato;
    }
    public Ordine getOrdine(int i){
        return prenotaz[i];
    }
}*/
