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
    
    /**
     *  Gestisce le richieste provenienti dalle pagine jsp
     * 
     * @param request
     * @param response
     * 
     * @throws ServletException
     * @throws IOException 
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        String action= request.getParameter("action");
        request.getSession().setAttribute("message", "");
        
        if(action!=null){
            switch (action) {
                case "switch":
                    switchPage(request);
                    break;
                //////////////////////////////////////
                case "register":
                    register(request);
                    break;
                case "login":
                    login(request);
                    break;
                case "logout":
                    logout(request);
                    break;
                //////////////////////////////////////
                case "addPizza":
                    addPizza(request);
                    aggiornaPagina(request);
                    break;
                case "remPizza":
                    remPizza(request);
                    aggiornaPagina(request);
                    break;
                case "modPizza":
                    modPizza(request);
                    aggiornaPagina(request);
                    break;
                case "addPrenotaz":
                    //Logger.getGlobal().info("sono nel controller in addprenotaz prima di addpren");
                    addPrenotazioni(request);
                   // Logger.getGlobal().info("sono nel controller in addprenotaz");
                    aggiornaPagina(request);
                    break;
                case "remPrenotaz":
                    //Model.remPrenotazione(request);
                    aggiornaPagina(request);
                    break;
   
            }
        }
        
        RequestDispatcher rd;
        //request e non named perchè richiediamo una jsp
        rd = getServletContext().getRequestDispatcher("/index.jsp");
        rd.include(request, response);
    }
    
    /**
     *  Si occupa di visualizzare la pagina richiesta
     * 
     * @param req 
     */
    
    public static void switchPage(HttpServletRequest req){
        String page = req.getParameter("name");
        if(page!=null && !page.equals(""))
            req.getSession().setAttribute("view",page);
        else
            req.getSession().setAttribute("view","");
        aggiornaPagina(req);
    }
    
    /**
     * Esegue il refresh della pagina
     * 
     * @param req 
     */
    
    public static void aggiornaPagina(HttpServletRequest req){
        String page=(String)(req.getSession()).getAttribute("view");
        
        switch (page) {
            case "catalogo":
                getCatalogo(req);
                break;
            case "prenotazioni":
                getPrenotazioni(req);
                break;
            case "Registrati":
                /*getRegistration(req);*/
                break;
                
        }
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI SESSIONE
    
    /**
     * Gestisce il login
     * 
     * @param req
     */
    
    public static void login(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try{
            if(Model.login(username, password)){
                View.login(req);
                s.setAttribute("username", username);
                s.setAttribute("password", password);
                notifica(s, "Login ok!");
            }
            else{
                notifica(req.getSession(), "Login error!");            
            }
        }catch(SQLException e){
            notifica(req.getSession(), "SQL error!");
        }
    }
    
    /**
     * Gestisce la registrazione
     * 
     * @param req 
     */
    
    public static void register(HttpServletRequest req){
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try{
            Model.creaUtente(username, password);
        }catch(SQLException e){
            notifica(req.getSession(),"Registrazione fallita!");
        }
    }
    
    
    /**
     * Controlla se è stato effettuato il login
     * 
     * @param req 
     * 
     * @return  
     */
    
    public static boolean checkLogin(HttpServletRequest req){
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try{
            if(Model.checkLogin(username, password)!=null){
                return true;
            }
        }catch(SQLException e){
                req.getSession().setAttribute("message", "SQL error!");   
        }
        return false;
    }
    
    
    /**
     * Gestisce la disconnessione
     * 
     * @param req 
     */
    
    public static void logout(HttpServletRequest req) {
        HttpSession s = req.getSession();
        s.invalidate();
        s = req.getSession();
        notifica(req.getSession(), "logout effettuato");
    }
    
    
////////////////////////////////////////////////////////////////////////////////
// METODI SU CATALOGO   

    // Solo visualizzazione
    
    /**
     * Permette all'admin di aggiungere  una pizza
     * 
     * @param req 
     */
    
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
    }

    /**
     * Permette all'admin di modificare una pizza
     * 
     * @param req 
     */
    
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
    }
    
    /**
     * Permette all'admin di rimuovere una pizza
     * 
     * @param req 
     */
    
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
    }

    
////////////////////////////////////////////////////////////////////////////////
// METODI SU PRENOTAZIONI

    /**
     * Permette ad un user di aggiungere una prenotazione
     * 
     * @param req 
     */
    
    public static void addPrenotazioni(HttpServletRequest req){
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p;
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
    }
    
    /**
     * Permette ad un user di modificare le sue prenotazioni ed all'admin di modificarle tutte
     * 
     * @param req 
     */
    
    public static void modPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p;
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
    }
    
    /**
     * Permette ad un user di rimuovere le sue prenotazioni
     * 
     * @param req 
     */
    
    public static void remPrenotazioni(HttpServletRequest req){
        
        HttpSession s = req.getSession();
        String username = req.getParameter("username");
        Prenotazione p;
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
                    p = Model.getPrenotazione(Integer.parseInt(req.getParameter("idPrenotaz")));
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
    }
    
    /**
     * Permette di assegnegnare una stringa ad un attributo della sessione
     * 
     * @param s
     * @param txt
     */
    
    public static void notifica(HttpSession s,String txt){
        s.setAttribute("message",txt+"<p>"+s.getAttribute("message")+"</p>");
    }

      
///////////////////////////////////////////////////////////////////////////////////////////
// METODI DI GET
    
    /**
     * Permette a TUTTI di visualizzare il catalogo pizze
     * 
     * @param req 
     * @return  
     */
    
    public static String getCatalogo(HttpServletRequest req){
        HttpSession s = req.getSession();
        ArrayList<Pizza> listaPizze=null;
        
        try{
             listaPizze= Model.getCatalogo();
             notifica(s,"catalogo ottenuto");
        }catch(SQLException e){
            notifica(s,"Impossibile ottenere il catalogo");
        }
        
        return View.visualizzaCatalogo(listaPizze ,req);
    }
    
    /**
     * Permette ad un user di visualizzare le sue prenotazioni ed all'admin di visualizzarle tutte
     * 
     * @param req 
     * @return  
     */
    
    public static String getPrenotazioni(HttpServletRequest req){
        String username = req.getParameter("username");
        ArrayList<Prenotazione> listaPrenotazioni = null;
        
        try{
            listaPrenotazioni = new ArrayList();
            Utente u = Model.getUtente(username);
            
            if (u != null && u.getPermission().equals("user")){
                listaPrenotazioni = Model.getListaPrenotazioni(u.getId());
                notifica(req.getSession(), "Caricate Proprie Prenotazioni");
            }
            else if(u != null && u.getPermission().equals("admin")){
                listaPrenotazioni = Model.getListaPrenotazioni();
                notifica(req.getSession(), "Caricate Tutte Prenotazioni");
            }
            else{
                notifica(req.getSession(), "non hai i permessi");
            }
        }catch(SQLException e){
            notifica(req.getSession(),"Impossibile ottenere il catalogo");
        }
        
        return View.visualizzaPrenotazioni(listaPrenotazioni, req);
    }
    
    /**
     * Permette ad un user di registrarsi
     * 
     * @param req 
     * @return  
     */
    
    /*public static String getRegistration(HttpServletRequest req){        
        return View.paginaRegistrazione(req);
    }*/
        

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
