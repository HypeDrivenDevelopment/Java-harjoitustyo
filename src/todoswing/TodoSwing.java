package todoswing;

import java.io.PrintStream;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import todo.Aika;
import todo.SailoException;
import todo.Tehtava;
import todo.Todo;
import fi.jyu.mit.gui.AbstractChooser;
import fi.jyu.mit.gui.TextAreaOutputStream;
import GUI.lomake;
import GUI.paaikkuna;

/**
 * @author Samu Peltonen
 * @version 23.4.2015
 * K�ytt�liittym�n toiminnallisuutta hoitava luokka
 */
public class TodoSwing {
    
    private Todo todo;
    private paaikkuna paaikkuna;
    //private TodoSwing todoswing;
    
    private final JTextArea getTextAreaKuvaus() {return paaikkuna.textArea;  }
    private final AbstractChooser<Tehtava> getListTehtavat() {return paaikkuna.tehtavaList;  }
    private Tehtava tehtavaKohdalla;
    
    
    /**
     * Listan toimintaa demonstroivat teht�v�t taulukossa.
     */
    public String[] getkohteet = new String[] {"09:30 Palaveri", "09:45 Johdon palaveri", "10:15 YT-neuvottelut", "11:45 Tuote X deadline", "13:00 Tapaaminen yritys Y kanssa"};
    
    /**
     * Kuvauksen toimintaa demonstroiva teksti.
     */
    public String gettext = "Normaali alkuviikon palaveri.";
         
    /**
     * Viite p��ikkunaan ja Todo-luokan luonti
     * @param paaikkuna
     */
    public TodoSwing(paaikkuna paaikkuna) {
        this.paaikkuna = paaikkuna;
        todo = new Todo();
    }
    
    /**
     * Hakee teht�v�n ja ajan.
     */
    private void hae(){
        getListTehtavat().clear();
        for (int i = 0; i < todo.getTehtavia(); i++){
            Tehtava tehtava = todo.anna(i);
            List<Aika> aika = todo.annaAjat(tehtava);
            
            for (Aika maare:aika) {
                String jono = maare.getAikajono();
                jono = jono.substring(4, 12);
                jono = jono.substring(2, 4) + "." + jono.substring(0, 2) + ".  " + jono.substring(4, 6) + ":" + jono.substring(6, 8);
                getListTehtavat().add(jono + " " + tehtava.getNimi(), tehtava);
            }
        }
    }
    
    /**
     * Hakee teht�v�n ja ajan tietylt� ajalta.
     * @param ajalta kuinka monelta p�iv�lt� teht�vi� haetaan
     */
    private void hae(int ajalta){
        getListTehtavat().clear();
        for (int i = 0; i < todo.getTehtavia(); i++){
            Tehtava tehtava = todo.anna(i);
            List<Aika> aikaList = todo.annaAjat(tehtava, ajalta);           
            
            for (Aika maare:aikaList) {
                String jono = maare.getAikajono();
                jono = jono.substring(4, 12);
                jono = jono.substring(0, 2) + "." + jono.substring(2, 4) + ". " + jono.substring(4, 6) + ":" + jono.substring(6, 8);
                getListTehtavat().add(jono + " " + tehtava.getNimi(), tehtava);
            }
        }
    }

    /**
     * Lukee teht�vien tiedot tiedostosta
     * @param s hakemiston nimi
     * @return null jos onnistuu, muuten virhe
     */
    public String lueTiedosto(String s) {
        try {
            todo.lueTiedostosta(s);
            hae();
            return null;
        } catch (SailoException e) {
            hae();
            return e.getMessage();
        }
    }
    
    /**
     * Tallentaa muutokset
     * @return ei mit��n jos onnistuu, muuten virheilmoitus
     */
    public String tallenna() {
        try { 
            todo.tallenna(); 
            return null; 
        } catch (SailoException ex) { 
            JOptionPane.showMessageDialog(null, "Tallennuksessa ongelmia! " + ex.getMessage()); 
            return ex.getMessage(); 
        } 
    }
    
    /**
     * Lis�� teht�v�n lomake-dialogista tulleilla tiedoilla
     * @param s1 nimen editpaneelin sis�lt�
     * @param s2 ajan editpaneelin sis�lt�
     * @param s3 kuvauksen editpaneelin sis�lt�
     * @param s4 teht�vien m��r�n editpaneelin sis�lt�
     */
    public void lisaaTeht(String s1, String s2, String s3, String s4) {
        Tehtava tehtava = new Tehtava();
        tehtava.rekisteroi();
        tehtava.vastaa(s1, s3);
        int nro = tehtava.getTunnusNro();
        int maara = Integer.parseInt(s4);
        try {
            todo.lisaa(tehtava);
        } catch (SailoException e) {
            JOptionPane.showMessageDialog(null, "ei onnistu" + e.getMessage());
        }
        while (maara != 0) {
            Aika aika = new Aika();
            aika.rekisteroi();
            aika.vastaaAika(nro, s2);
            try {    
                todo.lisaa(aika);        
            } catch (SailoException e) {
                JOptionPane.showMessageDialog(null, "ei onnistu" + e.getMessage());
            }
            String viikkoLisatty = lisaaViikko(s2);
            s2 = viikkoLisatty;
            maara --;
        }
        hae();
    }
    
    /**
     * Lis�� aika-merkkijonoon yhden viikon lis�� (ei kovin kaunista mutta toimii)
     * @param aikajono aikam��re merkkijonona
     * @return aikajono lis�tyll� viikolla
     */
    public String lisaaViikko(String aikajono) {

        int VV = Integer.parseInt(aikajono.substring(0, 4));
        int KK = Integer.parseInt(aikajono.substring(4, 6));
        int PP = Integer.parseInt(aikajono.substring(6, 8));

        String loput = aikajono.substring(8, 12);
        PP = PP + 7;

        if (KK == 1 || KK == 3 || KK == 5 || KK == 7 || KK == 8 || KK == 10 || KK == 12){
            if (PP > 31){
                PP = PP - 31;
                KK = KK + 1;
            }
        }
        
        if (KK == 4 || KK == 6 || KK == 9 || KK == 11){
            if (PP > 30){
                PP = PP - 30;
                KK = KK + 1;
            }
        }
        
        if (KK == 2){
            if (PP > 28){
                PP = PP - 28;
                KK = KK + 1;
            }
        }
        
        String vuodet = Integer.toString(VV);
        String kuukaudet = String.format("%02d", KK);
        String paivat = String.format("%02d", PP);
        String viikkoLisatty = vuodet + kuukaudet + paivat + loput;
        return viikkoLisatty;
        
    }
    /**
     * Poistaa valitun teht�v�n kaikki esiintym�t
     */
    public void poistaTeht() {
        
        int ind = getListTehtavat().getSelectedIndex();
        if (ind < 0) return;
        tehtavaKohdalla = getListTehtavat().getSelectedObject(); 
        if (tehtavaKohdalla == null) return;
        todo.poista(tehtavaKohdalla);
        hae();
    }
    
    /**
     * N�ytt�� teht�v�listassa t�m�n p�iv�n teht�v�t
     */
    public void paivaNakyma() {
        hae(1);
    }
    
    /**
     * N�ytt�� teht�v�listassa t�m�n viikon teht�v�t
     */
    public void viikkoNakyma() {
        hae(7);
    }
    
    /**
     * N�ytt�� teht�v�listassa kaikki teht�v�t
     */
    public void kaikkiNakyma() {
        hae();
    }
    
    /**
     * Tulosta teht�v�n kuvaus textboxiin
     */
    public void naytaKuvaus() {
        int ind = getListTehtavat().getSelectedIndex();
        if (ind < 0) return;
        tehtavaKohdalla = getListTehtavat().getSelectedObject(); 
        if (tehtavaKohdalla == null) return;
        getTextAreaKuvaus().setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(getTextAreaKuvaus())) {
            String kuvaus = todo.getKuvaus(tehtavaKohdalla);
            os.println(kuvaus);
        }
    }
    /**
     * Avaa teht�vien luomiseen k�ytett�v�n lomakkeen
     */
    public void avaaLomake() {
        lomake dialog = new lomake(this);
        dialog.setVisible(true);

    }

}



