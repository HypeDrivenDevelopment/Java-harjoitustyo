package todo;

/**
 * @author Samu Peltonen
 * @version 26.3.2015
 * Luokka joka l‰hett‰‰ poikkeusviestin mik‰li tietorakenne on jo t‰ynn‰.
 */
public class SailoException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    
    /**
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti){
        super (viesti);
    }

}
