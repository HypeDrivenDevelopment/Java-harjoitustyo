package todo;

import java.io.PrintStream;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Samu Peltonen
 * @version 23.4.2015
 * Luokka tehtävän luomiseen.
 */
public class Tehtava {
    
    private int tunnusNro;
    private String nimi = "";
    private String kuvaus = "";
    private int numero = 0;
    private static int seuraavaNro = 1; 
    
    /**
     * Tulostus-aliohjelma
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(numero);
        out.println(nimi);
        out.println(kuvaus + "\n");
    }
    
    /**
     * Asetetaan tehtävään tiedot
     * @param n nimi
     * @param k kuvaus
     * @example
     * <pre name="test">
     * Tehtava homma = new Tehtava();
     * homma.getTunnusNro() === 0;
     * homma.rekisteroi();
     * Tehtava duuni = new Tehtava();
     * duuni.rekisteroi();
     * int t1 = homma.getTunnusNro();
     * int t2 = duuni.getTunnusNro();
     * t1 === t2-1;
     * </pre>
     */
    public void vastaa(String n, String k) {
        nimi = n;
        kuvaus = k;
    }
    
    /**
     * Tehtävälle annetaan ensimmäinen vapaa id ja siirrytään seuraavaan
     * @return tehtavan id
     */
    public int rekisteroi(){
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    /**
     * Asettaa tehtävän id:n
     * @param nr tehtävän id
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    /**
     * Tehtävän id:n getteri
     * @return tehtavan id
     */
    public int getTunnusNro(){
        return tunnusNro;
    }
    
    /**
     * Tehtävän kuvauksen getteri
     * @return tehtavan kuvaus
     */
    public String getKuvaus(){
        return kuvaus;
    }
    
    /**
     * Tehtävän nimen getteri
     * @return tehtavan nimi
     * @example
     * <pre name="test">
     * Tehtava homma = new Tehtava();
     * homma.vastaa("Palaveri", "kuvaus");
     * homma.getNimi() === "Palaveri";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }

    @Override
    public String toString() {
        return getTunnusNro() + "|" + nimi + "|" + kuvaus;
    }
    
    /**
     * Erottaa tehtävän tiedot yhdestä merkkijonosta
     * @param rivi tiedoston rivi
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        kuvaus = Mjonot.erota(sb, '|', kuvaus);
    }

    /**
     * Testipääohjelma (TODO poistuu kun luokka kokonaan valmis)
     * @param args ei käytössä
     */
    public static void main(String[] args){

        Tehtava homma = new Tehtava();
        Tehtava duuni = new Tehtava();
        
        homma.tulosta(System.out);
        duuni.tulosta(System.out);
        
        homma.rekisteroi();
        duuni.rekisteroi();
        
        //homma.vastaa(1);
        //duuni.vastaa(2);
        
        homma.tulosta(System.out);
        duuni.tulosta(System.out);
        
    }

    
    
    
}
