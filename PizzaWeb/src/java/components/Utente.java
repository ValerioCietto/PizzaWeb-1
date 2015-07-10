package components;

/**
 * Classe dell'oggetto Utente
 *
 * @author Alessandro Genovese, Anna Di Leva, Mirko Costantino, Giuseppe
 * Mammolo;
 */
////////////////////////////////////////////////////////////////////////////////
public class Utente {

    private int idUtente = -1;
    private String username;
    private String pwd;
    private String permission;

////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    /**
     * Costruttore dell'oggetto Utente comprensivo di id;
     *
     * @param id indica l'id dell'utente;
     * @param iNome indica il nome dell'utente;
     * @param iPwd indica la password dell'utente;
     * @param iRuolo indica il ruolo dell'utente;
     */
    public Utente(int id, String iNome, String iPwd, String iRuolo) {
        this.idUtente = id;
        this.username = iNome;
        this.pwd = iPwd;
        this.permission = iRuolo;
    }

    /**
     * Costruttore dell'oggetto Utente senza conoscere l'id;
     *
     * @param iNome indica il nome dell'utente;
     * @param iPwd indica la password dell'utente;
     * @param iRuolo indica il ruolo dell'utente;
     */
    public Utente(String iNome, String iPwd, String iRuolo) {
        this.username = iNome;
        this.pwd = iPwd;
        this.permission = iRuolo;
    }

////////////////////////////////////////////////////////////////////////////////
// UTILITY
    @Override
    public String toString() {
        return "Utente { id : '" + this.idUtente + "', username : '" + this.username + "', password : '" + this.pwd + "', permission : " + this.permission + " }";
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DI GET   
    /**
     * Restituisce l'id dell'utente;
     *
     * @return l'id dell'utente;
     */
    public int getId() {
        return idUtente;
    }

    /**
     * Restituisce il nome dell'utente;
     *
     * @return il nome dell'utente;
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password dell'utente;
     *
     * @return la password dell'utente;
     */
    public String getPassword() {
        return pwd;
    }

    /**
     * Restituisce il ruolo dell'utente;
     *
     * @return il ruolo dell'utente;
     */
    public String getPermission() {
        return permission;
    }

////////////////////////////////////////////////////////////////////////////////
// METODI DI SET
    /**
     * Modifica l'id dell'utente;
     *
     * @param id indica il nuovo id dell'utente;
     */
    public void setId(int id) {
        this.idUtente = id;
    }

    /**
     * Modifica lo username dell'utente;
     *
     * @param name indica il nuovo nome dell'utente;
     */
    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * Modifica la password dell'utente;
     *
     * @param newPwd indica la nuova password dell'utente;
     */
    public void setPwd(String newPwd) {
        this.pwd = newPwd;
    }

    /**
     * Modifica il ruolo dell'utente;
     *
     * @param newRuolo indica il nuovo ruolo dell'utente;
     */
    public void setPermission(String newRuolo) {
        this.permission = newRuolo;
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}
