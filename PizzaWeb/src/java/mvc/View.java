package mvc;

/**
 * Classe dell'oggetto Pizza
 * @author Alessandro Genovese, Anna Di Leva, Mirko Costantino;
 */

////////////////////////////////////////////////////////////////////////////////

import components.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.*;


public class View {
    
    /**
     * Ritorna una stringa che fornisce i dati per costruire l'article
     * 
     * @param al
     * @param u
     * @param req
     * 
     * @return 
     */
    
    public static String visualizzaCatalogo(ArrayList<Pizza>al, Utente u, HttpServletRequest req){
        String html = "";
        if(al==null)
            return visualizzaFallimento(req);
        else if(al.size()==0)
            html +="<div>Non ci sono Pizze in catalogo</div>";
        else{
            if(u == null){
                for (Pizza al1 : al) {
                    html += "<div class='pizza'>";
                    html += "<span class='nome'>" + al1.getNome()  + "</span>";
                    html += "<span></br>Ingredienti:  </span>";
                    html += "<span class='ingredienti'>" + al1.getIngredinti() + "</span>";
                    html += "<span></br>Prezzo:  </span>";
                    html += "<span class='prezzo'>" + al1.getPrezzo() + "</span>";
                }
            }
            else if(u.getPermission().equals("user")){
                
                for (Pizza al1 : al) {
                    html += "<div class='pizza'>";
                    html += "<span class='nome'>" + al1.getNome()  + "</span>";
                    html += "<span></br>Ingredienti:  </span>";
                    html += "<span class='ingredienti'>" + al1.getIngredinti() + "</span>";
                    html += "<span></br>Prezzo:  </span>";
                    html += "<span class='prezzo'>" + al1.getPrezzo() + "</span>";
                    html += "<form action='/PizzaWeb/Servlet' method='post' >";
                    html += "<span>Quantità:  </span>";
                    html += "<input type ='number' name='quantita' min='0' max='100'value = '0' required>";
                    html += "<span>  Data:  </span>";
                    html += "<input type ='date' name='data' required>";
                    html += "<input type='hidden' name='pizza' value='" + al1.getNome() + "'>";
                    html += "<input type='hidden' name='action' value='addPrenotazione'>";
                    html += "<input type='submit' value='Prenota'>";
                    html += "</form>";
                    html += "</div>";
                }
            }
            else if(u.getPermission().equals("admin")){
                
                html += "<div class= 'new'>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                    html += "<span class='nome'>Nome:  </span>";
                    html += "<input type ='text' name='nome'>";
                    html += "<span class='ingredienti'> Ingredienti: </span>";
                    html += "<input type ='text' name='ingredienti'>";
                    html += "<span class='prezzo'> Prezzo: </span>";
                    html += "<input type='double' name='prezzo'></br>";                    
                    html += "<input type='hidden' name='action' value='addPizza'>";
                    html += "<input type='submit' value='Aggiungi'>";
                    html += "</form>";
                html += "</div>"; 
                
                for (Pizza al1 : al) {
                    html += "<div class='pizza'>";
                    html += "<span class='nome'>" + al1.getNome()  + "</span>";
                    html += "<span></br>Ingredienti:  </span>";
                    html += "<span class='ingredienti'>" + al1.getIngredinti() + "</span>";
                    html += "<span></br>Prezzo:  </span>";
                    html += "<span class='prezzo'>" + al1.getPrezzo() + "</span>";
                    html += "<span>";
                    html += "<form action='/PizzaWeb/Servlet' method='post' >";
                    html += "<span>Ingredienti:  </span>";
                    html += "<input type ='text' name='ingredienti'>";
                    html += "<span></br>Prezzo:  </span>";
                    html += "<input type ='text' name='prezzo'></br>";
                    html += "<input type='hidden' name='pizza' value='" + al1.getNome() + "'>";
                    html += "<input type='hidden' name='action' value='modPizza'>";
                    html += "<input type='submit' value='Modifica'>";
                    html += "</form>";
                    html += "<form action='/PizzaWeb/Servlet' method='post' >";
                    html += "<input type='hidden' name='pizza' value='" + al1.getNome() + "'>";
                    html += "<input type='hidden' name='action' value='remPizza'>";
                    html += "<input type='submit' value='Rimuovi'>";
                    html += "</form>";
                    html += "</span>";
                    html += "</div>";
                }
            }
        }
        return html;
    }
    
    /**
     * Ritorna una stringa che fornisce i dati per costruire l'article
     * 
     * @param al
     * @param u
     * @param req
     * 
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public static String visualizzaPrenotazioni(ArrayList<Prenotazione>al, Utente u, HttpServletRequest req) throws SQLException{
        String html="";
        if(al==null)
            return visualizzaFallimento(req);
        
        else if(al.size()==0)
            html +="<div>Non ci sono Prenotazioni in Programma</div>";
        else if(u.getPermission().equals("user")){
            for (Prenotazione al1 : al) {
                html += "<div class='prenotazione'>";
                html += "<span>Pizza:  </span>";
                html += "<span class='pizza'>" + Model.getPizza(al1.getIdPizza()).getNome() + "</span>";
                html += "<span></br>Quantità:  </span>";
                html += "<span class='quantità'>" + al1.getQuantita() + "</span>";
                html += "<span></br>Data:  </span>";
                html += "<span class='data'>" + al1.getData() + "</span>";
                html += "<span></br>Stato:  </span>";
                html += "<span class='stato'>" + al1.getStato() + "</span>";
                if (!al1.getStato().equals("Consegnato")) {
                    html += "<form action='/PizzaWeb/Servlet' method='post' >";
                    html += "<span>Quantità:  </span>";
                    html += "<input type ='number' name='quantita' min='0' max='100' value = '0'>";
                    html += "<span>  Data:  </span>";
                    html += "<input type ='date' name='data'>";
                    html += "<input type='hidden' name='id' value=" + al1.getIdPrenotazione() + ">";
                    html += "<input type='hidden' name='action' value='modPrenotazione'>";
                    html += "<input type='submit' value='Modifica'>";
                    html += "</form>";
                    html += "<form action='/PizzaWeb/Servlet' method='post' >";
                    html += "<input type='hidden' name='id' value=" + al1.getIdPrenotazione() + ">";
                    html += "<input type='hidden' name='action' value='modStatoPrenotazione'>";
                    html += "<input type='submit' value='pizza consegnata'>";
                    html += "</form>"; 
                }
                html += "</div>"; 
            }
        }else if(u.getPermission().equals("admin")){
            for (Prenotazione al1 : al) {
                html += "<div class='prenotazione'>";
                html += "<span>ID prenotazione:  </span>";
                html += "<span class='id'>" + al1.getIdPrenotazione() + "</span>";
                html += "<span></br>Utente:  </span>";
                html += "<span class='quantità'>" + Model.getUtente(al1.getIdUtente()).getUsername() + "</span>";
                html += "<span></br>Pizza:  </span>";
                html += "<span class='pizza'>" + Model.getPizza(al1.getIdPizza()).getNome() + "</span>";
                html += "<span></br>Quantità:  </span>";
                html += "<span class='quantità'>" + al1.getQuantita() + "</span>";
                html += "<span></br>Data:  </span>";
                html += "<span class='data'>" + al1.getData() + "</span>";
                html += "<span></br>Stato:  </span>";
                html += "<span class='stato'>" + al1.getStato() + "</span>";
                html += "<span>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<span> Utente: </span>";
                html += "<input type ='text' name='utente'>";
                html += "<span'> Pizza: </span>";
                html += "<input type ='text' name='pizza'>";
                html += "<span> Quantità:  </span>";
                html += "<input type ='number' name='quantita' min='0' max='100' value = '0'>";
                html += "<span>  Data:  </span>";
                html += "<input type ='date' name='data'>";
                html += "<input type='hidden' name='id' value=" + al1.getIdPrenotazione() + ">";
                html += "<input type='hidden' name='action' value='modPrenotazione'>";
                html += "<input type='submit' value='Modifica ordine'>";
                html += "</form>";
                html += "<form>";
                html += "<select name = 'stato'>";
                html += "<option value = 'Ordinato'> Ordinato </option>";
                html += "<option value = 'Consegnato' selected> Consegnato </option>";
                html += "</select>";
                html += "<input type='hidden' name='id' value=" + al1.getIdPrenotazione() + ">";
                html += "<input type='hidden' name='action' value='modPrenotazione'>";
                html += "<input type='submit' value='Modifica stato'>";
                html += "</form>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<input type='hidden' name='prenotazione' value=" + al1.getIdPrenotazione() + ">";
                html += "<input type='hidden' name='action' value='remPrenotazione'>";
                html += "<input type='submit' value='Rimuovi'>";
                html += "</form>";
                html += "</span>";
                html += "</div>"; 
            }
        
        }
        return html;
    }
    
    /**
     * Ritorna una stringa che fornisce i dati per costruire l'article
     * 
     * @param al
     * @param req
     * 
     * @return 
     */
    
    public static String visualizzaUtenti(ArrayList<Utente>al, HttpServletRequest req){
        String html = "";
        if(al==null)
            return visualizzaFallimento(req);
        else if(al.size()==0)
            html +="<div>Non ci sono Login ??? e tu chi sei?</div>";
        else{
            for (Utente al1 : al) {
                html += "<div class='utente'>";
                html += "<span class='username  '>" + al1.getUsername() + "</span>";
                html += "<span class='   password   '>" + al1.getPassword() + "</span>";
                html += "<span class='   permission   '>" + al1.getPermission() + "</span>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<span>username:  </span>";
                html += "<input type ='text' name='name'>";
                html += "<span>  password:  </span>";
                html += "<input type ='text' name='password'>";
                html += "<select name = 'permission'>";
                html += "<option value = 'admin'> admin </option>";
                html += "<option value = 'user' selected> user </option>";
                html += "</select>";
                html += "<input type='hidden' name='id' value='" + al1.getUsername() + "'>";
                html += "<input type='hidden' name='action' value='modUtente'>";
                html += "<input type='submit' value='Modifica'>";
                html += "</form>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<input type='hidden' name='id' value=" + al1.getId() + ">";
                html += "<input type='hidden' name='action' value='remUtente'>";
                html += "<input type='submit' value='Rimuovi'>";
                html += "</form>";
                html += "</div>";
            }
        }
        return html;
    }
    
    /**
     * Genera un messaggio di errore
     * 
     * @param req
     * 
     * @return 
     */
    
    public static String visualizzaFallimento(HttpServletRequest req){
        String html = "";
        html += "Errore" + req.getAttribute("message");
        return html;
    }
    
    /**
     * Esegue il refresh della pagina
     * 
     * @param req
     * 
     * @return 
     */
    
    public static String login(HttpServletRequest req){
        req.getSession().setAttribute("view", "login");
        String html = "<h1>Benvenuto " + req.getSession().getAttribute("username")+ "</h1>";
        return html;
    }


///////////////////////////////////////////////////////////////////////////////////////////    
}
