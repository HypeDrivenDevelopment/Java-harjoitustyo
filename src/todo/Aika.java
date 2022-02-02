package todo;

import java.io.PrintStream;
import java.time.LocalDateTime;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Samu Peltonen
 * @version 23.4.2015
 * Luokka ajan luomiseen tietylle teht‰v‰lle
 */
public class Aika {
    //id
    private int tunnusNro;
    //viite
    private int tehtavaNro;
    private String aikajono;

    private static int seuraavaNro = 1;


    /**
     * Alustetaan aika.
     */
    public Aika() {
        // ei tarvii mit‰‰n
    }


    /**
     * Alustetaan tietyn teht‰v‰n aika.  
     * @param tehtavaNro teht‰v‰n viitenumero 
     */
    public Aika(int tehtavaNro) {
        this.tehtavaNro = tehtavaNro;
    }


    /**
     * Apumetodi jolla teht‰v‰lle annetaan aika
     * @param nro teht‰v‰n id
     * @param a aika merkkijonona
     */
    public void vastaaAika(int nro, String a) {
        tehtavaNro = nro;
        aikajono = a;
    }


    /**
     * Tulostusaliohjelma
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(aikajono);
    }


    /**
     * Antaa ajalle seuraavan vapaan tunnusnumeron
     * @return ajan uusi tunnusnumero
     * @example
     * <pre name="test">
     * Aika maare1 = new Aika();
     * maare1.getTunnusNro() === 0;
     * maare1.rekisteroi();
     * Aika maare2 = new Aika();
     * maare2.rekisteroi();
     * int n1 = maare1.getTunnusNro();
     * int n2 = maare2.getTunnusNro();
     * n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Ajan id:n getteri
     * @return ajan id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Getteri tehtavaNrolle, joka osoittaa mille teht‰v‰lle aika kuuluu
     * @return j‰senen id
     */
    public int getTehtavaNro() {
        return tehtavaNro;
    }
    
    /**
     * Getteri jonomuotoiselle ajalle
     * @return aika merkkijonona
     */
    public String getAikajono() {
        return aikajono;
    }

    /**
     * Asettaa ajan id:n
     * @param n ajan id
     */
    private void setTunnusNro(int n) { 
        tunnusNro = n; 
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1; 
    } 

    /**
     * Asettaa ajan tiedot merkkijonoon
     */
    @Override 
    public String toString() { 
        return "" + getTunnusNro() + "|" + tehtavaNro + "|" + aikajono; 
    } 


    /**
     * Irrottaa ajan tiedot merkkijonosta 
     * @param rivi merkkijono tiedostosta
     */
    public void parse(String rivi) { 
        StringBuffer sb = new StringBuffer(rivi); 
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro())); 
        tehtavaNro = Mjonot.erota(sb, '|', tehtavaNro); 
        aikajono = Mjonot.erota(sb, '|', aikajono);
    } 


    //apumetodi
    @Override 
    public boolean equals(Object obj) { 
        return this.toString().equals(obj.toString()); 
    } 


    //apumetodi
    @Override 
    public int hashCode() { 
        return tunnusNro; 
    }
    
    /**
     * Ottaa t‰m‰nhetkisen p‰iv‰m‰‰r‰n ja kellonajan ja mahdollisesti lis‰‰ siihen halutun m‰‰r‰n p‰ivi‰
     * @param ajalta lis‰tt‰vien p‰ivien m‰‰r‰
     * @return j‰rjestelm‰n aika (+p‰iv‰t)
     */
    public long otaAika(int ajalta) {
        LocalDateTime paiva = LocalDateTime.now();
        LocalDateTime paivaPlus = paiva.plusDays(ajalta);
        String paivajono = paivaPlus.toString();
        String muokattuPj = paivajono.replaceAll("[-:T.]","");
        String stringAikajonoAtm = muokattuPj.substring(0, 12);
        long aikajonoAtm = Long.parseLong(stringAikajonoAtm);
        return aikajonoAtm;
    }


    /**
     * Testip‰‰ohjelma (TODO poistuu kun luokka kokonaan valmis)
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Aika maare = new Aika();
        //maare.vastaaAika(1);
        maare.tulosta(System.out);
        long aika = System.currentTimeMillis();
        System.out.println(aika);
        LocalDateTime paiva = LocalDateTime.now();
        System.out.println(paiva);
        LocalDateTime paivav = paiva.plusDays(7);
        System.out.println(paivav);
        String paiva2 = paiva.toString();
        System.out.println(paiva2);
        String paiva3 = paiva2.replaceAll("[-:T.]","");
        String paiva4 = paiva3.substring(0, 12);
        System.out.println(paiva4);
        long paiva5 = Long.parseLong(paiva4);
        System.out.println(paiva5);
        String jono = "201512311212";
        jono = jono.substring(4, 12);
        System.out.println(jono);
        jono = jono.substring(0, 2) + "." + jono.substring(2, 4) + ". " + jono.substring(4, 6) + ":" + jono.substring(6, 8);
        System.out.println(jono);
    }

}
