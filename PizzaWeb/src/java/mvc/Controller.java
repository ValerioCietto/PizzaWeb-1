package mvc;

import java.io.*;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})

public class Controller extends HttpServlet {
    
    private final Model model;

////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    public Controller() throws SQLException {
        super();
        model = new Model();       
        
    }

////////////////////////////////////////////////////////////////////////////////
// GESTIONE DELLE PAGINE
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        
        String action= request.getParameter("action");
        
        if(action!=null){
            switch (action) {
                case "switch":
                    switchPage(request);
                    break;
                case "login":
                    login(request);
                    break;
                case "logout":
                    logout(request);
                    break;
                case "addPizza":
                    model.addPizza(request);
                    aggPage(request);
                    break;
                case "remPizza":
                    model.remPizza(request);
                    aggPage(request);
                    break;
                case "modPizza":
                    model.modPizza(request);
                    aggPage(request);
                    break;
                case "registration":
                    model.addUser(request);
                    break;
                case "addPrenotaz":
                     //Logger.getGlobal().info("sono nel controller in addprenotaz prima di addpren");
                    model.addPren(request);
                   // Logger.getGlobal().info("sono nel controller in addprenotaz");
                    aggPage(request);
                    break;
                    
                case "remPrenotaz":
                    model.remPren(request);
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
            System.out.println();
        else if(page.equals("loginManager"))
            System.out.println();
        else if(page.equals("prenotazioni")){

        if (ruolo.equals("user"))
            System.out.println();
        else
            System.out.println();
        }
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DEL MODEL    
    
    public void login(HttpServletRequest req) throws SQLException {
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        model.loginModel(username, password, s);
    }

    public void logout(HttpServletRequest req) {
        HttpSession s = req.getSession();
        s.invalidate();
        s = req.getSession();
        s.setAttribute("message", "logout effettuato");
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// NON MODIFICARE    
    
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
