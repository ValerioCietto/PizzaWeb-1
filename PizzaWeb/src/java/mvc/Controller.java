package mvc;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import mvc.model.Database;

@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})

public class Controller extends HttpServlet {
    
    Database db;
    public Controller() {
        super();
        db = new Database();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        
        String action= request.getParameter("action");
        
        if(action!=null){
            switch (action) {
                case "switch":
                    switchPage(request);
                    break;
                case "login":
                    Model.login(request);
                    break;
                case "logout":
                    Model.logout(request);
                    break;
                case "addPizza":
                    Model.addPizza(request);
                    aggPage(request);
                    break;
                case "remPizza":
                    Model.remPizza(request);
                    aggPage(request);
                    break;
                case "modPizza":
                    Model.modPizza(request);
                    aggPage(request);
                    break;
                case "registration":
                    Model.addUser(request);
                    break;
                case "addPrenotaz":
                     //Logger.getGlobal().info("sono nel controller in addprenotaz prima di addpren");
                    Model.addPren(request);
                   // Logger.getGlobal().info("sono nel controller in addprenotaz");
                    aggPage(request);
                    break;
                    
                case "remPrenotaz":
                    Model.remPren(request);
                    aggPage(request);
                    break;
   
            }
        }
        RequestDispatcher rd;
        //request e non named perchè richiediamo una jsp
        rd = getServletContext().getRequestDispatcher("/index.jsp");
        rd.include(request, response);
    }
    
    public void switchPage(HttpServletRequest req){
        String page= req.getParameter("name");
        if(page!=null)
            req.getSession().setAttribute("view",page);
        else
            req.getSession().setAttribute("view","");
        aggPage(req);
    }
    
    public void aggPage(HttpServletRequest req){
        String ruolo=(String)(req.getSession()).getAttribute("ruolo");
        String login=(String)(req.getSession()).getAttribute("username");
        String page=(String)(req.getSession()).getAttribute("view");
        if(page.equals("catalogo"))
            req.getSession().setAttribute("dati",DBManager.query("SELECT * FROM PIZZE"));
        else if(page.equals("loginManager"))
            req.getSession().setAttribute("dati",DBManager.query("SELECT * FROM UTENTI"));
        else if(page.equals("prenotazioni")){
            //
            if (ruolo.equals("user"))
                req.getSession().setAttribute("dati",DBManager.query("SELECT * FROM PRENOTAZ WHERE CLIENTE= '"+login+ "'"));
            //
            else req.getSession().setAttribute("dati",DBManager.query("SELECT * FROM PRENOTAZ"));
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
