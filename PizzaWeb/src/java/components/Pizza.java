package components;


////////////////////////////////////////////////////////////////////////////////

public class Pizza {

    private int idPizza = -1;
    private String nome;
    private String ingredienti;
    private double prezzo;

    
////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    /**
     * Controlla che gli input non siano nulli o non accettabili In caso di non riuscita i parametri sono nulli Altrimenti carica i dati nell'oggetto
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
    
    public Pizza(String iNome, String iIngredienti, double iPrezzo) {
        this.ingredienti = iIngredienti;
        this.nome = iNome;
        this.prezzo = iPrezzo;
    }

    
////////////////////////////////////////////////////////////////////////////////
// UTILITY    
    
    @Override
    public String toString() {
        return "Pizza { id : '"+this.idPizza+"', nome : '"+this.nome+"', ingredienti : '"+this.ingredienti+"', prezzo : "+this.prezzo+" }";
    }
    
    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DI GET
    
    /**
     * Restituisce l'ID della pizza
     *
     * @return nome pizza
     */
    
    public int getId() {
        return this.idPizza;
    }

    /**
     * Restituisce il nome della pizza
     *
     * @return nome pizza
     */
    
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce gli ingredienti della pizza
     *
     * @return nome pizza
     */
    
    public String getIngredinti() {
        return this.ingredienti;
    }

    /**
     * Restituisce il prezzo della pizza
     *
     * @return prezzo
     */
    
    public double getPrezzo() {
        return this.prezzo;
    }

    
////////////////////////////////////////////////////////////////////////////////
// METODI DI SET
    
    /**
     * Modifica l'id della pizza
     *
     * @param newId
     */
    
    public void setId(int newId) {
        this.idPizza = newId;
    }
    
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
    
    public void setIngredienti(String newIngredienti) {
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


////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}
