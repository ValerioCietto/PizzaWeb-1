package mvc;

////////////////////////////////////////////////////////////////////////////////

import components.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.*;


public class View {
    
    public static String visualizzaCatalogo(ArrayList<Pizza>al,HttpServletRequest req){
        req.getSession().setAttribute("View", "catalogo");
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
    public static String visualizzaPrenotazioni(ArrayList<Prenotazione>al,HttpServletRequest req){
        req.getSession().setAttribute("View", "prenotazioni");
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
    
    public static String visualizzaFallimento(HttpServletRequest req){
        String html = "";
        html += "Errore" + req.getAttribute("message");
        return html;
    }
}
