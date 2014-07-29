package mvc;

////////////////////////////////////////////////////////////////////////////////

import components.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.*;


public class View {
    public static void visualizzaCatalogo(ArrayList<Pizza>al,HttpServletRequest req,HttpServletResponse response){
        if(al==null)
            visualizzaFallimento(req);
        response.setContentType("text/html;charset=UTF-8");
        
        try {
        PrintWriter out;out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>FirstServlet!!</title></head>");
        out.println("<body>");
        out.println("<h2>Ciao: sono FirstServlet</h2>");
        out.println("<h2>Context path:" + req.getContextPath() + "</h2>");
        out.println("</body>");
        out.println("</html>");
        out.close();
        }catch(IOException e){
        } finally {}
        
    }
    public static void visualizzaPrenotazioni(ArrayList<Prenotazione>al,HttpServletRequest req){
        if(al==null)
            visualizzaFallimento(req);
    }
    public static void visualizzaFallimento(HttpServletRequest req){}
}
