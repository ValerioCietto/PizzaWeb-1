package mvc;

import components.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
/*
 La libreria JSON la si può recuperare su internet, ma in questo progetto
 è stata posizionata nella directory Libraries del progetto
 */
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe dell'oggetto Pizza
 *
 * @author Alessandro Genovese, Anna Di Leva, Mirko Costantino, Giuseppe
 * Mammolo;
 */
@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})

public class Controller extends HttpServlet {
    
////////////////////////////////////////////////////////////////////////////////
// GESTIONE DELLE PAGINE
    /**
     * Gestisce le richieste provenienti dalle pagine jsp
     *
     * @param request
     * @param response
     *
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        //in quest'occasione inizializza user e modifica automaticamente anche il session
        if (user == null) {
            user = new UserBean();
        }

        String page = request.getParameter("view");
        if (page != null) {
            switch (page) {
                case "catalogo":
                    user.setView("catalogo");
                    out.println("<script src='js/catalogo.js'></script>");
                    out.println("<script src='js/prenotazione.js'></script>");
                    out.println(getCatalogo(request));
                    break;
                case "prenotazioni": {
                    try {
                        user.setView("prenotazioni");
                        out.println("<script src='js/prenotazione.js'></script>");
                        out.println(getPrenotazioni(request));
                    } catch (SQLException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "utenti":
                    user.setView("utenti");
                    out.println("<script src='js/userList.js'></script>");
                    out.println(getUtenti(request));
                    break;
                default:
                    out.print("Pagina non trovata");
                    break;
            }
            out.close();
            return;
        }

        String ajaxRequest = request.getParameter("ajaxAction");
        if (ajaxRequest != null) {
            switch (ajaxRequest) {
                case "modPizza":
                    out.print(modPizza(request));
                    break;
                case "addPizza":
                    out.print(addPizza(request));
                    break;
                case "remPizza":
                    out.print(remPizza(request));
                    break;
                case "modPrenotazione":
                    out.println(modPrenotazioni(request));
                    break;
                case "modStatoPrenotazione":
                    out.println(modStatoPrenotazione(request));
                    break;
                case "remPrenotazione":
                    out.print(remPrenotazioni(request));
                    break;
                case "modUtente":
                    out.print(modUtente(request));
                    break;
                case "remUtente":
                    out.print(remUtente(request));
                    break;
            }
            out.close();
            return;
        }
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                //////////////////////////////////////
                case "register":
                    register(request);
                    break;
                case "login":
                    login(request, user);
                    break;
                case "logout":
                    logout(request);
                    break;
                //////////////////////////////////////
                case "addPrenotazione":
                    addPrenotazioni(request, out);
                    break;
            }

        }
        RequestDispatcher rd;
        //request e non named perchè richiediamo una jsp
        rd = getServletContext().getRequestDispatcher("/index.jsp");
        rd.include(request, response);
        out.close();
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI SESSIONE
    /**
     * Gestisce il login
     *
     * @param req
     * @param user
     */
    public static void login(HttpServletRequest req, UserBean user) {
        HttpSession s = req.getSession();
        if (checkLogin(req)) {
            return;
        }
        String username = "" + req.getParameter("username");
        String password = "" + req.getParameter("password");
        try {
            if (Model.login(username, password)) {
                user.setUsername(username);
                user.setPassword(password);
                goodMessage(s, "Login ok!");
            } else {
                errorMessage(s, "error: Assicurati che Username e Password siano esistenti e corretti");
            }
        } catch (SQLException ex) {
            errorMessage(s, "error: E' Esploso il mondo");
        }
    }

    /**
     * Gestisce la registrazione
     *
     * @param req
     */
    public static void register(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        try {
            if (password1.equals(password2)) {
                if (Model.creaUtente(username, password1)) {
                    UserBean user = new UserBean();
                    user.setUsername(username);
                    req.getSession().setAttribute("user", user);
                    goodMessage(req.getSession(), "Utente Creato con successo");
                } else {
                    errorMessage(req.getSession(), "Utente già esistente");
                }
                req.getSession().setAttribute("view", "back");
            } else {
                errorMessage(req.getSession(), "password diverse");
            }
        } catch (SQLException e) {
            errorMessage(req.getSession(), "Registrazione fallita!");
        }
    }

    /**
     * Controlla se è stato effettuato il login
     *
     * @param req
     *
     * @return
     */
    public static boolean checkLogin(HttpServletRequest req) {
        UserBean user = (UserBean) req.getSession().getAttribute("user");

        if (user == null) {
            return false;
        }
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            if (Model.login(username, password)) {
                return true;
            }
        } catch (SQLException e) {
            errorMessage(req.getSession(), "SQL error!");
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
    public static boolean checkAdmin(HttpServletRequest req) {

        UserBean user = (UserBean) req.getSession().getAttribute("user");
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            if (Model.checkLogin(username, password).getPermission().equals("admin")) {
                return true;
            }
        } catch (SQLException e) {
            errorMessage(req.getSession(), "SQL error!");
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
        if (checkLogin(req)) {
            s.invalidate();
            goodMessage(req.getSession(), "logout effettuato");
        } else {
            errorMessage(req.getSession(), "logout impossibile");
        }
    }

    /**
     * Stampa messaggio di warning
     *
     * @param s
     * @param txt
     */
    public static void warningMessage(HttpSession s, String txt) {
        UserBean user = (UserBean) s.getAttribute("user");
        if (user != null) {
            user.setMessage("<p class='warning'>" + txt + "</p>");
        }
    }

    /**
     * Stampa messaggio di successo
     *
     * @param s
     * @param txt
     */
    public static void goodMessage(HttpSession s, String txt) {
        UserBean user = (UserBean) s.getAttribute("user");
        if (user != null) {
            user.setMessage("<p class='good'>" + txt + "</p>");
        }
    }

    /**
     * Stampa messaggio di errore
     *
     * @param s
     * @param txt
     */
    public static void errorMessage(HttpSession s, String txt) {
        UserBean user = (UserBean) s.getAttribute("user");
        if (user != null) {
            user.setMessage("<p class='error'>" + txt + "</p>");
        }
    }

////////////////////////////////////////////////////////////////////////////////
// METODI SU CATALOGO   
    // Solo visualizzazione
    /**
     * Permette all'admin di aggiungere una pizza
     *
     * @param req
     */
    public static String addPizza(HttpServletRequest req) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        try {
            if (Model.getUtente(username).getPermission().equals("admin")) {
                String nome = req.getParameter("pizza");
                String ingredienti = req.getParameter("ingredienti");
                String prezzoS = req.getParameter("prezzo");
                prezzoS = prezzoS.replaceAll(",", ".");
                double prezzo = -1;
                try {
                    prezzo = Double.parseDouble(prezzoS);
                } catch (NumberFormatException e) {
                    errorMessage(s, prezzoS + " non double");
                    return null;
                }
                if (nome != null && Model.getPizza(nome) == null && ingredienti != null && !nome.equals("") && !ingredienti.equals("") && prezzo > 0) {
                    Pizza p = new Pizza(nome, ingredienti, prezzo);
                    Model.addPizza(p);
                    goodMessage(s, "pizza aggiunta");
                    return View.getAdminPizza(p);
                } else {
                    errorMessage(s, "pizza non aggiunta");
                }
            } else {
                errorMessage(s, "non hai i permessi");
            }
        } catch (SQLException e) {
            errorMessage(s, "???A???");
        }
        return null;
    }

    /**
     * Permette all'admin di modificare una pizza
     *
     * @param req
     * @return
     */
    public static String modPizza(HttpServletRequest req) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        try {
            if (Model.getUtente(username).getPermission().equals("admin")) {
                Pizza p = Model.getPizza(req.getParameter("pizza"));
                if (p != null) {
                    //gestione prezzo
                    String prezzoS = req.getParameter("prezzo") + "";
                    prezzoS = prezzoS.replaceAll(",", ".");
                    if (!prezzoS.equals("")) {
                        double prezzo = -1;
                        try {
                            prezzo = Double.parseDouble(prezzoS);
                        } catch (NumberFormatException e) {
                            errorMessage(s, prezzoS + " non double");
                            return null;
                        }
                        if (prezzo > 0) {
                            p.setPrezzo(prezzo);
                        }
                    }
                    //gestione ingrediente
                    String ingredienti = req.getParameter("ingredienti");
                    if (ingredienti != null && !ingredienti.equals("")) {
                        p.setIngredienti(ingredienti);
                    }
                    //applica modifiche
                    Model.modPizza(p);
                    goodMessage(s, "Pizza Aggiornata");
                    return View.getPizzaElement(p);
                } else {
                    errorMessage(s, "pizza non trovata");
                }
            } else {
                errorMessage(s, "non hai i permessi");
            }
        } catch (SQLException e) {
            errorMessage(s, "KABOOM BABY!");
        }
        return null;
    }

    /**
     * Permette all'admin di rimuovere una pizza
     *
     * @param req
     */
    public static String remPizza(HttpServletRequest req) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        try {
            if (Model.getUtente(username).getPermission().equals("admin")) {
                Pizza p = Model.getPizza(req.getParameter("pizza"));
                if (p != null) {
                    Model.remPizza(p);
                    goodMessage(s, "pizza rimossa");
                    return "";
                } else {
                    errorMessage(s, "pizza non trovata");
                }
            } else {
                errorMessage(s, "non hai i permessi");
            }
        } catch (SQLException e) {
            errorMessage(s, "???B???");
        }
        return null;
    }

////////////////////////////////////////////////////////////////////////////////
// METODI SU PRENOTAZIONI
    /**
     * Permette ad un user di aggiungere una prenotazione
     *
     * @param req
     */
    public static void addPrenotazioni(HttpServletRequest req, PrintWriter out) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        JSONArray jarr = null;
        HashMap<String, Integer> carrello = new HashMap<>();
        String data = req.getParameter("data").replace("T", " ");
        try {
            jarr = new JSONArray(req.getParameter("lista"));
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject obj = jarr.getJSONObject(i);
                String pizza = obj.getString("pizza");
                Integer qt = obj.getInt("quantita");
                if (carrello.containsKey(pizza)) {
                    qt += carrello.get(pizza);
                }
                carrello.put(pizza, qt);
            }
            Iterator<String> it = carrello.keySet().iterator();
            while (it.hasNext()) {
                String pizza = it.next();
                Integer qt = carrello.get(pizza);
                if (Model.getUtente(username).getPermission().equals("user")) {
                    int idUser = Model.getIdUtente(username);
                    int idPizza = Model.getIdPizza(pizza);
                    if (Model.getPizza(pizza) != null && qt > 0 && data != null && !data.equals("")) {
                        Prenotazione p = new Prenotazione(idUser, idPizza, qt, data);
                        Model.addPrenotazione(p);
                    } else {
                        errorMessage(s, "prenotazione non aggiunta");
                    }
                }
            }
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Permette ad un user di modificare le sue prenotazioni ed all'admin di
     * modificarle tutte
     *
     * @param req
     */
    public static String modPrenotazioni(HttpServletRequest req) {

        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        try {
            Prenotazione p;
            Utente u = Model.getUtente(username);
            int idpren = -1;
            switch (Model.getUtente(username).getPermission()) {
                case "user":
                    try {
                        idpren = Integer.parseInt(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        errorMessage(s, req.getParameter("id") + " non int");
                    }
                    p = Model.getPrenotazione(idpren);
                    if (p != null) {
                        if (p.getIdUtente() == u.getId()) {
                            //gestione quantità
                            int quantita = -1;
                            try {
                                quantita = Integer.parseInt(req.getParameter("quantita"));
                            } catch (NumberFormatException e) {
                                errorMessage(s, req.getParameter("quantita") + " non int");
                            }
                            if (quantita > 0) {
                                p.setQuantita(quantita);
                            }
                            //gestione data
                            String data = req.getParameter("data").replace("T", " ");
                            if (data != null && !data.equals("")) {
                                p.setData(data);
                            }
                            Model.modPrenotazione(p);
                            goodMessage(s, "prenotazione aggiornata");
                            return View.getPrenotazioneUserView(p);
                        } else {
                            errorMessage(s, "prenotazione non tua");
                        }
                    } else {
                        errorMessage(s, "prenotazione non trovata");
                    }
                    break;
                case "admin":
                    try {
                        idpren = Integer.parseInt(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        errorMessage(s, req.getParameter("id") + " non int");
                    }
                    p = Model.getPrenotazione(idpren);
                    if (p != null) {
                        //gestione cliente
                        if (req.getParameter("nome_utente") != null) {
                            String nome = req.getParameter("nome_utente") + "";
                            if (!nome.equals("")) {
                                int i = Model.getIdUtente(nome);
                                if (i > 0) {
                                    p.setIdUtente(i);
                                    goodMessage(s, "modificato idNome");
                                } else {
                                    errorMessage(s, "Utente non trovato");
                                }
                            }
                        }
                        //gestione pizza
                        if (req.getParameter("pizza") != null && !req.getParameter("pizza").equals("")) {
                            int pizza = Model.getIdPizza(req.getParameter("pizza") + "");
                            if (pizza > 0) {
                                p.setIdPizza(pizza);
                                goodMessage(s, "modificato pizza");
                            } else {
                                errorMessage(s, "Pizza non trovata nel menu");
                            }
                        }
                        //gestione quantità
                        if (req.getParameter("quantita") != null) {
                            int quantita = -1;
                            try {
                                quantita = Integer.parseInt(req.getParameter("quantita"));
                            } catch (NumberFormatException e) {
                                errorMessage(s, req.getParameter("quantita") + " non int");
                            }
                            if (quantita > 0) {
                                p.setQuantita(quantita);
                                goodMessage(s, "modificata quantità");
                            }
                        }
                        //gestione data
                        if (req.getParameter("data") != null) {
                            String data = req.getParameter("data").replace("T", " ");
                            if (data != null && !data.equals("")) {
                                p.setData(data);
                                goodMessage(s, "modificata data");
                            }
                        }
                        //gestione stato
                        if (req.getParameter("stato") != null) {
                            String stato = req.getParameter("stato");
                            if (stato != null && !stato.equals("")) {
                                p.setStato(stato);
                                goodMessage(s, "modificato stato");
                            }
                        }
                        Model.modStatoPrenotazione(p);
                        Model.modPrenotazione(p);
                        goodMessage(s, "prenotazione aggiornata");
                        return View.getPrenotazioneAdminView(p);
                    } else {
                        errorMessage(s, "prenotazione non trovata");
                    }
                    break;
                default:
                    errorMessage(s, "non hai i permessi in generale!");
                    break;
            }
        } catch (SQLException e) {
            errorMessage(s, "???B???");
        }
        return "";
    }

    public static String modStatoPrenotazione(HttpServletRequest req) {

        HttpSession s = req.getSession();
        UserBean bean = (UserBean) s.getAttribute("user");
        String username = bean.getUsername();
        try {
            Prenotazione p;
            Utente u = Model.getUtente(username);
            int idpren = -1;
            switch (Model.getUtente(username).getPermission()) {
                case "user":
                    try {
                        idpren = Integer.parseInt(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        errorMessage(s, req.getParameter("id") + " non int");
                    }
                    p = Model.getPrenotazione(idpren);
                    if (p.getIdUtente() == u.getId()) {
                        p.setStato("Consegnato");
                        Model.modStatoPrenotazione(p);
                        goodMessage(s, "prenotazione aggiornata");
                        return View.getPrenotazioneUserView(p);
                    } else {
                        errorMessage(s, "prenotazione non tua");
                    }
                    break;
                case "admin":
                    try {
                        idpren = Integer.parseInt(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        errorMessage(s, req.getParameter("id") + " non int");
                    }
                    p = Model.getPrenotazione(idpren);
                    if (p != null) {

                        //gestione stato
                        if (req.getParameter("stato") != null) {
                            String stato = req.getParameter("stato");
                            if (stato != null && !stato.equals("")) {
                                p.setStato(stato);
                            }
                        }
                        Model.modStatoPrenotazione(p);
                        goodMessage(s, "prenotazione aggiornata");
                        return View.getPrenotazioneAdminView(p);
                    } else {
                        errorMessage(s, "prenotazione non trovata");
                    }
                    break;
                default:
                    warningMessage(s, "non hai i permessi");
                    break;
            }
        } catch (SQLException e) {
            errorMessage(s, "???B???");
        }
        return "";
    }

    /**
     * Permette ad un user di rimuovere le sue prenotazioni
     *
     * @param req
     */
    public static String remPrenotazioni(HttpServletRequest req) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        Prenotazione p;
        int idpren = -1;
        try {
            switch (Model.getUtente(username).getPermission()) {
                case "user":
                    try {
                        idpren = Integer.parseInt(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        errorMessage(s, req.getParameter("id") + " non int");
                    }
                    p = Model.getPrenotazione(idpren);
                    if (p != null) {
                        if (p.getIdUtente() == Model.getIdUtente(username)) {
                            Model.remPrenotazione(p);
                            goodMessage(s, "prenotazione rimossa");
                            return "";
                        } else {
                            errorMessage(s, "prenotazione non tua");
                        }

                    } else {
                        errorMessage(s, "prenotazione non trovata");
                    }
                    break;
                case "admin":
                    try {
                        idpren = Integer.parseInt(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        errorMessage(s, req.getParameter("id") + " non int");
                    }
                    p = Model.getPrenotazione(idpren);
                    if (p != null) {
                        Model.remPrenotazione(p);
                        goodMessage(s, "prenotazione rimossa");
                        return "";
                    } else {
                        errorMessage(s, "prenotazione non trovata");
                    }
                    break;
                default:
                    warningMessage(s, "non hai i permessi per farlo");
                    break;
            }
        } catch (SQLException e) {
            errorMessage(s, "???B???");
        }
        return "null";
    }

///////////////////////////////////////////////////////////////////////////////////////////
// METODI SU UTENTI
    /**
     * Modifica user, password o permessi
     *
     * @param req
     */
    public static String modUtente(HttpServletRequest req) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        try {
            Utente u = Model.getUtente(username);
            switch (u.getPermission()) {
                case "admin":
                    int idUtente = -1;
                    try {
                        idUtente = Integer.parseInt(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        errorMessage(s, req.getParameter("id") + " non int");
                    }
                    Utente modu = Model.getUtente(idUtente);
                    ///modifica utente
                    String moduser_name = modu.getUsername();
                    String name = req.getParameter("name") + "";
                    if (name != null && !name.equals("")) {
                        modu.setUsername(name);
                    }
                    ///modifica password
                    String password = req.getParameter("password") + "";
                    if (password != null && !password.equals("")) {
                        modu.setPwd(password);
                    }
                    ///modifica permission
                    String permission = req.getParameter("rule");
                    if (permission != null && !permission.equals("")) {
                        modu.setPermission(permission);
                    }
                    Model.modUtente(moduser_name, modu);
                    goodMessage(s, "utente aggiornato");
                    return View.getUtenteElement(modu);
                default:
                    errorMessage(s, "non hai i permessi per modificare gli utenti");
                    break;
            }
        } catch (SQLException e) {
            errorMessage(s, e.getMessage());
        }
        return "";
    }

    /**
     * Permette ad un admin di rimuovere un utente
     *
     * @param req
     */
    public static String remUtente(HttpServletRequest req) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        String username = user.getUsername();
        int idUtente = -1;
        try {
            idUtente = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            errorMessage(s, req.getParameter("id") + " non int");
        }
        try {
            if ("admin".equals(Model.getUtente(username).getPermission())) {
                Utente u = Model.getUtente(idUtente);
                if (u != null) {
                    Model.remUtente(u);
                    goodMessage(s, "utente rimosso");
                    return "";
                } else {
                    errorMessage(s, "utente non trovato");
                }
            } else {
                errorMessage(s, "non hai i permessi per rimuovere un utente");
            }
        } catch (SQLException e) {
            errorMessage(s, "???B???");
        }
        return "null";
    }

///////////////////////////////////////////////////////////////////////////////////////////
// METODI DI GET
    /**
     * Permette a TUTTI di visualizzare il catalogo pizze
     *
     * @param req
     * @return
     */
    public static String getCatalogo(HttpServletRequest req) {
        HttpSession s = req.getSession();
        UserBean user = (UserBean) s.getAttribute("user");
        ArrayList<Pizza> listaPizze = null;
        Utente u = null;
        try {
            u = Model.getUtente(user.getUsername());
            listaPizze = Model.getCatalogo();
            goodMessage(s, "catalogo ottenuto");
        } catch (SQLException e) {
            errorMessage(s, "Impossibile ottenere il catalogo");
        }
        return View.visualizzaCatalogo(listaPizze, u, req);
    }

    /**
     * Permette ad un user di visualizzare le sue prenotazioni ed all'admin di
     * visualizzarle tutte
     *
     * @param req
     * @return
     * @throws java.sql.SQLException
     */
    public static String getPrenotazioni(HttpServletRequest req) throws SQLException {
        UserBean user = (UserBean) req.getSession().getAttribute("user");
        String username = user.getUsername();
        ArrayList<Prenotazione> listaPrenotazioni = null;
        Utente u = null;
        try {
            listaPrenotazioni = new ArrayList();
            u = Model.getUtente(username);
            if (u != null && u.getPermission().equals("user")) {
                listaPrenotazioni = Model.getListaPrenotazioni(u.getId());
                goodMessage(req.getSession(), "Caricate le tue Prenotazioni");

            } else if (u != null && u.getPermission().equals("admin")) {
                listaPrenotazioni = Model.getListaPrenotazioni();
                goodMessage(req.getSession(), "Caricate Tutte Prenotazioni");
            } else {
                errorMessage(req.getSession(), "non hai i permessi per vedere le prenotazioni");
            }
        } catch (SQLException e) {
            errorMessage(req.getSession(), "Impossibile ottenere il catalogo");
        }
        return View.visualizzaPrenotazioni(listaPrenotazioni, u, req);
    }

    /**
     * Permette ad un user di visualizzare le sue prenotazioni ed all'admin di
     * visualizzarle tutte
     *
     * @param req
     * @return
     */
    public static String getUtenti(HttpServletRequest req) {
        UserBean user = (UserBean) req.getSession().getAttribute("user");
        String username = user.getUsername();
        ArrayList<Utente> listaUtenti = null;
        try {
            listaUtenti = new ArrayList();
            Utente u = Model.getUtente(username);

            if (u != null && u.getPermission().equals("admin")) {
                listaUtenti = Model.getListaUtenti(u.getId());
                goodMessage(req.getSession(), "Caricati tutti gli utenti");
            } else {
                errorMessage(req.getSession(), "non hai i permessi per vedere la lista utenti");
            }
        } catch (SQLException e) {
            errorMessage(req.getSession(), "Impossibile ottenere il catalogo");
        }
        return View.visualizzaUtenti(listaUtenti, req);
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// NON MODIFICARE    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
