package mvc;

////////////////////////////////////////////////////////////////////////////////

import components.*;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;


public class View {
    public static void visualizzaCatalogo(ArrayList<Pizza>al,HttpServletRequest req){
        if(al==null)
            visualizzaFallimento(req);
    }
    public static void visualizzaPrenotazioni(ArrayList<Prenotazione>al,HttpServletRequest req){
        if(al==null)
            visualizzaFallimento(req);
    }
    public static void visualizzaFallimento(HttpServletRequest req){}
}
