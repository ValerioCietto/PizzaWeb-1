package mvc;

/**
 * Classe dell'oggetto Pizza
 *
 * @author Alessandro Genovese, Anna Di Leva, Mirko Costantino;
 */
////////////////////////////////////////////////////////////////////////////////
import components.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.*;

public class View {

    /**
     * Ritorna una stringa che fornisce i dati per costruire l'article
     *
     * @param al
     * @param u
     * @param req
     *
     * @return
     */
    public static String visualizzaCatalogo(ArrayList<Pizza> al, Utente u, HttpServletRequest req) {
        String html = "";
        if (al == null) {
            return visualizzaFallimento(req);
        } else if (al.size() == 0) {
            html += "<div>Non ci sono Pizze in catalogo</div>";
        } else {
            if (u == null) {
                for (Pizza al1 : al) {
                    html += "<div class='pizza'>";
                    html += getPizzaElement(al1);
                    html += "</div>";
                }
            } else if (u.getPermission().equals("user")) {

                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<div class='pren_pizza_list'>";
                html += getSelectablePizzaSlot();
                html += "<div class='slot_add_pizza'></div>";
                html += "</div>";
                html += "<input type='button' name='addPizza' value ='Aggiungi Pizza' onclick='Prenotazione.addPizza()'/>";
                html += "<div class='data_consegna'> Consegna: <input type ='date' name='data' required pattern='/(0000-00-00 00:00)|((20\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|[12][0-4]):([0-5][0-9]))$/' placeholder=\"AAAA-MM-GG hh:mm\"  title=\"orario prenotazione: AAAA-MM-GG hh:mm\"/></div>";
                html += "<!--<script>$('input[name=\"data\"]').val(new Date().toISOString().substring(0, 16))</script>-->";
                html += "<input type='button' value ='prenota' onclick='Prenotazione.addPrenotazione()'/>";
                html += "</form>";

            } else if (u.getPermission().equals("admin")) {

                html += "<div class= 'new'>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<span class='nome'>Nome:  </span>";
                html += "<input type ='text' name='nome'>";
                html += "<span class='ingredienti'> Ingredienti: </span>";
                html += "<input type ='text' name='ingredienti'>";
                html += "<span class='prezzo'> Prezzo: </span>";
                html += "<input type='double' name='prezzo'></br>";
                html += "<input type='hidden' name='action' value='addPizza'>";
                html += "<input type='button' value='Aggiungi' onclick='Catalogo.aggiungiPizza(this)'>";
                html += "</form>";
                html += "</div>";

                for (Pizza al1 : al) {
                    html += getAdminPizza(al1);
                }
            }
        }

        return html;
    }

    public static String getSelectablePizzaSlot() {
        String s = "";
        ArrayList<Pizza> listapizze;
        try {
            listapizze = Model.getListaPizze();
        } catch (SQLException ex) {
            listapizze = new ArrayList<>();
        }
        s += "<div class='pren_pizza'>";
        s += " <select>\n";
        for (Pizza p : listapizze) {
            s += "\t <option value=\"" + p.getNome() + "\">" + p.getNome() + "</option>\n";
        }
        s += "</select> ";
        s += "<span>Quantità:  </span>";
        s += "<input type ='number' name='quantita' min='0' max='100'value = '0' required>";
        s += "</div>";
        return s;
    }

    /**
     * Restituisce un nuovo div quando viene aggiunta una pizza ATTENZIONE:
     * Metodo riservato all' inserimento della pizza da parte dell' admin con
     * tanto di form;
     *
     * @param al1
     * @return
     */
    protected static String getAdminPizza(Pizza al1) {
        String html = "";
        html += "<div class='pizza'>";
        html += getPizzaElement(al1);
        html += "<span>";
        html += "<form action='/PizzaWeb/Servlet' method='post' >";
        html += "<span>Ingredienti:  </span>";
        html += "<input type ='text' name='ingredienti'>";
        html += "<span></br>Prezzo:  </span>";
        html += "<input type ='text' name='prezzo'></br>";
        html += "<input type='hidden' name='pizza' value='" + al1.getNome() + "'>";
        html += "<input type='hidden' name='action' value='modPizza'>";
        html += "<input type='button' value='Modifica' onclick='Catalogo.modificaPizza(this)'>";
        html += "</form>";
        html += "<form action='/PizzaWeb/Servlet' method='post' >";
        html += "<input type='hidden' name='pizza' value='" + al1.getNome() + "'>";
        html += "<input type='button' value='Rimuovi' onclick='Catalogo.rimuoviPizza(this)'>";
        html += "</form>";
        html += "</span>";
        html += "</div>";
        return html;
    }

    /**
     * Restituisce l'html della pizza nel catalogo, contente solo le
     * informazioni basilari
     *
     * @param al1
     * @return
     */
    protected static String getPizzaElement(Pizza al1) {
        String html = "";
        html += "<div class='element'>";
        html += "<div class='row'>"
                + "<span class='nome'>" + al1.getNome() + "</span>";
        html += "<div class='prezzo_block'>"
                + "<span>Prezzo:  </span>";
        html += "<span class='prezzo'>" + al1.getPrezzo() + "</span>"
                + "</div>"
                + "</div>";
        html += "<div class='row ingredienti_block'><span>Ingredienti:  </span>";
        html += "<span class='ingredienti'>" + al1.getIngredinti() + "</span></div>"
                + "</div>";
        return html;
    }

    /**
     * Ritorna una stringa che fornisce i dati per costruire l'article
     *
     * @param al
     * @param u
     * @param req
     *
     * @return
     * @throws java.sql.SQLException
     */
    public static String visualizzaPrenotazioni(ArrayList<Prenotazione> al, Utente u, HttpServletRequest req) throws SQLException {
        String html = "";
        if (al == null) {
            return visualizzaFallimento(req);
        } else if (al.size() == 0) {
            html += "<div>Non ci sono Prenotazioni in Programma</div>";
        } else if (u.getPermission().equals("user")) {
            for (Prenotazione al1 : al) {
                html += getPrenotazioneUserView(al1);
            }
        } else if (u.getPermission().equals("admin")) {
            for (Prenotazione al1 : al) {
                html += getPrenotazioneAdminView(al1);
            }

        }
        return html;
    }

    public static String getPrenotazioneUserView(Prenotazione al1) throws SQLException {
        String html = "";
        html += "<div class='prenotazione'>";
        html += "<span>Pizza:  </span>";
        html += "<span class='pizza'>" + Model.getPizza(al1.getIdPizza()).getNome() + "</span>";
        html += "<span></br>Quantità:  </span>";
        html += "<span class='quantità'>" + al1.getQuantita() + "</span>";
        html += "<span></br>Data:  </span>";
        html += "<span class='data'>" + al1.getData() + "</span>";
        html += "<span></br>Stato:  </span>";
        html += "<span class='stato'>" + al1.getStato() + "</span>";
        if (!al1.getStato().equals("Consegnato")) {
            html += "<form action='/PizzaWeb/Servlet' method='post' >";
            html += "<span>Quantità:  </span>";
            html += "<input type ='number' name='quantita' min='0' max='100' value = '0'>";
            html += "<span>  Data:  </span>";
            html += "<input type ='date' name='data'>";
            html += "<input type='hidden' name='id' value=" + al1.getIdPrenotazione() + ">";
            html += "<input type='hidden' name='action' value='modPrenotazione'>";
            html += "<input type='button' value='Modifica' onclick='return Prenotazione.modPrenotazioneUser(this)'>";
            html += "<input type='button' value='pizza consegnata' onclick = 'Prenotazione.modStatoPrenotazione(this)'>";      
            html += "</form>";
        }
        html += "</div>";
        return html;
    }

    public static String getPrenotazioneAdminView(Prenotazione al1) throws SQLException {
        String html = "";
        html += "<div class='prenotazione'>";
        html += "<span>ID prenotazione:  </span>";
        html += "<span class='id'>" + al1.getIdPrenotazione() + "</span>";
        html += "<span></br>Utente:  </span>";
        html += "<span class='quantità'>" + Model.getUtente(al1.getIdUtente()).getUsername() + "</span>";
        html += "<span></br>Pizza:  </span>";
        html += "<span class='pizza'>" + Model.getPizza(al1.getIdPizza()).getNome() + "</span>";
        html += "<span></br>Quantità:  </span>";
        html += "<span class='quantità'>" + al1.getQuantita() + "</span>";
        html += "<span></br>Data:  </span>";
        html += "<span class='data'>" + al1.getData() + "</span>";
        html += "<span></br>Stato:  </span>";
        html += "<span class='stato'>" + al1.getStato() + "</span>";
        html += "<form action='/PizzaWeb/Servlet' method='post' >";
        html += "<span> Utente: </span>";
        html += "<input type ='text' name='nome_utente'>";
        html += "<span> Pizza: </span>";
        html += "<input type ='text' name='pizza'>";
        html += "<span> Quantità:  </span>";
        html += "<input type ='number' name='quantita' min='0' max='100' value = '0'>";
        html += "<span>  Data:  </span>";
        html += "<input type ='date' name='data'>";
        html += "<input type='hidden' name='id' value=" + al1.getIdPrenotazione() + ">";
        html += "<input type='button' value='Modifica ordine' onclick='return Prenotazione.modPrenotazioneUser(this)'>";
        html += "<select name = 'stato'>";
        html += "<option value = 'Ordinato' "+ ((!"Ordinato".equals(al1.getStato())) ? "selected" : "") +" > Ordinato </option>";
        html += "<option value = 'Consegnato' "+((!"Consegnato".equals(al1.getStato())) ? "selected" : "")+" > Consegnato </option>";
        html += "</select>";
        html += "<input type='button' value='Modifica stato' onclick='Prenotazione.modStatoPrenotazione(this)'>";
        html += "<input type='button' value='Rimuovi' onclick='Prenotazione.remPrenotazione(this)'>";
        html += "</form>";
        html += "</div>";
        return html;
    }

    /**
     * Ritorna una stringa che fornisce i dati per costruire l'article
     *
     * @param al
     * @param req
     *
     * @return
     */
    public static String visualizzaUtenti(ArrayList<Utente> al, HttpServletRequest req) {
        String html = "";
        if (al == null) {
            return visualizzaFallimento(req);
        } else if (al.size() == 0) {
            html += "<div>Non ci sono Login ??? e tu chi sei?</div>";
        } else {
            for (Utente al1 : al) {
                html += "<div class='utente'>";
                html += "<span class='username  '>" + al1.getUsername() + "</span>";
                html += "<span class='   password   '>" + al1.getPassword() + "</span>";
                html += "<span class='   permission   '>" + al1.getPermission() + "</span>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<span>username:  </span>";
                html += "<input type ='text' name='name'>";
                html += "<span>  password:  </span>";
                html += "<input type ='text' name='password'>";
                html += "<select name = 'permission'>";
                html += "<option value = 'admin'> admin </option>";
                html += "<option value = 'user' selected> user </option>";
                html += "</select>";
                html += "<input type='hidden' name='id' value='" + al1.getUsername() + "'>";
                html += "<input type='hidden' name='action' value='modUtente'>";
                html += "<input type='submit' value='Modifica'>";
                html += "</form>";
                html += "<form action='/PizzaWeb/Servlet' method='post' >";
                html += "<input type='hidden' name='id' value=" + al1.getId() + ">";
                html += "<input type='hidden' name='action' value='remUtente'>";
                html += "<input type='submit' value='Rimuovi'>";
                html += "</form>";
                html += "</div>";
            }
        }
        return html;
    }

    /**
     * Genera un messaggio di errore
     *
     * @param req
     *
     * @return
     */
    public static String visualizzaFallimento(HttpServletRequest req) {
        String html = "";
        html += "Errore" + req.getAttribute("message");
        return html;
    }



///////////////////////////////////////////////////////////////////////////////////////////    
}
