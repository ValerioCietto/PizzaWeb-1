package mvc;

import components.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})

public class Controller extends HttpServlet {
    

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
                    //Model.addPizza(request);
                    aggPage(request);
                    break;
                case "remPizza":
                    //Model.remPizza(request);
                    aggPage(request);
                    break;
                case "modPizza":
                    //Model.modPizza(request);
                    aggPage(request);
                    break;
                case "registration":
                    //Model.addUtente(request);
                    break;
                case "addPrenotaz":
                     //Logger.getGlobal().info("sono nel controller in addprenotaz prima di addpren");
                    //Model.addPrenotazione(request);
                   // Logger.getGlobal().info("sono nel controller in addprenotaz");
                    aggPage(request);
                    break;
                case "remPrenotaz":
                    //Model.remPrenotazione(request);
                    aggPage(request);
                    break;
   
            }
        }
        RequestDispatcher rd;
        //request e non named perchè richiediamo una jsp
        rd = getServletContext().getRequestDispatcher("/index.jsp");
        rd.include(request, response);
    }
    
    public static void switchPage(HttpServletRequest req){
        String page= req.getParameter("name");
        if(page!=null)
            req.getSession().setAttribute("view",page);
        else
            req.getSession().setAttribute("view","");
        aggPage(req);
    }
   
    public static void aggPage(HttpServletRequest req){
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
    
    public static void login(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try{
            Model.login(username, password, s);
        }catch(SQLException e){
            System.out.println("Login fallito!");
        }
    }
    
    public static void register(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try{
            Model.creaUtente(username, password);
        }catch(SQLException e){
            System.out.println("Registrazione fallita!");
        }
    }
    
    public static void logout(HttpServletRequest req) {
        HttpSession s = req.getSession();
        s.invalidate();
        s = req.getSession();
        notifica(s, "logout effettuato");
    }
    
////////////////////////////////////////////////////////////////////////////////
// METODI SU CATALOGO   

    // Solo visualizzazione
    
    public static void addPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            if(Model.getUtente(username).getPermission().equals("admin")){
                String nome=req.getParameter("nome");
                String ingredienti=req.getParameter("ingredientiPizza");
                double prezzo=Double.parseDouble(req.getParameter("prezzoPizza"));
                if(nome!=null && ingredienti!=null && !nome.equals("") && !ingredienti.equals("") && prezzo >0){
                    Pizza p=new Pizza(nome,ingredienti,prezzo);
                    Model.addPizza(p);
                    notifica(s, "pizza aggiunta");
                }else
                    notifica(s, "pizza non aggiunta");
            }else
                notifica(s, "non hai i permessi");
        }catch(SQLException e){notifica(s,"???A???");}
        getCatalogo(req);
    }
    public static void getCatalogo(HttpServletRequest req){
        HttpSession s = req.getSession();
        ArrayList<Pizza> listaPizze=null;
        try{
             listaPizze= Model.getCatalogo();
        }catch(SQLException e){notifica(s,"Impossibile ottenere il catalogo");}
        View.visualizzaCatalogo(listaPizze ,req);
    }
    public static void modPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            if(Model.getUtente(username).getPermission().equals("admin")){
                Pizza p=Model.getPizza(req.getParameter("nomePizza"));
                if(p!=null){
                    //gestione prezzo
                    double prezzo=Double.parseDouble(req.getParameter("prezzoPizza"));
                    if(prezzo>0)
                        p.setPrezzo(prezzo);
                    //gestione ingrediente
                    String ingredienti=req.getParameter("ingredientiPizza");
                    if(ingredienti!=null && !ingredienti.equals(""))
                        p.setIngredienti(ingredienti);
                    //applica modifiche
                    Model.modPizza(p);
                    notifica(s, "pizza aggiornata");
                }else
                    notifica(s, "pizza non trovata");
            }else
                notifica(s, "non hai i permessi");
        }catch(SQLException e){notifica(s,"???A???");}
        getCatalogo(req);
    }
    public static void remPizza(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        try{
            if(Model.getUtente(username).getPermission().equals("admin")){
                Pizza p=Model.getPizza(req.getParameter("nomePizza"));
                if(p!=null){
                    Model.remPizza(p);
                    notifica(s, "pizza rimossa");
                }else
                    notifica(s, "pizza non trovata");
            }else
                notifica(s, "non hai i permessi");
        }catch(SQLException e){notifica(s,"???B???");}
        getCatalogo(req);
    }

////////////////////////////////////////////////////////////////////////////////
// METODI SU PRENOTAZIONI

    public static void getPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        ArrayList<Prenotazione> listaPrenotazioni=null;
        try{
            listaPrenotazioni = new ArrayList();
            Utente u=Model.getUtente(username);
            switch (u.getPermission()){
                case "user":
                    listaPrenotazioni=Model.getListaPrenotazioni(u.getId());
                    notifica(s, "Caricate Proprie Prenotazioni");
                    break;
                case "admin":
                    listaPrenotazioni=Model.getListaPrenotazioni();
                    notifica(s, "Caricate Tutte Prenotazioni");
                    break;
                default:
                    notifica(s, "non hai i permessi");
                    break;
            }
        }catch(SQLException e){notifica(s,"Impossibile ottenere il catalogo");}
        View.visualizzaPrenotazioni(listaPrenotazioni, req);
    }
    public static void addPrenotazioni(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p=null;
        int idUser;
        int idPizza;
        int quantita;
        String data;
        try{
            switch (Model.getUtente(username).getPermission()){
                case "user":
                    idUser=Model.getIdUtente(username);
                    idPizza=Integer.parseInt(req.getParameter("idPizza"));
                    quantita=Integer.parseInt(req.getParameter("quantita"));
                    data=req.getParameter("data");
                    if(Model.getPizza(data)!=null && quantita>0 && data!=null && !data.equals("")){
                        p = new Prenotazione(idUser,idPizza,quantita,data);
                        Model.addPrenotazione(p);
                        notifica(s, "prenotazione aggiunta");
                    }else
                        notifica(s,"prenotazione non aggiunta");
                    break;
                case "admin":
                    idUser=Integer.parseInt(req.getParameter("idUser"));
                    idPizza=Integer.parseInt(req.getParameter("idPizza"));
                    quantita=Integer.parseInt(req.getParameter("quantita"));
                    data=req.getParameter("data");
                    if(Model.getPizza(data)!=null && quantita>0 && data!=null && !data.equals("")){
                        p = new Prenotazione(idUser,idPizza,quantita,data);
                        Model.addPrenotazione(p);
                        notifica(s, "prenotazione aggiunta");
                    }else
                        notifica(s,"prenotazione non aggiunta");
                    break;
                default:
                    notifica(s, "non hai i permessi");
                    break;
            }
        }catch(SQLException e){notifica(s,"???B???");}
        getPrenotazioni(req);
    }
    public static void modPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p=null;
        try{
            switch (Model.getUtente(username).getPermission()){
                case "user":
                    p=Model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
                    if(p!=null){
                        if(p.getIdUtente()==Model.getIdUtente(username)){
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

                            Model.modPrenotazione(p);
                            notifica(s, "prenotazione aggiornata");
                        }else
                            notifica(s, "prenotazione non tua");
                    }else
                        notifica(s, "prenotazione non trovata");
                    break;
                case "admin":
                    p=Model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
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

                        Model.modPrenotazione(p);
                        notifica(s, "prenotazione aggiornata");
                        
                    }else
                        notifica(s, "prenotazione non trovata");
                    break;
                default:
                    notifica(s, "non hai i permessi");
                    break;
            }
        }catch(SQLException e){notifica(s,"???B???");}
        getPrenotazioni(req);
    }
    public static void remPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p=null;
        try{
            switch (Model.getUtente(username).getPermission()){
                case "user":
                    p =Model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
                    if(p!=null){
                        if(p.getIdUtente()==Model.getIdUtente(username)){
                            Model.remPrenotazione(p);
                            notifica(s, "prenotazione rimossa");
                        }else
                            notifica(s, "prenotazione non tua");
                    }
                    else
                        notifica(s, "prenotazione non trovata");
                    break;
                case "admin":
                    p =Model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
                    if(p!=null){
                        Model.remPrenotazione(p);
                        notifica(s, "prenotazione rimossa");
                    }
                    else
                        notifica(s, "prenotazione non trovata");
                    break;
                default:
                    notifica(s, "non hai i permessi");
                    break;
            }
        }catch(SQLException e){notifica(s,"???B???");}
        getPrenotazioni(req);
    }
    
    public static void notifica(HttpSession s,String txt){
        s.setAttribute("message",txt);
        System.out.println(txt);
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
