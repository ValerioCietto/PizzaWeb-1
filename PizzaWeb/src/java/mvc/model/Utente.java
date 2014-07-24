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
public class Utente {

    private int idUtente = -1;
    private String username;
    private String pwd;
    private String permission;

    /**
     * Crea l'utente
     *
     * @param id
     * @param iNome
     * @param iPwd
     * @param iRuolo
     *
     * NB: i controlli in input vanno fatti PRIMA DELLA CREAZIONE DELL'OGGETTO
     */
    protected Utente(int id, String iNome, String iPwd, String iRuolo) {
        this.idUtente = id;
        this.username = iNome;
        this.pwd = iPwd;
        this.permission = iRuolo;
    }
    
    public Utente(String iNome, String iPwd, String iRuolo) {
        this.username = iNome;
        this.pwd = iPwd;
        this.permission = iRuolo;
    }


        //METODI DI GET    
    /**
     * Restituisce l'ID dell'utente
     *
     * @return int nome Pizza
     */
    public int getId() {
        return idUtente;
    }

    /**
     * Restituisce il nome dell'utente
     *
     * @return <String> nome Pizza
     */
    public String getNome() {
        return username;
    }

    /**
     * Restituisce la password dell'utente
     *
     * @return <String>
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Restituisce il ruolo dell'utente
     *
     * @return \<String\>
     */
    public String getPermission() {
        return permission;
    }

        //METODI DI SET
    /**
     * Modifica l'id dell'utente
     *
     * @param id
     */
    protected void setId(int id) {
        this.idUtente = id;
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
}
