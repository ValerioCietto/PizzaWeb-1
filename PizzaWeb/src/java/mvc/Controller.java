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
        request.getSession().setAttribute("message", "");
        response.setContentType("text/html;charset=UTF-8");
        String action= request.getParameter("action");
        
                    notifica(request.getSession(), action);
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
                case "addPrenotazione":
                    addPrenotazioni(request);
                    aggiornaPagina(request);
                    break;
                case "modPrenotazione":
                    modPrenotazioni(request);
                    aggiornaPagina(request);
                    break;
                case "remPrenotaz":
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
                //getCatalogo(req);
                req.getSession().setAttribute("view", "catalogo");
                break;
            case "prenotazioni":
                //getPrenotazioni(req);
                req.getSession().setAttribute("view", "prenotazioni");
                break;
            case "utenti":
                //getUtenti(req);
                req.getSession().setAttribute("view", "utenti");
                break;
            case "Registrati":
                //getRegistration(req);
                req.getSession().setAttribute("view", "registrati");
                break;
            case "back":
                goBack(req);
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
        String username = ""+req.getParameter("username");
        String password = ""+req.getParameter("password");
        try {
            if(!checkLogin(req) && Model.login(username, password)){
                s.setAttribute("username", username);
                s.setAttribute("password", password);
                View.login(req);
                notifica(s, "Login ok!");
            }
            else{
                notifica(req.getSession(), "Login error!");
            }
        } catch (SQLException ex) {
            notifica(req.getSession(), "Login exception!");
        }
    }
    
    /**
     * Gestisce la registrazione
     * 
     * @param req 
     */
    
    public static void register(HttpServletRequest req){
        String username = req.getParameter("username");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        try{
            if(password1.equals(password2))
                Model.creaUtente(username, password1);
            else
                notifica(req.getSession(),"password diverse");
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
        String username = ""+req.getSession().getAttribute("username");
        String password = ""+req.getSession().getAttribute("password");
        try{
            if(Model.login(username, password)){
                return true;
            }
        }catch(SQLException e){
                req.getSession().setAttribute("message", "SQL error!");   
        }
        return false;
    }
    
    /**
     * Controlla se è stato effettuato il login
     * 
     * @param req 
     * 
     * @return  
     */
    
    public static boolean checkAdmin(HttpServletRequest req){
        
        String username = ""+req.getSession().getAttribute("username");
        String password = ""+req.getSession().getAttribute("password");
        try{
            if(Model.checkLogin(username, password).getPermission().equals("admin")){
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
        if(checkLogin(req)){
            s.invalidate();
            s = req.getSession();
            notifica(req.getSession(), "logout effettuato");
        }else
            notifica(req.getSession(), "logout impossibile");
    }
    
    /**
     * Permette di assegnegnare una stringa ad un attributo della sessione
     * 
     * @param s
     * @param txt
     */
    
    public static void notifica(HttpSession s,String txt){
        s.setAttribute("message",s.getAttribute("message")+"//"+txt);
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
        String username = s.getAttribute("username")+"";
        try{
            if(Model.getUtente(username).getPermission().equals("admin")){
                String nome = req.getParameter("nome");
                String ingredienti = req.getParameter("ingredienti");
                double prezzo = Double.parseDouble(req.getParameter("prezzo"));
                if(nome != null && ingredienti!=null && !nome.equals("") && !ingredienti.equals("") && prezzo >0){
                    Pizza p = new Pizza(nome,ingredienti,prezzo);
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
        String username = s.getAttribute("username")+"";
        try{
            if(Model.getUtente(username).getPermission().equals("admin")){
                Pizza p=Model.getPizza(req.getParameter("pizza"));
                if(p!=null){
                    //gestione prezzo
                    double prezzo=Double.parseDouble(req.getParameter("prezzo"));
                    if(prezzo>0)
                        p.setPrezzo(prezzo);
                    //gestione ingrediente
                    String ingredienti=req.getParameter("ingredienti");
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
        String username = s.getAttribute("username")+"";
        try{
            if(Model.getUtente(username).getPermission().equals("admin")){
                Pizza p=Model.getPizza(req.getParameter("pizza"));
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
        String username = s.getAttribute("username")+"";
        String nomePizza = req.getParameter("pizza");
        int quantita= Integer.parseInt(req.getParameter("quantita"));
        String data=req.getParameter("data");
        try{
            if(Model.getUtente(username).getPermission().equals("user")){
                int idUser=Model.getIdUtente(username);
                int idPizza=Model.getIdPizza(nomePizza);
                if(Model.getPizza(nomePizza)!=null && quantita>0 && data!=null && !data.equals("")){
                    Prenotazione p = new Prenotazione(idUser,idPizza,quantita,data);
                    Model.addPrenotazione(p);
                    notifica(s, "prenotazione aggiunta");
                }else
                    notifica(s,"prenotazione non aggiunta");
            }
            else{    
                notifica(s, "non hai i permessi");
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
        String username = req.getSession().getAttribute("username")+"";
        Prenotazione p;
        try{
            switch (Model.getUtente(username).getPermission()){
                case "user":
                    p=Model.getPrenotazione(Integer.parseInt(req.getParameter("id")));
                    if(p!=null){
                        if(p.getIdUtente()==Model.getIdUtente(username)){
                            //gestione quantità
                            int quantita=Integer.parseInt(req.getParameter("quantita"));
                            if(quantita>0)
                                p.setQuantita(quantita);
                            //gestione data
                            String data=req.getParameter("data");
                            if(data!=null && !data.equals(""))
                                p.setData(data);
                            //gestione stato
                            String stato=req.getParameter("stato");
                            if(stato!=null && !stato.equals(""))
                                p.setStato(stato);

                            Model.modPrenotazione(p);
                            notifica(s, "prenotazione aggiornata");
                        }else
                            notifica(s, "prenotazione non tua");
                    }else
                        notifica(s, "prenotazione non trovata");
                    break;
                case "admin":
                    p=Model.getPrenotazione(Integer.parseInt(req.getParameter("id")));
                    if(p!=null){
                        
                        //gestione cliente
                        if(req.getParameter("utente")!=null){
                            int utente=Model.getIdUtente(req.getParameter("utente"));
                            if(utente>0)
                                p.setIdUtente(utente);
                        }
                        
                        //gestione pizza
                        if(req.getParameter("pizza")!=null){
                            int pizza=Model.getIdPizza(req.getParameter("pizza"));
                            if(pizza>0)
                                p.setIdPizza(pizza);
                        }
                        
                        //gestione quantità
                        if(req.getParameter("quantita")!=null){
                            int quantita=Integer.parseInt(req.getParameter("quantita"));
                            if(quantita>0)
                                p.setQuantita(quantita);
                        }
                        
                        //gestione data
                        if(req.getParameter("data")!=null){
                            String data=req.getParameter("data");
                            if(data!=null && !data.equals(""))
                                p.setData(data);
                        }
                        
                        //gestione stato
                        if(req.getParameter("stato")!=null){
                            String stato=req.getParameter("stato");
                            if(stato!=null && !stato.equals(""))
                                p.setStato(stato);
                        }
                        
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
        String username = req.getSession().getAttribute("username")+"";
        ArrayList<Pizza> listaPizze=null;
        Utente u = null;
        
        try{
            u = Model.getUtente(username);
            
            listaPizze= Model.getCatalogo();
            notifica(s,"catalogo ottenuto");
        }catch(SQLException e){
            notifica(s,"Impossibile ottenere il catalogo");
        }
        
        return View.visualizzaCatalogo(listaPizze, u  ,req);
    }
    
    /**
     * Permette ad un user di visualizzare le sue prenotazioni ed all'admin di visualizzarle tutte
     * 
     * @param req 
     * @return  
     * @throws java.sql.SQLException  
     */
    
    public static String getPrenotazioni(HttpServletRequest req) throws SQLException{
        String username = req.getSession().getAttribute("username")+"";
        ArrayList<Prenotazione> listaPrenotazioni = null;
        
        Utente u = null;
        
        try{
            listaPrenotazioni = new ArrayList();
            u = Model.getUtente(username);
            
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
        
        return View.visualizzaPrenotazioni(listaPrenotazioni, u, req);
    }
    
    /**
     * Permette ad un user di visualizzare le sue prenotazioni ed all'admin di visualizzarle tutte
     * 
     * @param req 
     * @return  
     */
    
    public static String getUtenti(HttpServletRequest req){
        String username = req.getSession().getAttribute("username")+"";
        ArrayList<Utente> listaUtenti= null;
        
        try{
            listaUtenti = new ArrayList();
            Utente u = Model.getUtente(username);
            
            if(u != null && u.getPermission().equals("admin")){
                listaUtenti = Model.getListaUtenti(u.getId());
                notifica(req.getSession(), "Caricate tutti gli utenti");
            }
            else{
                notifica(req.getSession(), "non hai i permessi");
            }
        }catch(SQLException e){
            notifica(req.getSession(),"Impossibile ottenere il catalogo");
        }
        
        return View.visualizzaUtenti(listaUtenti, req);
    }
    
    /**
     * Permette ad un user di registrarsi
     * 
     * @param req 
     */
    
    public static void getRegistration(HttpServletRequest req){
        req.getSession().setAttribute("view", "register");
    }
        
    /**
     * Reinizializza gli attributi "name" e "view"
     * 
     * @param req 
     */
    
    public static void goBack(HttpServletRequest req){
        req.getSession().setAttribute("view", "");
        req.getSession().setAttribute("name", "");
        notifica(req.getSession(), req.getSession().getAttribute("view")+"");
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
