package mvc;

import components.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})

public class Controller extends HttpServlet {
    
    private final Model model;
    private Utente user;
    private ArrayList<Pizza> catalogo;
    private ArrayList<Prenotazione> listaPrenotazioni;
    

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
                    //model.addPizza(request);
                    aggPage(request);
                    break;
                case "remPizza":
                    //model.remPizza(request);
                    aggPage(request);
                    break;
                case "modPizza":
                    //model.modPizza(request);
                    aggPage(request);
                    break;
                case "registration":
                    //model.addUtente(request);
                    break;
                case "addPrenotaz":
                     //Logger.getGlobal().info("sono nel controller in addprenotaz prima di addpren");
                    //model.addPrenotazione(request);
                   // Logger.getGlobal().info("sono nel controller in addprenotaz");
                    aggPage(request);
                    break;
                case "remPrenotaz":
                    //model.remPrenotazione(request);
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
        
        switch (page) {
            case "catalogo":
                System.out.println();
                break;
            case "loginManager":
                System.out.println();
                break;
            case "prenotazioni":
                if (ruolo.equals("user"))
                    System.out.println();
                else
                    System.out.println();
                break;
        }
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DEL MODEL    
    
////////////////////////////////////////////////////////////////////////////////
// METODI SESSIONE
    
    public void login(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try{
            model.login(username, password, s);
        }catch(SQLException e){
            System.out.println("Login fallito!");
        }
    }
    
    public void register(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try{
            model.creaUtente(username, password);
        }catch(SQLException e){
            System.out.println("Registrazione fallita!");
        }
    }
    
    public void logout(HttpServletRequest req) {
        HttpSession s = req.getSession();
        s.invalidate();
        s = req.getSession();
        s.setAttribute("message", "logout effettuato");
    }
    
////////////////////////////////////////////////////////////////////////////////
// METODI SU CATALOGO   

    // Solo visualizzazione
    public void getCatalogo(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            ArrayList<Pizza> listaPizze = model.getCatalogo();
        }catch(SQLException e){System.out.println("Impossibile ottenere il catalogo");}
    }
    public void modPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        //......
        try{
            if(model.getUtente(username).getPermission().equals("admin")){
                //if (ho tutti i parametri e la pizza esiste)
                    //modifica pizza
                //else
                    //mancano dati o pizza
            }else{
                //non hai i permessi
            }
        }catch(SQLException e){System.out.println("???A???");}
    }
    public void remPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        //......
        try{
            if(model.getUtente(username).getPermission().equals("admin")){
                //if (pizza esiste)
                    //rimuovi pizza
                //else
                    //non esisteva gia...
            }
            else{
                //non hai i permessi
            }
        }catch(SQLException e){System.out.println("???B???");}
    }

////////////////////////////////////////////////////////////////////////////////
// METODI SU PRENOTAZIONI

    public ArrayList<Prenotazione> getPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            ArrayList<Prenotazione> listaPrenotazioni = new ArrayList();
            switch (model.getUtente(username).getPermission()){
                case "user":
                    //visualizza proprie prenotazioni
                    break;
                case "admin":
                    //visualizza le prenotazioni di tutti
                    break;
                default:
                    //non hai i permessi
                    break;
            }
        }catch(SQLException e){System.out.println("Impossibile ottenere il catalogo");}
        return listaPrenotazioni;
    }
    public void modPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            switch (model.getUtente(username).getPermission()){
                case "user":
                    //if (ho tutti i parametri e la prenotazione esiste ed è tua)
                        //modifica prenotaz
                    //else
                        //mancano dati o prenotaz o non è tua
                    break;
                case "admin":
                     //if (ho tutti i parametri e la prenotazione esiste)
                        //modifica prenotaz
                    //else
                        //mancano dati o prenotaz
                    break;
                default:
                    //non hai i permessi
                    break;
            }
        }catch(SQLException e){System.out.println("???B???");}
    }
    public void remPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            switch (model.getUtente(username).getPermission()){
                case "user":
                    //if (la prenotazione esiste ed è tua)
                        //rem prenotaz
                    //else
                        //prenotaz non esiste o non è tua
                    break;
                case "admin":
                     //if (la prenotazione esiste)
                        //rem prenotaz
                    //else
                        //non esiste pren prenotaz
                    break;
                default:
                    //non hai i permessi
                    break;
            }
        }catch(SQLException e){System.out.println("???B???");}
    }

////////////////////////////////////////////////////////////////////////////////
// METODI ADMIN    
    
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
