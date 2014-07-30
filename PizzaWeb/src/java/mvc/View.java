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
     * @param u
     * @param req
     * 
     * @return 
     */
    
    public static String visualizzaCatalogo(ArrayList<Pizza>al, Utente u, HttpServletRequest req){
        
        String html = "";
        
        if(al==null)
            return visualizzaFallimento(req);
        else{
            if(u == null){
                for(int i=0;i<al.size();i++){
                html += "<div class='pizza'>";
                html += "<span class='nome'>"+al.get(i).getNome()+"</span>";
                html += "<span class='ingredienti'>"+al.get(i).getIngredinti()+"</span>";
                html += "<span class='prezzo'>"+al.get(i).getPrezzo()+"</span>";
                html += "</div>";
                }
            }
            else if(u.getPermission().equals("user")){
                
                for(int i=0;i<al.size();i++){
                html += "<div class='pizza'>";
                html += "<span class='nome'>"+"</br>"+al.get(i).getNome()+"</br>"+"</span>";
                html += "<span></br>Ingredienti:  </span>";
                html += "<span class='ingredienti'>"+al.get(i).getIngredinti()+"</span>";
                html += "<span></br>Prezzo:  </span>";
                html += "<span class='prezzo'>"+al.get(i).getPrezzo()+"</span>";
                
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                    html += "<span>Quantità:  </span>";
                    html += "<input type ='number' name='quantita' min='1' max='100' required>";
                    html += "<span>  Data:  </span>";
                    html += "<input type ='date' name='data' required>";
                    html += "<input type='hidden' name='pizza' value='"+al.get(i).getNome()+"'>";
                    html += "<input type='hidden' name='action' value='addPrenotazione'>";
                    html += "<input type='submit' value='Prenota'>";
                html += "</form>"; 
                html += "</div></br>";
                }
            }
            else if(u.getPermission().equals("admin")){
                
                html += "<div>";
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
                html += "</div></br></br>"; 
                
                for(int i=0;i<al.size();i++){
                html += "<div class='pizza'>";
                html += "<span class='nome'>"+"</br>"+al.get(i).getNome()+"</br>"+"</span>";
                html += "<span></br>Ingredienti:  </span>";
                html += "<span class='ingredienti'>"+al.get(i).getIngredinti()+"</span>";
                html += "<input type ='text' name='ingredienti'>";
                html += "<span></br>Prezzo:  </span>";
                html += "<span class='prezzo'>"+al.get(i).getPrezzo()+"</span>";
                html += "<input type ='double' name='prezzo'></br>";
                
                html += "<span>";
                    html += "<form action='/PizzaWeb/Servlet' method='post' >";
                        html += "<input type='hidden' name='pizza' value='"+al.get(i).getNome()+"'>";
                        html += "<input type='hidden' name='action' value='modPizza'>";
                        html += "<input type='submit' value='Modifica'>";
                    html += "</form>"; 

                    html += "<form action='/PizzaWeb/Servlet' method='post' >";
                        html += "<input type='hidden' name='pizza' value='"+al.get(i).getNome()+"'>";
                        html += "<input type='hidden' name='action' value='remPizza'>";
                        html += "<input type='submit' value='Rimuovi'>";
                    html += "</form>"; 
                html += "</span>";
                
                html += "</div></br>";
                }
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
