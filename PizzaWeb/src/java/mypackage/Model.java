package mypackage;

import java.util.ArrayList;
import javax.servlet.http.*;
import java.lang.*;

public class Model {
    public static void login(HttpServletRequest req){
        HttpSession s=req.getSession();
        String name = req.getParameter("login");
        String password = req.getParameter("password");
        if(name!=null && password!=null && !name.equals("") && !password.equals("")){
            Utente login = new Utente( DBManager.getLogin(name,password) );
            if(!login.getNome().equals("")){
                s.setAttribute("username",login.getNome());
                s.setAttribute("ruolo", login.getRuolo());
                s.setAttribute("message","login effettuato, benvenuto!");
            }else
                s.setAttribute("message","login errato, sicuro di esserti registrato?");
        }else
            s.setAttribute("message","inserisci il tuo nome utente e la tua password.");
    }
    public static void logout(HttpServletRequest req){
        HttpSession s=req.getSession();
        s.invalidate();
        s=req.getSession();
        s.setAttribute("message", "logout effettuato");
    }
    public static void remPizza(HttpServletRequest req){
        String pizza= req.getParameter("pizza");
        if(!DBManager.remPizza(pizza))
            (req.getSession()).setAttribute("message","Problema sql");
    }
    public static void addPizza(HttpServletRequest req){
        HttpSession s=req.getSession();
        Pizza temp=new Pizza(req.getParameter("pizza"),req.getParameter("ingredienti"),req.getParameter("prezzo"));
        if (temp.getNome()!=null){
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
        String n=req.getParameter("login");
        String p=req.getParameter("password");
        String ruolo= "user";
        DBManager.addLogin(n,p, ruolo); //To change body of generated methods, choose Tools | Templates.
    }
    static void addPren(HttpServletRequest req) {
        
        //ALLORA, QUANDO SI CLICCA IL PRENOTA VIENE INVIATO SOLO LA PIZZA CORRISPONDENTE QUINDI NON BISOGNA USARE ARRAY PORCA TROTA!!!!!!
       
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
    String nome;
    String ingredienti;
    double prezzo;
    public Pizza(String iNome,String iIngredienti,double iPrezzo){
        if(iNome!=null && !iNome.equals("")){
            if(iIngredienti!=null){
                if (iPrezzo>0){
                    ingredienti=iIngredienti;
                    nome=iNome;
                    prezzo=iPrezzo;
                }}}
    }
    public Pizza(String iNome,String iIngredienti,String iPrezzo){
    try{
            double tempPrezzo= Double.parseDouble(iPrezzo);
            if(iNome!=null && !iNome.equals("")){
            if(iIngredienti!=null){
                if (tempPrezzo>0){
                    ingredienti=iIngredienti;
                    nome=iNome;
                    prezzo=tempPrezzo;
                }}}
                  
       }catch(NumberFormatException e){
                    System.out.println(e);
                }
    }
    public double getPrezzo(){
        return prezzo;
    }
    public String getNome(){
    return nome;
    }
    public void setPizza(String nIngredienti,double nPrezzo){
        String oNome=this.nome;
        DBManager.modPizza(oNome, nIngredienti, nPrezzo);

    }
}
class Utente{
    String nome="";
    String pwd="";
    String ruolo="";
    public Utente(String iNome,String iPwd, String iRuolo){
        nome=iNome;
        pwd=iPwd;
        ruolo=iRuolo;
    }
    public Utente(String []input){
        if(input==null)
            return;
        nome=input[0];
        pwd=input[1];
        ruolo=input[2];
    }
    public String getNome(){
        return nome;
    }
    public String getPwd(){
        return pwd;
    }
    public String getRuolo(){
        return ruolo;
    }
}
class Ordine extends Pizza{
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
}

