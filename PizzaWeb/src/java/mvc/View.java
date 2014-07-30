package mvc;

////////////////////////////////////////////////////////////////////////////////

import components.*;
import java.util.ArrayList;
import javax.servlet.http.*;


public class View {
    
    /**
     * Ritorna una stringa che fornisce i dati per costruire l'article
     * 
     * @param al
     * @param req
     * 
     * @return 
     */
    
    public static String visualizzaCatalogo(ArrayList<Pizza>al, HttpServletRequest req){
        
        String html = "";
        
        if(al==null)
            return visualizzaFallimento(req);
        else{
            for(int i=0;i<al.size();i++){
                html += "<div class='pizza'>";
                html += "<span class='nome'>"+al.get(i).getNome()+"</span>";
                html += "<span class='ingredienti'>"+al.get(i).getIngredinti()+"</span>";
                html += "<span class='prezzo'>"+al.get(i).getPrezzo()+"</span>";
                html += "</div>";
            }
        }
        Controller.notifica(req.getSession(),"user2:"+ req.getSession().getAttribute("username"));
        Controller.notifica(req.getSession(),"password2:"+ req.getSession().getAttribute("password"));
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
    
    public static String visualizzaPrenotazioni(ArrayList<Prenotazione>al,HttpServletRequest req){
        String html="";
        if(al==null)
            return visualizzaFallimento(req);
        else{
            for(int i=0;i<al.size();i++){
                html += "<div class='prenotazione'>";
                html += "<span class='nome'>"+al.get(i).toString()+"</span>";
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
        else{
            for(int i=0;i<al.size();i++){
                html += "<div class='utente'>";
                html += "<span class='username'>"+al.get(i).getUsername()+"</span>";
                html += "<span class='password'>"+al.get(i).getPassword()+"</span>";
                html += "<span class='permission'>"+al.get(i).getPermission()+"</span>";
                html += "</div>";
            }
        }
        Controller.notifica(req.getSession(),"user2:"+ req.getSession().getAttribute("username"));
        Controller.notifica(req.getSession(),"password2:"+ req.getSession().getAttribute("password"));
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
        String html = "Benvenuto " + req.getSession().getAttribute("username");
        return html;
    }


///////////////////////////////////////////////////////////////////////////////////////////    
}
