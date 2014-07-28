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
        ArrayList<Pizza> listaPizze=null;
        try{
             listaPizze= model.getCatalogo();
        }catch(SQLException e){System.out.println("Impossibile ottenere il catalogo");}
        //View.visualizzaCatalogo(listaPizze ,req) possibile listaPizze null
    }
    public void modPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            if(model.getUtente(username).getPermission().equals("admin")){
                Pizza p=model.getPizza(req.getParameter("nomePizza"));
                if(p!=null){
                    //gestione prezzo
                    double prezzo=Double.parseDouble(req.getParameter("prezzoPizza"));
                    if(prezzo>0)
                        p.setPrezzo(prezzo);
                    //gestione ingrediente
                    String ingredienti=req.getParameter("prezzoPizza");
                    if(ingredienti!=null && !ingredienti.equals(""))
                        p.setIngredienti(ingredienti);
                    //applica modifiche
                    model.modPizza(p);
                    s.setAttribute("message", "pizza aggiornata");
                }else
                    s.setAttribute("message", "pizza non trovata");
            }else
                s.setAttribute("message", "non hai i permessi");
        }catch(SQLException e){System.out.println("???A???");}
        getCatalogo(req);
    }
    public void remPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            if(model.getUtente(username).getPermission().equals("admin")){
                Pizza p=model.getPizza(req.getParameter("nomePizza"));
                if(p!=null){
                    model.remPizza(p);
                    s.setAttribute("message", "pizza rimossa");
                }else
                    s.setAttribute("message", "pizza non trovata");
            }else
                s.setAttribute("message", "non hai i permessi");
        }catch(SQLException e){System.out.println("???B???");}
        getCatalogo(req);
    }

////////////////////////////////////////////////////////////////////////////////
// METODI SU PRENOTAZIONI

    public void getPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        ArrayList<Prenotazione> listaPrenotazioni=null;
        try{
            listaPrenotazioni = new ArrayList();
            Utente u=model.getUtente(username);
            switch (u.getPermission()){
                case "user":
                    listaPrenotazioni=model.getListaPrenotazioni(u.getId());
                    s.setAttribute("message", "Caricate Proprie Prenotazioni");
                    break;
                case "admin":
                    listaPrenotazioni=model.getListaPrenotazioni();
                    s.setAttribute("message", "Caricate Tutte Prenotazioni");
                    break;
                default:
                    s.setAttribute("message", "non hai i permessi");
                    break;
            }
        }catch(SQLException e){System.out.println("Impossibile ottenere il catalogo");}
        //View.visualizzaPrenotazioni(listaPrenotazioni, req);
    }
    public void modPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p=null;
        try{
            switch (model.getUtente(username).getPermission()){
                case "user":
                    p=model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
                    if(p!=null){
                        if(p.getIdUtente()==model.getIdUtente(username)){
                            //gestione pizza
                            int pizza=Integer.parseInt(req.getParameter("pizzaPrenotaz"));
                            if(pizza>0)
                                p.setIdPizza(pizza);
                            //gestione quantità
                            int quantita=Integer.parseInt(req.getParameter("quantitaPrenotaz"));
                            if(quantita>0)
                                p.setIdPizza(quantita);

                            /////////////////////
                            //gestione id
                            /////////////////////

                            //gestione data
                            String data=req.getParameter("dataPrenotaz");
                            if(data!=null && !data.equals(""))
                                p.setData(data);
                            //gestione stato
                            String stato=req.getParameter("statoPrenotaz");
                            if(stato!=null && !stato.equals(""))
                                p.setData(stato);

                            model.modPrenotazione(p);
                            s.setAttribute("message", "prenotazione aggiornata");
                        }else
                            s.setAttribute("message", "prenotazione non tua");
                    }else
                        s.setAttribute("message", "prenotazione non trovata");
                    break;
                case "admin":
                    p=model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
                    if(p!=null){
                        //gestione cliente
                        int cliente=Integer.parseInt(req.getParameter("clientePrenotaz"));
                        if(cliente>0)
                            p.setIdUtente(cliente);
                        //gestione pizza
                        int pizza=Integer.parseInt(req.getParameter("pizzaPrenotaz"));
                        if(pizza>0)
                            p.setIdPizza(pizza);
                        //gestione quantità
                        int quantita=Integer.parseInt(req.getParameter("quantitaPrenotaz"));
                        if(quantita>0)
                            p.setIdPizza(quantita);
                        //gestione data
                        String data=req.getParameter("dataPrenotaz");
                        if(data!=null && !data.equals(""))
                            p.setData(data);
                        //gestione stato
                        String stato=req.getParameter("statoPrenotaz");
                        if(stato!=null && !stato.equals(""))
                            p.setData(stato);

                        model.modPrenotazione(p);
                        s.setAttribute("message", "prenotazione aggiornata");
                        
                    }else
                        s.setAttribute("message", "prenotazione non trovata");
                    break;
                default:
                    s.setAttribute("message", "non hai i permessi");
                    break;
            }
        }catch(SQLException e){System.out.println("???B???");}
        getPrenotazioni(req);
    }
    public void remPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p=null;
        try{
            switch (model.getUtente(username).getPermission()){
                case "user":
                    p =model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
                    if(p!=null){
                        if(p.getIdUtente()==model.getIdUtente(username)){
                            model.remPrenotazione(p);
                            s.setAttribute("message", "prenotazione rimossa");
                        }else
                            s.setAttribute("message", "prenotazione non tua");
                    }
                    else
                        s.setAttribute("message", "prenotazione non trovata");
                    break;
                case "admin":
                    p =model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
                    if(p!=null){
                        model.remPrenotazione(p);
                        s.setAttribute("message", "prenotazione rimossa");
                    }
                    else
                        s.setAttribute("message", "prenotazione non trovata");
                    break;
                default:
                    s.setAttribute("message", "non hai i permessi");
                    break;
            }
        }catch(SQLException e){System.out.println("???B???");}
        getPrenotazioni(req);
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
