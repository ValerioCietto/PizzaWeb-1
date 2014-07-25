/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

/**
 *
 * @author jorkut
 */
public class Prenotazione {

    private int idPrenotazione;
    private int idUtente;
    private int idPizza;
    private int quantita;
    private String data;
    private final String stato;

    /**
     * Crea la prenotazione
     *
     * @param id
     * @param idU
     * @param idP
     * @param iQuantita
     * @param iData
     * NB: i controlli in input vanno fatti PRIMA DELLA CREAZIONE DELL'OGGETTO
     */
    public Prenotazione(int id, int idU, int idP, int iQuantita, String iData) {
        this.idPrenotazione = id;
        this.idUtente = idU;
        this.idPizza = idP;
        this.quantita = iQuantita;
        this.data = iData;
        this.stato = "Ordinata";
    }

    //METODI DI GET    

    /**
     * Restituisce l'ID della prenotazione
     *
     * @return int idPrenotazione
     */
    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    /**
     * Restituisce l'ID dell'utente che ha effettuato la prenotazione
     *
     * @return \<int\> idUtente
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Restituisce l'ID della pizza prenotata
     *
     * @return \<int\> idPizza
     */
    public int getIdPizza() {
        return idPizza;
    }

    /**
     * Restituisce la quantità di pizze prenotate
     *
     * @return \<int\> quantita
     */
    public int getQuantita() {
        return quantita;
    }

    /**
     * Restituisce la data della prenotazione
     *
     * @return \<String\> data
     */
    public String getData() {
        return data;
    }

    /**
     * Restituisce lo stato della prenotazione
     *
     * @return \<String\> stato
     */
    public String getStato() {
        return stato;
    }

    //METODI DI SET
    
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
    
    @Override
    public String toString() {
        return "Prenotazione { id : '"+this.idPrenotazione+"', idUser : '"+this.idUtente+"', idPizza : '"+this.idPizza+"', quantità : "+this.quantita+"', data : '"+this.data+"', stato : '"+this.stato+" }";
    }
}