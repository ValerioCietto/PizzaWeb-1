package mypackage;

import java.util.ArrayList;
import javax.servlet.http.*;

public class Model {
    public class Pizza{
        String nome;
        String ingredienti;
        double prezzo;
        public Pizza(String iNome,String iIngredienti,double iPrezzo){
            nome=iNome;
            ingredienti=iIngredienti;
            prezzo=iPrezzo;
        }
        public double getPrezzo(){
            return prezzo;
        }
        public void setPizza(String nIngredienti,double nPrezzo){
            String oNome=this.nome;
            DBManager.updatePizza(oNome, nIngredienti, nPrezzo);
        
        }
    }
    public class Utente{
        String nome;
        String pwd;
        String ruolo;
        public Utente(String iNome,String iPwd, String iRuolo){
            nome=iNome;
            pwd=iPwd;
            ruolo=iRuolo;
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
    public class Ordine extends Pizza{
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
    public class Prenotazione{
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
    public static String pagina(HttpServletRequest req){
        String out="";
        out+="<header><h1>PIZZERIA ONLINE</h1></header>";
        out+="<nav>"+nav(req)+"</nav>";
        out+="<article>"+article(req)+"</article>";
        out+="<aside>"+aside(req)+"</aside>";
        out+="<footer>footer</footer>";
        return out;
    }
    public static String article(HttpServletRequest req){
        String view=req.getParameter("view");
        if(view!=null && !view.equals(""))
            (req.getSession()).setAttribute("view", view);
        view=(String)(req.getSession()).getAttribute("view");
        if(view==null || view.equals(""))
            return "home";
        if(view.equals("catalogo"))
            return catalogo(req);
        if(view.equals("modPermessi"))
            return listaLogin(req);
        else //non ancora pronto
            return "non pronto";
        
            //return articleGenerico();
    }
       
    public static String catalogo(HttpServletRequest req){
        String ruolo=(String)(req.getSession()).getAttribute("ruolo");
        String out="";
        ArrayList<String[]> ris=DBManager.query("SELECT * FROM PIZZE");
        out+="<table>";
        //intestazione
        out+="<tr>";
        for(int j=0;j<ris.get(0).length;j++)
            out+="<th>"+ris.get(0)[j]+"</th>";
            if(ruolo!=null && ruolo.equals("admin"))
                out+="<th>modifica</th><th>cancella</th>";
            if(ruolo!=null && (ruolo.equals("admin")||ruolo.equals("user")))
                out+="<th>add</th><th>ordinate</th>";
        out+="</tr>"; 
        //dati
        for(int i=1;i<ris.size();i++){
            out+="<tr>";
            for(int j=0;j<ris.get(i).length;j++)
                out+="<td>"+ris.get(i)[j]+"</td>";
            if(ruolo!=null && ruolo.equals("admin"))
                    //mod
                out +="<td>mod</td>"
                    //remove
                    + "<td><form action=\"/PizzaWeb/Servlet\" method=\"post\">"
                    + "<input type=\"hidden\" name= \"pizza\"  value=\""+ris.get(i)[0]+"\">"
                    + "<input type=\"submit\" name= \"action\" value= \"remove\">"
                    + "</form></td>";
            if(ruolo!=null && (ruolo.equals("admin")||ruolo.equals("user")))
                out+="<td>add</td><td>att</td>";
            out+="</tr>";
        }
        if(ruolo!=null && ruolo.equals("admin")){
            out+="<form action=\"/PizzaWeb/Servlet\" method=\"post\"><tr>"
               + "<td><input type=\"text\" name= \"pizza\"  value=\"nome\"></td>"
               + "<td><input type=\"text\" name= \"ingredienti\"  value=\"ingredienti\"></td>"
               + "<td><input type=\"text\" name= \"prezzo\"  value=\"prezzo\"></td>"
               + "<td colspan=4><input type=\"submit\" name= \"action\"  value=\"aggiungi\"></td>"
               + "</tr></form>";
        }
        out+="</table>";
        return out;
    }
    public static String listaLogin(HttpServletRequest req){
        String out="";
        ArrayList<String[]> ris=DBManager.query("SELECT * FROM UTENTI");
        out+="<table>";
        
        for(int i=0;i<ris.size();i++){
            out+="<tr>";
            if(i==0)
                for(int j=0;j<ris.get(i).length;j++)
                    out+="<th>"+ris.get(i)[j]+"</th>";
            else
                for(int j=0;j<ris.get(i).length;j++)
                    out+="<td>"+ris.get(i)[j]+"</td>";
            out+="</tr>";
        }
        out+="</table>";
        return out;
    }
    
    public static String nav(HttpServletRequest req){
        String out="<ul>";
        String ruolo=(String)(req.getSession()).getAttribute("ruolo");
        out     +="<li><a href=\"catalogo.jsp\">Catalogo</a></li>";
        if(ruolo!=null && ruolo.equals("user"))
            out +="<li>Prenota</li>"
                + "<li>Visualizza Tue Prenotazioni</li>";
        if(ruolo!=null && ruolo.equals("admin"))
            out +="<li>Visualizza Tutte le Prenotazioni</li>"
                + "<li><a href=\"loginmanger.jsp\">Modifica Permessi</a></li>";
        return out+"</ul>";
    }
    
    public static String aside(HttpServletRequest req){
        String out="";
        String ruolo=(String)(req.getSession()).getAttribute("ruolo");
        String message=(String)(req.getSession()).getAttribute("message");
        String action=req.getParameter("action");
        out+="<table>";
        if(ruolo==null || ruolo.equals(""))
            out +="<form action=\"/PizzaWeb/Servlet\" method=\"post\">"
                + "<tr><td>"
                + "<p>Login</p> <input type =\"text\" name=\"login\">"
                //+ "</td></tr>"
                //+ "<tr><td>"
                + "<p>Password</p> <input type =\"password\" name=\"password\">"
                + "<input type= \"submit\" name= \"action\" value= \"login\">"
                + "</td></tr>"
                + "</form>"
                + "<tr><td>"
                + "<p>registrati</p>"
                + "</td></tr>";
        else
            out +="<tr><td><form action=\"/PizzaWeb/Servlet\" method=\"post\">"
                + "<p><input type=\"submit\" name= \"action\" value= \"logout\"></p>"
                + "</form></td></tr>";
        
        if(message!=null && !message.equals("")){
            out+="<tr><td><p>"+message+"</p></td></tr>";
            (req.getSession()).setAttribute("message","");
        }
        out+="</table>";
        return out;
    }
    
}
