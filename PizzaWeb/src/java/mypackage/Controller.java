/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mypackage;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author st116578
 * prova commit
 */
@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Controller extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        operazione(request);
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<link rel=\"stylesheet\" href=\"pizzacss.css\" type=\"text/css\">");
            out.println("<head>");
            out.println("<title>Pizzeria</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println(Model.pagina(request));
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }
    public void operazione(HttpServletRequest req)throws ServletException, IOException{
        String action= req.getParameter("action");
        if(action!=null){
            switch (action) {
                /*case "switch":
                    login(req);
                    break;*/
                case "login":
                    Model.login(req);
                    break;
                case "logout":
                    Model.logout(req);
                    break;
                case "aggPizza":
                    aggiungi(req);
                    break;
                case "remPizza":
                    remove(req);
                    break;
                case "modPizza":
                    aggiungi(req);
                    break;
                case "aggLogin":
                    aggiungi(req);
                    break;
                case "remLogin":
                    remove(req);
                    break;
                case "modLogin":
                    aggiungi(req);
                    break;    
            }
        }
    }
    
    public static void remove(HttpServletRequest req){
        HttpSession s=req.getSession();
        String name = req.getParameter("pizza");
        if(name!=null && !name.equals("")){
            DBManager.remPizza(name);
            s.setAttribute("message","rimossa pizza "+name);
            return;
        }
        name = req.getParameter("login");
        if(name!=null && !name.equals("")){
            DBManager.remLogin(name);
            s.setAttribute("message","rimosso login "+name);
            return;
        }
        s.setAttribute("message","errore di input");
    }
    public static void aggiungi(HttpServletRequest req){
        HttpSession s=req.getSession();
        String name = req.getParameter("pizza");
        if(name!=null && !name.equals("")){
            String ingredienti=req.getParameter("ingredienti");
            if(ingredienti!=null){
                try{
                    int prezzo= Integer.parseInt(req.getParameter("prezzo"));
                    DBManager.addPizza(name, ingredienti, prezzo);
                    s.setAttribute("message","aggiunta pizza "+name);
                }catch(NumberFormatException e){
                    s.setAttribute("message","errore input prezzo");
                }
            }else
                s.setAttribute("message","errore input ingredienti");
            return;
        }
        s.setAttribute("message","parametro pizza non trovato");
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
