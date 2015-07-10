package components;

/**
 * Classe dell'oggetto Pizza
 *
 * @author Alessandro Genovese, Anna Di Leva, Mirko Costantino, Giuseppe
 * Mammolo;
 */
////////////////////////////////////////////////////////////////////////////////
public class Pizza {

    private int idPizza = -1;
    private String nome;
    private String ingredienti;
    private double prezzo;

////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    /**
     * Costruttore dell'oggetto Pizza comprensivo di id;
     *
     * @param id indica l'id della pizza;
     * @param iNome indica il nome della pizza;
     * @param iIngredienti indica gli ingredienti della pizza;
     * @param iPrezzo indica il prezzo della pizza
     */
    public Pizza(int id, String iNome, String iIngredienti, double iPrezzo) {
        this.idPizza = id;
        this.ingredienti = iIngredienti;
        this.nome = iNome;
        this.prezzo = iPrezzo;
    }

    /**
     * Costruttore dell'oggetto Pizza senza id;
     *
     * @param iNome indica il nome della pizza;
     * @param iIngredienti indica gli ingredienti della pizza;
     * @param iPrezzo indica il prezzo della pizza
     */
    public Pizza(String iNome, String iIngredienti, double iPrezzo) {
        this.ingredienti = iIngredienti;
        this.nome = iNome;
        this.prezzo = iPrezzo;
    }

////////////////////////////////////////////////////////////////////////////////
// UTILITY    
    @Override
    public String toString() {
        return "Pizza { id : '" + this.idPizza + "', nome : '" + this.nome + "', ingredienti : '" + this.ingredienti + "', prezzo : " + this.prezzo + " }";
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DI GET
    /**
     * Restituisce l'ID della pizza;
     *
     * @return l'id della pizza;
     */
    public int getId() {
        return this.idPizza;
    }

    /**
     * Restituisce il nome della pizza;
     *
     * @return il nome pizza;
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce gli ingredienti della pizza;
     *
     * @return gli ingredienti della pizza;
     */
    public String getIngredinti() {
        return this.ingredienti;
    }

    /**
     * Restituisce il prezzo della pizza;
     *
     * @return il prezzo della pizza;
     */
    public double getPrezzo() {
        return this.prezzo;
    }

////////////////////////////////////////////////////////////////////////////////
// METODI DI SET
    /**
     * Modifica l'id della pizza;
     *
     * @param newId indica il nuovo id da assegnare alla pizza;
     */
    public void setId(int newId) {
        this.idPizza = newId;
    }

    /**
     * Modifica il nome della pizza;
     *
     * @param newNome indica il nuovo nome da assegnare alla pizza;
     */
    public void setNome(String newNome) {
        this.nome = newNome;
    }

    /**
     * Modifica gli ingredienti della pizza;
     *
     * @param newIngredienti indica i nuovi ingredienti da assegnare alla pizza;
     */
    public void setIngredienti(String newIngredienti) {
        this.ingredienti = newIngredienti;
    }

    /**
     * Modifica il prezzo della pizza;
     *
     * @param newPrezzo indica il nuovo prezzo da assegnare alla pizza;
     */
    public void setPrezzo(double newPrezzo) {
        this.prezzo = newPrezzo;
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}
