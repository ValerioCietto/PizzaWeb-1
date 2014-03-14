package mypackage;

import java.util.ArrayList;
import javax.servlet.http.*;

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
                s.setAttribute("message","login effettuato");
            }else
                s.setAttribute("message","login errato");
        }else
            s.setAttribute("message","input vuoto");
    }
    public static void logout(HttpServletRequest req){
        HttpSession s=req.getSession();
        s.invalidate();
        s=req.getSession();
        s.setAttribute("message", "logout effettuato");
    }
    public static void remPizza(HttpServletRequest req){
        String pizza= req.getParameter("pizza");
        DBManager.remPizza(pizza);
    }
    public static void addPizza(HttpServletRequest req){
        HttpSession s=req.getSession();
        Pizza temp=new Pizza(req.getParameter("pizza"),req.getParameter("ingredienti"),req.getParameter("prezzo"));
        if (temp.getNome()!=null){
            String n= temp.getNome();
            String i= req.getParameter("ingredienti");
            Double p= temp.getPrezzo();
            DBManager.addPizza(n, i, p);
            s.setAttribute("message","aggiunta pizza "+n);
        }else
            s.setAttribute("message","parametro pizza non trovato");
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
    int id;
    Utente cliente;
    ArrayList <Ordine> prenotaz;
    String stato;
    public Prenotazione(int iId, Utente iCliente, ArrayList <Ordine> iPrenotaz, String iStato){
        id=iId;
        cliente=iCliente;
        for(int i=0;i<iPrenotaz.size();i++)
            prenotaz.add(iPrenotaz.get(i));
        stato=iStato;
    }
    public int getId(){
        return id;
    }
    public Utente getUtente(){
        return cliente;
    }
    public String getStato(){
        return stato;
    }
    public Ordine getOrdine(int i){
        return prenotaz.get(i);
    }
}

