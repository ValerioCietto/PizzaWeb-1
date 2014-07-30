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
        
        req.getSession().setAttribute("view", "catalogo");
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
        req.getSession().setAttribute("view", "prenotazioni");
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
        String html = "Benvenuto " + req.getParameter("username");
        return html;
    }
    
    /**
     * Visualizza pagina Registrazione
     * 
     * @param req
     */
    
    /*public static String paginaRegistrazione(HttpServletRequest req){
        return "restriamoci";
    }*/


///////////////////////////////////////////////////////////////////////////////////////////    
}
