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
public class Pizza {

    private int idPizza;
    private String nome;
    private String ingredienti;
    private double prezzo;

    /**
     * Controlla che gli input non siano nulli o non accettabili In caso di non
     * riuscita i parametri sono nulli Altrimenti carica i dati nell'oggetto
     *
     * @param id
     * @param iNome
     * @param iIngredienti
     * @param iPrezzo
     *
     * NB: i controlli in input vanno fatti PRIMA DELLA CREAZIONE DELL'OGGETTO
     */
    public Pizza(int id, String iNome, String iIngredienti, double iPrezzo) {
        this.idPizza = id;
        this.ingredienti = iIngredienti;
        this.nome = iNome;
        this.prezzo = iPrezzo;
    }

    @Override
    public String toString() {
        return "" + this.idPizza + "-" + this.ingredienti + "-" + this.nome + "-" + this.prezzo;
    }

    // METODI DI GET
    /**
     * Restituisce l'ID della pizza
     *
     * @return <String> nome pizza
     */
    public int getId() {
        return this.idPizza;
    }

    /**
     * Restituisce il nome della pizza
     *
     * @return <String> nome pizza
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce gli ingredienti della pizza
     *
     * @return <String> nome pizza
     */
    public String getIngredinti() {
        return this.ingredienti;
    }

    /**
     * Restituisce il prezzo della pizza
     *
     * @return <double> prezzo
     */
    public double getPrezzo() {
        return this.prezzo;
    }

    //METODI DI SET
    /**
     * Modifica il nome della pizza
     *
     * @param newNome
     */
    public void setNome(String newNome) {
        this.nome = newNome;
    }

    /**
     * Modifica gli ingredienti della pizza
     *
     * @param newIngredienti
     */
    public void setIngredinti(String newIngredienti) {
        this.ingredienti = newIngredienti;
    }

    /**
     * Modifica il prezzo della pizza
     *
     * @param newPrezzo
     */
    public void setPrezzo(double newPrezzo) {
        this.prezzo = newPrezzo;
    }

}
