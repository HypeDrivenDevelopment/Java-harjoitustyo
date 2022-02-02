package todo;

import java.io.File;
import java.util.List;

/**
 * @author Samu Peltonen
 * @version 23.4.2015
 * Luokka joka v‰litt‰‰ toimintaa TodoSwingin, teht‰vien ja aikojen v‰lill‰.
 */
public class Todo {
    
    private Tehtavat tehtavat;
    private Ajat ajat;
    
    /**
     * Alempien luokkien luonti
     */
    public Todo(){
        tehtavat = new Tehtavat();
        ajat = new Ajat();
    }
    
    /**
     * Tehtavan lisaamisen v‰litys
     * @param tehtava lisattava tehtava
     * @throws SailoException jos jokin ei onnistu 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Todo todo = new Todo();
     * Tehtava homma1 = new Tehtava(), homma2 = new Tehtava();
     * homma1.rekisteroi(); homma2.rekisteroi();
     * todo.getTehtavia() === 0;
     * todo.lisaa(homma1); todo.getTehtavia() === 1;
     * todo.lisaa(homma2); todo.getTehtavia() === 2;
     * todo.lisaa(homma1); todo.getTehtavia() === 3;
     * todo.getTehtavia() === 3;
     * todo.anna(0) === homma1;
     * todo.anna(1) === homma2;
     * todo.anna(2) === homma1;
     * todo.anna(3) === homma1; #THROWS IndexOutOfBoundsException 
     * todo.lisaa(homma1); todo.getTehtavia() === 4;
     * todo.lisaa(homma1); todo.getTehtavia() === 5;
     * todo.lisaa(homma1); todo.getTehtavia() === 6;
     * todo.lisaa(homma1); todo.getTehtavia() === 7;
     * todo.lisaa(homma1); todo.getTehtavia() === 8;
     * todo.lisaa(homma1); todo.getTehtavia() === 9;
     * todo.lisaa(homma1); todo.getTehtavia() === 10;
     * </pre>
     */
    public void lisaa(Tehtava tehtava) throws SailoException{
        tehtavat.lisaa(tehtava);
    }

    /** 
     * Ajan lisaamisen valitys 
     * @param maare lis‰tt‰v‰ aika  
     * @throws SailoException 
     */ 
    public void lisaa(Aika maare) throws SailoException{ 
        ajat.lisaa(maare); 
    }

    /**
     * Aikojen ja teht‰vien yhdist‰minen tietylt‰ ajalta
     * @param tehtava viite teht‰v‰‰n
     * @param ajalta haetaan teht‰v‰t t‰lt‰ ajalta
     * @return tietorakenne jossa viitteet lˆydettyihin aikam‰‰reisiin
     */
    public List<Aika> annaAjat(Tehtava tehtava, int ajalta) { 
        return ajat.annaAjat(tehtava.getTunnusNro(), ajalta); 
    } 
    
    /**
     * Aikojen ja teht‰vien yhdist‰minen
     * @param tehtava viite teht‰v‰‰n
     * @return tietorakenne jossa viitteet lˆydettyihin aikam‰‰reisiin
     */
    public List<Aika> annaAjat(Tehtava tehtava) { 
        return ajat.annaAjat(tehtava.getTunnusNro()); 
    } 

    /**
     * Tehtavien lukumaaran getteri
     * @return tehtavien lukum‰‰r‰
     */
    public int getTehtavia(){
        return tehtavat.getLkm();
    }
    
    /**
     * Teht‰v‰n kuvauksen getteri
     * @param tehtava k‰sitelt‰v‰ teht‰v‰
     * @return teht‰v‰n kuvaus
     */
    public String getKuvaus(Tehtava tehtava){
        return tehtava.getKuvaus();
    }
    

    /**
     * Kutsu taulukon i:nnen alkion viitteen antavaan metodiin
     * @param i monesko j‰sen (ei indeksi)
     * @return viite i:n tehtavaan
     */
    public Tehtava anna(int i){
        return tehtavat.anna(i);
    }

    /** 
     * Lukee teht‰vien ja aikojen tiedot tiedostosta 
     * @param nimi jota k‰yte‰‰n lukemisessa 
     * @throws SailoException jos lukeminen ep‰onnistuu 
     */ 
    public void lueTiedostosta(String nimi) throws SailoException { 
        tehtavat = new Tehtavat();
        ajat = new Ajat(); 
        setTiedosto(nimi); 
        tehtavat.lueTiedostosta(); 
        ajat.lueTiedostosta(); 
    }

    /** 
     * Asettaa tiedostojen perusnimet 
     * @param nimi hakemisto
     */ 
    public void setTiedosto(String nimi) { 
        File dir = new File(nimi); 
        dir.mkdirs(); 
        String hakemistonNimi = ""; 
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/"; 
        tehtavat.setTiedostonPerusNimi(hakemistonNimi + "tehtavat"); 
        ajat.setTiedostonPerusNimi(hakemistonNimi + "ajat"); 
    } 



    /** 
     * Tallentaa teht‰vien ja aikojen tiedot erikseen
     * @throws SailoException jos ei onnistu
     */ 
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            tehtavat.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }
        try {
            ajat.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }
    
    /**
     * V‰litt‰‰ teht‰v‰n kaikkien aikam‰‰reiden poistamisen ja t‰m‰n j‰lkeen poistaa myˆs teht‰v‰n
     * @param tehtavaKohdalla teht‰v‰ joka on valittuna listasta.
     */
    public void poista(Tehtava tehtavaKohdalla) {
        List<Aika> aika = annaAjat(tehtavaKohdalla);
        for (Aika maare:aika) {
            ajat.poista(maare);
        }
        tehtavat.poista(tehtavaKohdalla.getTunnusNro());
    }
    

    /**
     * Testip‰‰ohjelma (TODO poistuu kun luokka kokonaan valmis)
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Todo todo = new Todo();
        
        Tehtava homma = new Tehtava();
        Tehtava duuni = new Tehtava();
        Tehtava tinki = new Tehtava();
        
        homma.rekisteroi();
        duuni.rekisteroi();
        tinki.rekisteroi();
        
        //homma.vastaa(1);
        //duuni.vastaa(2);
        //tinki.vastaa(3);
        
        try {
            todo.lisaa(homma);
            todo.lisaa(duuni);
            todo.lisaa(tinki);
        } catch (SailoException e) {
            System.err.println(e.getMessage());
        }
        
        for (int i = 0; i < todo.getTehtavia(); i++){
            Tehtava tehtava = todo.anna(i);
            System.out.println("Tehtava nro: " + i);
            tehtava.tulosta(System.out);
        }

    }


}
