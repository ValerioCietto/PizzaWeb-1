package components;

/**
 * Classe dell'oggetto Prenotazione
 * @author Alessandro Genovese, Anna Di Leva, Mirko Costantino, Giuseppe Mammolo;
 */
////////////////////////////////////////////////////////////////////////////////

public class Prenotazione {

    private int idPrenotazione = -1;
    private int idUtente;
    private int idPizza;
    private int quantita;
    private String data;
    private String stato;

    
////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    /**
     * Costruttore dell'oggetto Prenotazione comprensivo di id;
     * 
     * @param id indica l'id della prenotazione;
     * @param idU indica l'id dell'utente che ha effettuato la prenotazione;
     * @param idP indica l'id della pizza prenotata;
     * @param iQuantita indica la quantità di pizze prenotate;
     * @param iData indica la data della prenotazione;
    */
    
    public Prenotazione(int id, int idU, int idP, int iQuantita, String iData) {
        this.idPrenotazione = id;
        this.idUtente = idU;
        this.idPizza = idP;
        this.quantita = iQuantita;
        this.data = iData;
        this.stato = "Ordinato";
    }
    
    /**
     * Costruttore dell'oggetto Prenotaqzione comprensivo di id e stato;
     *
     * @param id indica l'id della prenotazione;
     * @param idU indica l'id dell'utente che ha effettuato la prenotazione;
     * @param idP indica l'id della pizza prenotata;
     * @param iQuantita indica la quantità di pizze prenotate;
     * @param iData indica la data della prenotazione;
     * @param stato indica lo stato di gestione della prenotazione;
    */
    
    public Prenotazione(int id, int idU, int idP, int iQuantita, String iData, String stato) {
        this.idPrenotazione = id;
        this.idUtente = idU;
        this.idPizza = idP;
        this.quantita = iQuantita;
        this.data = iData;
        this.stato = stato;
    }
    
    /**
     * Costruttore dell'oggetto Prenotazione senza conoscere id e stato;
     *
     * @param idU indica l'id dell'utente che ha effettuato la prenotazione;
     * @param idP indica l'id della pizza prenotata;
     * @param iQuantita indica la quantità di pizze prenotate;
     * @param iData indica la data della prenotazione;
    */
    
    public Prenotazione(int idU, int idP, int iQuantita, String iData) {
            this.idUtente = idU;
            this.idPizza = idP;
            this.quantita = iQuantita;
            this.data = iData;
            this.stato = "Ordinata";
        }   

    
////////////////////////////////////////////////////////////////////////////////
// UTILITY    
    
    @Override
    public String toString() {
        return "Prenotazione { id : '"+this.idPrenotazione+"', idUser : '"+this.idUtente+"', idPizza : '"+this.idPizza+"', quantità : "+this.quantita+"', data : '"+this.data+"', stato : '"+this.stato+" }";
    }
    
    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////    
// METODI DI GET

    /**
     * Restituisce l'ID della prenotazione;
     *
     * @return l'id della prenotazione;
     */
    
    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    /**
     * Restituisce l'ID dell'utente che ha effettuato la prenotazione;
     *
     * @return l'id dell'utente che ha effettuato la prenotazione;
     */
    
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Restituisce l'ID della pizza prenotata;
     *
     * @return l'id della pizza prenotata;
     */
    
    public int getIdPizza() {
        return idPizza;
    }

    /**
     * Restituisce la quantità di pizze prenotate;
     *
     * @return la quantità di pizze prenotate;
     */
    
    public int getQuantita() {
        return quantita;
    }

    /**
     * Restituisce la data della prenotazione;
     *
     * @return la data di prenotazione;
     */
    
    public String getData() {
        return data;
    }

    /**
     * Restituisce lo stato della prenotazione;
     *
     * @return lo stato di gestione della prenotazione;
     */
    
    public String getStato() {
        return stato;
    }
    
    
////////////////////////////////////////////////////////////////////////////////
// METODI DI SET
    
    /**
     * Imposta l'id prenotazione;
     *
     * @param q indica il nuovo id della prenotazione;
     */
    
    public void setIdPrenotazione(int q) {
        this.idPrenotazione = q;
    }
    
    /**
     * Imposta l'id utente;
     *
     * @param q indica il nuovo id dell'utente che effettua la prenotazione;
     */
    
    public void setIdUtente(int q) {
        this.idUtente = q;
    }
    
    /**
     * Imposta l'id pizza;
     *
     * @param q indica il nuovo id della pizza prenotata;
     */
    
    public void setIdPizza(int q) {
        this.idPizza = q;
    }
    
    /**
     * Imposta la quantità di pizze nell'ordine;
     *
     * @param q indica la nuova quantità di pizze ordinate;
     */
    
    public void setQuantita(int q) {
        this.quantita = q;
    }

    /**
     * Imposta la data dell'ordine;
     *
     * @param d indica la nuova data di prenotazione;
     */
    
    public void setData(String d) {
        this.data = d;
    }

    /**
     * Imposta lo stato dell'ordine;
     *
     * @param d indica il nuovo stato di gestione dell'ordine;
     */
    
    public void setStato(String d) {
        this.stato = d;
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}