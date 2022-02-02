package todo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Samu Peltonen
 * @version 23.4.2015
 * Luokka jossa on listassa teht‰vien ajat
 */
public class Ajat implements Iterable<Aika> {
    
    private boolean muutettu = false; 
    private String tiedostonPerusNimi = ""; 


    /**
     * Taulukko ajoista
     */
    private final Collection<Aika> alkiot = new ArrayList<Aika>();


    /**
     * Aikojen alustaminen
     */
    public Ajat() {
        // ei tarvii enemp‰‰
    }

    /**
     * Aikojen tiedostoon tallennus
     * @throws SailoException jos ei toimi
     */
    public void tallenna() throws SailoException { 
        if ( !muutettu ) return; 
        File fbak = new File(getBakNimi()); 
        File ftied = new File(getTiedostonNimi()); 
        fbak.delete();
        ftied.renameTo(fbak);

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) { 
            for (Aika maare : this) { 
                fo.println(maare.toString()); 
            } 
        } catch ( FileNotFoundException ex ) { 
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea"); 
        } catch ( IOException ex ) { 
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia"); 
        } 

        muutettu = false; 
    } 

    /**
     * Aikojen lukeminen tiedostosta
     * @param tied tiedoston nimi
     * @throws SailoException jos ei toimi
     */
    public void lueTiedostosta(String tied) throws SailoException { 
        setTiedostonPerusNimi(tied); 
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) { 
            String rivi; 
            while ( (rivi = fi.readLine()) != null ) { 
                rivi = rivi.trim(); 
                Aika maare = new Aika(); 
                maare.parse(rivi);
                long aikaAtm = maare.otaAika(0);
                String tehtAikaS = maare.getAikajono();
                long tehtAika = Long.parseLong(tehtAikaS);
                if (tehtAika >= aikaAtm) lisaa(maare); 
            } 
            muutettu = false; 

        } catch ( FileNotFoundException e ) { 
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea"); 
        } catch ( IOException e ) { 
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage()); 
        } 
    } 


    /** 
     * Luetaan tiedostosta ilman nime‰ parametrina
     * @throws SailoException jos ei toimi
     */ 
    public void lueTiedostosta() throws SailoException { 
        lueTiedostosta(getTiedostonPerusNimi()); 
    } 


    /**
     * Lis‰‰ uuden ajan tietorakenteeseen
     * @param maare lis‰tt‰v‰ aika
     */
    public void lisaa(Aika maare) {
        alkiot.add(maare);
        muutettu = true; 
    }   
    
    /**
     * Lis‰‰ uuden ajan tietorakenteeseen
     * @param maare lis‰tt‰v‰ aika
     */
    public void poista(Aika maare) {
        alkiot.remove(maare);
        muutettu = true; 
    }


    /**
     * Aikojen lukum‰‰r‰n getteri
     * @return aikojen lukum‰‰r‰
     */
    public int getLkm() {
        return alkiot.size();
    }

    /** 
     * Asettaa tiedoston perusnimen 
     * @param tied tallennustiedoston perusnimi 
     */ 
    public void setTiedostonPerusNimi(String tied) { 
        tiedostonPerusNimi = tied; 
    } 


    /** 
     * Tallennustiedoston perusnimen getteri
     * @return tallennustiedoston perusnimi 
     */ 
    public String getTiedostonPerusNimi() { 
        return tiedostonPerusNimi; 
    } 


    /** 
     * Tallennustiedoston nimen getteri
     * @return tallennustiedoston nimi 
     */ 
    public String getTiedostonNimi() { 
        return tiedostonPerusNimi + ".dat"; 
    } 


    /** 
     * Varakopion nimen getteri
     * @return varakopion nimi 
     */ 
    public String getBakNimi() { 
        return tiedostonPerusNimi + ".bak"; 
    } 


    /**
     * Iteraattori kaikkien aikojen l‰pik‰ymiseen
     * @return aikaiteraattori 
     */
    @Override
    public Iterator<Aika> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki teht‰v‰n ajat
     * @param tunnusnro teht‰v‰n tunnusnumero jolle aikoja haetaan
     * @return lista jossa viiteet aikoihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     * Ajat maareet = new Ajat();
     * Aika maare6 = new Aika(2); maareet.lisaa(maare6);
     * Aika maare66 = new Aika(1); maareet.lisaa(maare66);
     * Aika maare666 = new Aika(2); maareet.lisaa(maare666);
     * Aika maare3 = new Aika(1); maareet.lisaa(maare3);
     * Aika maare33 = new Aika(2); maareet.lisaa(maare33);
     * Aika maare333 = new Aika(5); maareet.lisaa(maare333);
     *  
     * List<Aika> loytyneet;
     * loytyneet = maareet.annaAjat(3);
     * loytyneet.size() === 0; 
     * loytyneet = maareet.annaAjat(1);
     * loytyneet.size() === 2; 
     * loytyneet.get(0) == maare66 === true;
     * loytyneet.get(1) == maare3 === true;
     * loytyneet = maareet.annaAjat(5);
     * loytyneet.size() === 1; 
     * loytyneet.get(0) == maare333 === true;
     * </pre>
     */
    public List<Aika> annaAjat(int tunnusnro) {
        List<Aika> loydetyt = new ArrayList<Aika>();
        for (Aika maare : alkiot)
            if (maare.getTehtavaNro() == tunnusnro) loydetyt.add(maare);
        return loydetyt;
    }
    
    /**
     * Haetaan kaikki teht‰v‰n ajat tietylt‰ ajalta
     * @param tunnusnro
     * @param ajalta
     * @return lista jossa viitteet aikoihin
     */
    public List<Aika> annaAjat(int tunnusnro, int ajalta) {
        Aika aika = new Aika();
        long aikajonoAjalta = aika.otaAika(ajalta);
        List<Aika> loydetyt = new ArrayList<Aika>();
        for (Aika maare : alkiot)
            if (maare.getTehtavaNro() == tunnusnro){
                String aikajonoS = maare.getAikajono();
                long aikajonoI = Long.parseLong(aikajonoS);
                if (aikajonoI < aikajonoAjalta) loydetyt.add(maare);
            }
        return loydetyt;
    }


    /**
     * Testiohjelma ajoille (TODO poistuu kun luokka kokonaan valmis)
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Ajat aikamaareet = new Ajat();
        Aika maare1 = new Aika();
        //maare1.vastaaAika(1);
        Aika maare2 = new Aika();
        //maare2.vastaaAika(2);
        Aika maare3 = new Aika();
        //maare3.vastaaAika(1);
        Aika maare4 = new Aika();
        //maare4.vastaaAika(1);

        aikamaareet.lisaa(maare1);
        aikamaareet.lisaa(maare2);
        aikamaareet.lisaa(maare3);
        aikamaareet.lisaa(maare2);
        aikamaareet.lisaa(maare4);

        List<Aika> ajat2 = aikamaareet.annaAjat(2);

        for (Aika maare : ajat2) {
            System.out.print(maare.getTehtavaNro() + " ");
            maare.tulosta(System.out);
        }

    }

}