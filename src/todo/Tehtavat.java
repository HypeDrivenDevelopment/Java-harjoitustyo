package todo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author Samu Peltonen
 * @version 23.4.2015
 * Luokka jossa teht‰v‰t ovat taulukossa ja niit‰ voidaan lis‰t‰ taulukkoon
 */
public class Tehtavat {
    
    /**
     * Maksimi teht‰vien m‰‰r‰ taulukossa, nyt kasvaa automaattisesti
     */
    public static final int MAX_TEHTAVIA = 9;
    
    private int lkm;
    private Tehtava[] alkiot;
    private boolean muutettu;
    private String tiedostonPerusNimi = "tehtavat";
    
    /**
     * Muodostaja
     */
    public Tehtavat(){
        alkiot = new Tehtava[MAX_TEHTAVIA];
        lkm = 0;
    }
    
    /**
     * Teht‰vien lukum‰‰r‰n getteri
     * @return teht‰vien lukum‰‰r‰
     */
    public int getLkm(){
        return lkm;
    }
    
    /**
     * Teht‰v‰n lis‰‰minen taulukkoon
     * @param uusi lis‰tt‰v‰ teht‰v‰
     * @throws SailoException jos ei mahdu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Tehtavat tehtavat = new Tehtavat();
     * Tehtava homma = new Tehtava(), duuni = new Tehtava();
     * tehtavat.getLkm() === 0;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 1;
     * tehtavat.lisaa(duuni); tehtavat.getLkm() === 2;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 3;
     * tehtavat.anna(0) === homma;
     * tehtavat.anna(1) === duuni;
     * tehtavat.anna(2) === homma;
     * tehtavat.anna(1) == homma === false;
     * tehtavat.anna(1) == duuni === true;
     * tehtavat.anna(3) === homma; #THROWS IndexOutOfBoundsException 
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 4;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 5;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 6;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 7;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 8;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 9;
     * tehtavat.lisaa(homma); tehtavat.getLkm() === 10;
     * </pre>
     */
    public void lisaa(Tehtava uusi) throws SailoException{
        if (lkm >= alkiot.length) { 
            alkiot = Arrays.copyOf(alkiot, alkiot.length+10);
        }
        alkiot[lkm] = uusi;
        lkm++;
        muutettu = true;
        
    }
    
    /**
     * Antaa tietylle indeksille teht‰v‰‰n viitteen
     * @param i indeksi
     * @return viite tehtavaan, jonka indeksi on i
     */
    public Tehtava anna(int i){
        if (i<0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Lukee teht‰v‰t tiedostosta
     * @param tied tiedoston nimi
     * @throws SailoException jos virhe
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                Tehtava tehtava = new Tehtava();
                tehtava.parse(rivi);
                lisaa(tehtava);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }

    /**
     * Lukee teht‰v‰t tiedostosta ilman tiedostonnime‰ parametrin‰
     * @throws SailoException jos virhe
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /**
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Asettaa tiedoston perusnimen ilman p‰‰tett‰
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Tallentaa teht‰v‰t tiedostoon
     * @throws SailoException jos virhe
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (int i=0; i<getLkm(); i++) {
                Tehtava tehtava = anna(i);
                fo.println(tehtava.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    /**
     * Poistaa valitun teht‰v‰n
     * @param id poistettavan teht‰v‰n id
     */
    public void poista(int id) {
        int paikka = haePaikka(id);
        lkm--; 
        for (int i = paikka; i < lkm; i++){
            alkiot[i] = alkiot[i + 1]; // siirret‰‰n alkioita taulukossa eteenp‰in alkaen poistettavasta
        }
        alkiot[lkm] = null; //koska m‰‰r‰ v‰hentynyt yhdell‰ niin poistetaan viimeinen alkio
        muutettu = true; 

    }

    /**
     * Hakee poistettavan teht‰v‰n paikan taulukossa
     * @param id teht‰v‰n id
     * @return teht‰v‰n paikka taulukossa
     */
    public int haePaikka(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; //johtaa virheeseen
    }


    /**
     * Testausp‰‰ohjelma (TODO poistuu kun luokka kokonaan valmis)
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        
        Tehtavat tehtavat = new Tehtavat();
        
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
            tehtavat.lisaa(homma);
            tehtavat.lisaa(duuni);
            tehtavat.lisaa(tinki);
        } catch (SailoException e) {
            e.printStackTrace();
        }

        
        System.out.println("Tehtavia on " + tehtavat.getLkm() + "\n");
        
        for (int i = 0; i<tehtavat.getLkm(); i++){
            Tehtava tehtava = tehtavat.anna(i);
            System.out.println("Tehtava nro: " + i);
            tehtava.tulosta(System.out);
        }


        
    }


}
