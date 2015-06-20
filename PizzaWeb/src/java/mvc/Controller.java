package mvc;

import components.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Classe dell'oggetto Pizza
 *
 * @author Alessandro Genovese, Anna Di Leva, Mirko Costantino;
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
          out.println(getCatalogo(request));
          break;
        case "prenotazioni": {
          try {
            user.setView("prenotazioni");
            out.println(getPrenotazioni(request));
          } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
        break;
        case "utenti":
          user.setView("utenti");
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
      }

      out.close();
      return;
    }

    String action = request.getParameter("action");
    notifica(request.getSession(), action);
    if (action != null) {
      switch (action) {
        case "switch":
          switchPage(request);
          break;
        //////////////////////////////////////
        case "register":
          register(request);
          break;
        case "login":
          login(request,user);
          break;
        case "logout":
          logout(request);
          break;
        //////////////////////////////////////

        case "addPrenotazione":
          addPrenotazioni(request);
          aggiornaPagina(request);
          break;
        case "modPrenotazione":
          modPrenotazioni(request);
          aggiornaPagina(request);
          break;
        case "modStatoPrenotazione":
          modStatoPrenotazione(request);
          aggiornaPagina(request);
          break;
        case "remPrenotazione":
          remPrenotazioni(request);
          aggiornaPagina(request);
          break;
        case "modUtente":
          modUtente(request);
          aggiornaPagina(request);
          break;
        case "remUtente":
          remUtente(request);
          aggiornaPagina(request);
          break;

      }

    }

    RequestDispatcher rd;
    //request e non named perchè richiediamo una jsp
    rd = getServletContext().getRequestDispatcher("/index.jsp");
    rd.include(request, response);
    out.close();
  }

  /**
   * Si occupa di visualizzare la pagina richiesta
   *
   * @param req
   */
  public static void switchPage(HttpServletRequest req) {
    String page = req.getParameter("name");
    if (page != null && !page.equals("")) {
      req.getSession().setAttribute("view", page);
    } else {
      req.getSession().setAttribute("view", "");
    }
    aggiornaPagina(req);
  }

  /**
   * Esegue il refresh della pagina
   *
   * @param req
   * @return
   */
  public static String aggiornaPagina(HttpServletRequest req) {
    String page = (String) req.getParameter("action");

    switch (page) {
      case "catalogo":
//        return getCatalogo(req);
        req.getSession().setAttribute("view", "catalogo");
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
        req.getSession().setAttribute("view", "Registrati");
        break;
      case "back":
        goBack(req);
        break;

    }

    return "";
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
    if(checkLogin(req))
      return;
    String username = "" + req.getParameter("username");
    String password = "" + req.getParameter("password");
    try {
      if ( Model.login(username, password)) {
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
          //s.setAttribute("password", password);
          req.getSession().setAttribute("user", user);
          goodMessage(req.getSession(), "Utente Creato con successo");

        } else {
          errorMessage(req.getSession(), "Utente già esistente");
        }

        req.getSession().setAttribute("view", "back");
        aggiornaPagina(req);
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
    
    if(user == null)
      return false;
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
      notifica(req.getSession(), "logout effettuato");
    } else {
      notifica(req.getSession(), "logout impossibile");
    }
  }

  /**
   * Permette di assegnegnare una stringa ad un attributo della sessione
   *
   * @param s
   * @param txt
   */
  public static void notifica(HttpSession s, String txt) {
    s.setAttribute("message", s.getAttribute("message") + "<p>" + txt + "</p>");

  }

  public static void warningMessage(HttpSession s, String txt) {
    UserBean user = (UserBean) s.getAttribute("user");
    if (user != null) {
      user.setMessage("<p class='warning'>" + txt + "</p>");
    }
  }

  public static void goodMessage(HttpSession s, String txt) {
    UserBean user = (UserBean) s.getAttribute("user");
    if (user != null) {
      user.setMessage("<p class='good'>" + txt + "</p>");
    }
  }

  public static void errorMessage(HttpSession s, String txt) {
    UserBean user = (UserBean) s.getAttribute("user");
    if (user != null) {
      user.setMessage("<p class='error'>" + txt + "</p>");
    }
  }

  public static void notificautente(HttpSession s, String txt) {
    s.setAttribute("alert", "<script type='text/javascript'>alert('" + txt + "')</script>");
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
    String username = s.getAttribute("username") + "";
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
        if (nome != null && ingredienti != null && !nome.equals("") && !ingredienti.equals("") && prezzo > 0) {
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
    String username = s.getAttribute("username") + "";
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
              notifica(s, prezzoS + " non double");
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
          notifica(s, "pizza aggiornata");
          return View.getPizzaElement(p);
        } else {
          errorMessage(s, "pizza non trovata");
          notifica(s, "pizza non trovata");
        }
      } else {
        errorMessage(s, "non hai i permessi");
        notifica(s, "non hai i permessi");
      }
    } catch (SQLException e) {
      errorMessage(s, "KABOOM BABY!");
      notifica(s, "???A???");
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
    String username = s.getAttribute("username") + "";
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
  public static void addPrenotazioni(HttpServletRequest req) {
    HttpSession s = req.getSession();
    String username = s.getAttribute("username") + "";
    String nomePizza = req.getParameter("pizza");
    int quantita = -1;
    try {
      quantita = Integer.parseInt(req.getParameter("quantita"));
    } catch (NumberFormatException e) {
      notifica(s, req.getParameter("quantita") + " non int");
    }
    String data = req.getParameter("data");
    try {
      if (Model.getUtente(username).getPermission().equals("user")) {
        int idUser = Model.getIdUtente(username);
        int idPizza = Model.getIdPizza(nomePizza);
        if (Model.getPizza(nomePizza) != null && quantita > 0 && data != null && !data.equals("")) {
          Prenotazione p = new Prenotazione(idUser, idPizza, quantita, data);
          Model.addPrenotazione(p);
          notifica(s, "prenotazione aggiunta");
          notificautente(s, "prenotazione aggiunta");
        } else {
          notifica(s, "prenotazione non aggiunta");
          notificautente(s, "prenotazione non aggiunta");
        }
      } else {
        notifica(s, "non hai i permessi");
        notificautente(s, "non hai i permessi");
      }
    } catch (SQLException e) {
      notifica(s, "???B???");
      notificautente(s, "???B???");
    }
    req.getSession().setAttribute("view", "catalogo");
    aggiornaPagina(req);
  }

  /**
   * Permette ad un user di modificare le sue prenotazioni ed all'admin di
   * modificarle tutte
   *
   * @param req
   */
  public static void modPrenotazioni(HttpServletRequest req) {

    HttpSession s = req.getSession();
    String username = req.getSession().getAttribute("username") + "";
    try {
      Prenotazione p;
      Utente u = Model.getUtente(username);
      int idpren = -1;
      switch (Model.getUtente(username).getPermission()) {
        case "user":
          try {
            idpren = Integer.parseInt(req.getParameter("id"));
          } catch (NumberFormatException e) {
            notifica(s, req.getParameter("id") + " non int");
          }
          p = Model.getPrenotazione(idpren);

          if (p != null) {
            if (p.getIdUtente() == u.getId()) {

              //gestione quantità
              int quantita = -1;
              try {
                quantita = Integer.parseInt(req.getParameter("quantita"));
              } catch (NumberFormatException e) {
                notifica(s, req.getParameter("quantita") + " non int");
              }
              if (quantita > 0) {
                p.setQuantita(quantita);
              }
              //gestione data
              String data = req.getParameter("data");
              if (data != null && !data.equals("")) {
                p.setData(data);
              }

              Model.modPrenotazione(p);

              notificautente(s, "prenotazione aggiornata");
            } else {
              notificautente(s, "prenotazione non tua");
            }
          } else {
            notificautente(s, "prenotazione non trovata");
          }
          break;
        case "admin":
          try {
            idpren = Integer.parseInt(req.getParameter("id"));
          } catch (NumberFormatException e) {
            notifica(s, req.getParameter("id") + " non int");
          }
          p = Model.getPrenotazione(idpren);
          if (p != null) {

            //gestione cliente
            if (req.getParameter("utente") != null) {
              String nome = req.getParameter("utente") + "";
              if (nome.equals("")) {
                notifica(s, "nome vuoto");
              } else {
                int i = Model.getIdUtente(nome);
                if (i > 0) {
                  p.setIdUtente(i);
                  notifica(s, "modificato idNome");
                }
              }
            }

            //gestione pizza
            if (req.getParameter("pizza") != null) {
              int pizza = Model.getIdPizza(req.getParameter("pizza") + "");
              if (pizza > 0) {
                p.setIdPizza(pizza);
                notifica(s, "modificato id");
              }
            }

            //gestione quantità
            if (req.getParameter("quantita") != null) {
              int quantita = -1;
              try {
                quantita = Integer.parseInt(req.getParameter("quantita"));
              } catch (NumberFormatException e) {
                notifica(s, req.getParameter("quantita") + " non int");
              }
              if (quantita > 0) {
                p.setQuantita(quantita);
                notifica(s, "modificata quantità");

              }
            }

            //gestione data
            if (req.getParameter("data") != null) {
              String data = req.getParameter("data");
              if (data != null && !data.equals("")) {
                p.setData(data);
                notifica(s, "modificata data");
              }
            }

            //gestione stato
            if (req.getParameter("stato") != null) {
              String stato = req.getParameter("stato");
              if (stato != null && !stato.equals("")) {
                p.setStato(stato);
                notifica(s, "modificato stato");
              }
            }

            Model.modStatoPrenotazione(p);
            Model.modPrenotazione(p);
            notifica(s, "prenotazione aggiornata");
          } else {
            notifica(s, "prenotazione non trovata");
          }
          break;
        default:

          notificautente(s, "non hai i permessi");
          break;
      }
    } catch (SQLException e) {
      notifica(s, "???B???");
    }
    req.getSession().setAttribute("view", "prenotazioni");
    aggiornaPagina(req);
  }

  public static void modStatoPrenotazione(HttpServletRequest req) {

    HttpSession s = req.getSession();
    String username = req.getSession().getAttribute("username") + "";
    try {
      Prenotazione p;
      Utente u = Model.getUtente(username);
      int idpren = -1;
      switch (Model.getUtente(username).getPermission()) {
        case "user":
          try {
            idpren = Integer.parseInt(req.getParameter("id"));
          } catch (NumberFormatException e) {
            notifica(s, req.getParameter("id") + " non int");
          }
          p = Model.getPrenotazione(idpren);
          if (p.getIdUtente() == u.getId()) {
            p.setStato("Consegnato");
            Model.modStatoPrenotazione(p);
            notificautente(s, "prenotazione aggiornata");
          } else {
            notificautente(s, "prenotazione non tua");
          }
          break;
        case "admin":
          try {
            idpren = Integer.parseInt(req.getParameter("id"));
          } catch (NumberFormatException e) {
            notifica(s, req.getParameter("id") + " non int");
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
            notifica(s, "prenotazione aggiornata");
          } else {
            notifica(s, "prenotazione non trovata");
          }
          break;
        default:
          notifica(s, "non hai i permessi");
          break;
      }
    } catch (SQLException e) {
      notifica(s, "???B???");
    }
    req.getSession().setAttribute("view", "prenotazioni");
    aggiornaPagina(req);
  }

  /**
   * Permette ad un user di rimuovere le sue prenotazioni
   *
   * @param req
   */
  public static void remPrenotazioni(HttpServletRequest req) {

    HttpSession s = req.getSession();
    String username = req.getSession().getAttribute("username") + "";
    Prenotazione p;
    int idpren = -1;
    try {
      switch (Model.getUtente(username).getPermission()) {
        case "user":
          try {
            idpren = Integer.parseInt(req.getParameter("prenotazione"));
          } catch (NumberFormatException e) {
            notifica(s, req.getParameter("prenotazione") + " non int");
          }
          p = Model.getPrenotazione(idpren);
          if (p != null) {
            if (p.getIdUtente() == Model.getIdUtente(username)) {
              Model.remPrenotazione(p);
              notificautente(s, "prenotazione rimossa");
            } else {
              notificautente(s, "prenotazione non tua");
            }

          } else {
            notificautente(s, "prenotazione non trovata");
          }
          break;
        case "admin":
          try {
            idpren = Integer.parseInt(req.getParameter("prenotazione"));
          } catch (NumberFormatException e) {
            notifica(s, req.getParameter("prenotazione") + " non int");
          }
          p = Model.getPrenotazione(idpren);
          if (p != null) {
            Model.remPrenotazione(p);
            notifica(s, "prenotazione rimossa");
          } else {
            notifica(s, "prenotazione non trovata");
          }
          break;
        default:
          notifica(s, "non hai i permessi");
          break;
      }
    } catch (SQLException e) {
      notifica(s, "???B???");
    }
    req.getSession().setAttribute("view", "prenotazioni");
    aggiornaPagina(req);
  }

///////////////////////////////////////////////////////////////////////////////////////////
// METODI SU UTENTI
  /**
   * Modifica user, password o permessi
   *
   * @param req
   */
  public static void modUtente(HttpServletRequest req) {
    HttpSession s = req.getSession();
    String user = req.getSession().getAttribute("username") + "";
    try {
      Utente u = Model.getUtente(user);
      switch (u.getPermission()) {
        case "admin":
          req.getParameter("id");
          String username = req.getParameter("id") + "";
          u = Model.getUtente(username);
          ///modifica utente

          String name = req.getParameter("name") + "";
          if (name != null && !name.equals("")) {
            u.setUsername(name);
          }

          ///modifica password
          String password = req.getParameter("password") + "";
          if (password != null && !password.equals("")) {
            u.setPwd(password);
          }

          ///modifica permission
          String permission = req.getParameter("permission");
          if (permission != null && !permission.equals("")) {
            u.setPermission(permission);
          }

          notifica(s, u.getUsername() + u.getPassword() + u.getPermission() + "");

          Model.modUtente(username, u);
          notifica(s, "utente aggiornato");
          break;
        default:
          notificautente(s, "non hai i permessi");
          notifica(s, "non hai i permessi");
          break;
      }
    } catch (SQLException e) {
      notifica(s, e.getMessage());
    }
    req.getSession().setAttribute("view", "utenti");
    aggiornaPagina(req);
  }

  /**
   * Permette ad un admin di rimuovere un utente
   *
   * @param req
   */
  public static void remUtente(HttpServletRequest req) {

    HttpSession s = req.getSession();
    String username = req.getSession().getAttribute("username") + "";
    int idUtente = -1;
    try {
      idUtente = Integer.parseInt(req.getParameter("id"));
    } catch (NumberFormatException e) {
      notifica(s, req.getParameter("id") + " non int");
    }
    try {
      switch (Model.getUtente(username).getPermission()) {
        case "admin":

          Utente u = Model.getUtente(idUtente);
          if (u != null) {
            Model.remUtente(u);
            notifica(s, "utente rimosso");
          } else {
            notifica(s, "utente non trovato");
          }
          break;
        default:
          notificautente(s, "non hai i permessi");
          notifica(s, "non hai i permessi");
          break;
      }
    } catch (SQLException e) {
      notifica(s, "???B???");
    }
    req.getSession().setAttribute("view", "utenti");
    aggiornaPagina(req);
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
    String username = req.getSession().getAttribute("username") + "";
    ArrayList<Pizza> listaPizze = null;
    Utente u = null;

    try {
      u = Model.getUtente(username);

      listaPizze = Model.getCatalogo();
      notifica(s, "catalogo ottenuto");
    } catch (SQLException e) {
      notificautente(s, "Impossibile ottenere il catalogo");
      notifica(s, "Impossibile ottenere il catalogo");
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
    String username = req.getSession().getAttribute("username") + "";
    ArrayList<Prenotazione> listaPrenotazioni = null;

    Utente u = null;

    try {
      listaPrenotazioni = new ArrayList();
      u = Model.getUtente(username);

      if (u != null && u.getPermission().equals("user")) {
        listaPrenotazioni = Model.getListaPrenotazioni(u.getId());
        //notificautente(req.getSession(), "Caricate le tue Prenotazioni");

      } else if (u != null && u.getPermission().equals("admin")) {
        listaPrenotazioni = Model.getListaPrenotazioni();
        notifica(req.getSession(), "Caricate Tutte Prenotazioni");
      } else {
        notifica(req.getSession(), "non hai i permessi");
        notificautente(req.getSession(), "non hai i permessi");
      }
    } catch (SQLException e) {
      notifica(req.getSession(), "Impossibile ottenere il catalogo");
      notificautente(req.getSession(), "Impossibile ottenere il catalogo");
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
    String username = req.getSession().getAttribute("username") + "";
    ArrayList<Utente> listaUtenti = null;

    try {
      listaUtenti = new ArrayList();
      Utente u = Model.getUtente(username);

      if (u != null && u.getPermission().equals("admin")) {
        listaUtenti = Model.getListaUtenti(u.getId());
        notifica(req.getSession(), "Caricati tutti gli utenti");
      } else {
        notifica(req.getSession(), "non hai i permessi");
      }
    } catch (SQLException e) {
      notifica(req.getSession(), "Impossibile ottenere il catalogo");
      notificautente(req.getSession(), "Impossibile ottenere il catalogo");
    }

    return View.visualizzaUtenti(listaUtenti, req);
  }

  /**
   * Permette ad un user di registrarsi
   *
   * @param req
   */
  public static void getRegistration(HttpServletRequest req) {
//    req.getSession().setAttribute("view", "register");
  }

  /**
   * Reinizializza gli attributi "name" e "view"
   *
   * @param req
   */
  public static void goBack(HttpServletRequest req) {
    req.getSession().setAttribute("view", "");
    req.getSession().setAttribute("name", "");
    notifica(req.getSession(), req.getSession().getAttribute("view") + "");
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
