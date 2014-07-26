package components;

////////////////////////////////////////////////////////////////////////////////

public class Prenotazione {

    private int idPrenotazione = -1;
    private int idUtente;
    private int idPizza;
    private int quantita;
    private String data;
    private final String stato;

    
////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    /**
     * Crea la prenotazione conoscendo l'ID
     *
     * @param id
     * @param idU
     * @param idP
     * @param iQuantita
     * @param iData
    */
    
    protected Prenotazione(int id, int idU, int idP, int iQuantita, String iData) {
        this.idPrenotazione = id;
        this.idUtente = idU;
        this.idPizza = idP;
        this.quantita = iQuantita;
        this.data = iData;
        this.stato = "Ordinata";
    }
    
    /**
     * Crea la prenotazione senza conoscere l'ID
     *
     * @param idU
     * @param idP
     * @param iQuantita
     * @param iData
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
     * Restituisce l'ID della prenotazione
     *
     * @return idPrenotazione
     */
    
    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    /**
     * Restituisce l'ID dell'utente che ha effettuato la prenotazione
     *
     * @return idUtente
     */
    
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Restituisce l'ID della pizza prenotata
     *
     * @return idPizza
     */
    
    public int getIdPizza() {
        return idPizza;
    }

    /**
     * Restituisce la quantità di pizze prenotate
     *
     * @return quantita
     */
    
    public int getQuantita() {
        return quantita;
    }

    /**
     * Restituisce la data della prenotazione
     *
     * @return data
     */
    
    public String getData() {
        return data;
    }

    /**
     * Restituisce lo stato della prenotazione
     *
     * @return stato
     */
    
    public String getStato() {
        return stato;
    }
    
    
////////////////////////////////////////////////////////////////////////////////
// METODI DI SET
    
    /**
     * Imposta l'id prenotazione
     *
     * @param q
     */
    
    public void setIdPrenotazione(int q) {
        this.idPrenotazione = q;
    }
    
    /**
     * Imposta l'id utente
     *
     * @param q
     */
    
    public void setIdUtente(int q) {
        this.idUtente = q;
    }
    
    /**
     * Imposta l'id pizza
     *
     * @param q
     */
    
    public void setIdPizza(int q) {
        this.idPizza = q;
    }
    
    /**
     * Imposta la quantità di pizze nell'ordine
     *
     * @param q
     */
    
    public void setQuantita(int q) {
        this.quantita = q;
    }

    /**
     * Imposta la data dell'ordine
     *
     * @param d
     */
    
    public void setData(String d) {
        this.data = d;
    }

    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}