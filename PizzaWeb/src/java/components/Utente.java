package components;

////////////////////////////////////////////////////////////////////////////////

public class Utente {

    private int idUtente = -1;
    private String username;
    private String pwd;
    private String permission;

////////////////////////////////////////////////////////////////////////////////
// COSTRUTTORE
    
    /**
     * Crea l'utente conoscendo l'ID
     *
     * @param id
     * @param iNome
     * @param iPwd
     * @param iRuolo
     */
    
    protected Utente(int id, String iNome, String iPwd, String iRuolo) {
        this.idUtente = id;
        this.username = iNome;
        this.pwd = iPwd;
        this.permission = iRuolo;
    }
    
    /**
     * Crea l'utente senza conoscere l'ID
     *
     * @param iNome
     * @param iPwd
     * @param iRuolo
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
        return "Utente { id : '"+this.idUtente+"', username : '"+this.username+"', password : '"+this.pwd+"', permission : "+this.permission+" }";
    }
    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// METODI DI GET   
    
    /**
     * Restituisce l'ID dell'utente
     *
     * @return nome Pizza
     */
    
    public int getId() {
        return idUtente;
    }

    /**
     * Restituisce il nome dell'utente
     *
     * @return nome Pizza
     */
    
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password dell'utente
     *
     * @return 
     */
    
    public String getPassword() {
        return pwd;
    }

    /**
     * Restituisce il ruolo dell'utente
     *
     * @return 
     */
    
    public String getPermission() {
        return permission;
    }

////////////////////////////////////////////////////////////////////////////////
// METODI DI SET
    
    /**
     * Modifica l'id dell'utente
     *
     * @param id
     */
    
    protected void setId(int id) {
        this.idUtente = id;
    }
    
    /**
     * Modifica l'id dell'utente
     *
     * @param name
     */
    
    protected void setUsername(String name) {
        this.username = name;
    }
    
    /**
     * Modifica la password dell'utente
     *
     * @param newPwd
     */
    
    public void setPwd(String newPwd) {
        this.pwd = newPwd;
    }

    /**
     * Modifica il ruolo dell'utente
     *
     * @param newRuolo
     */
    
    public void setPermission(String newRuolo) {
        this.permission = newRuolo;
    }

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}
