package GUI;

import java.awt.EventQueue;
import java.awt.BorderLayout;

import fi.jyu.mit.gui.AbstractChooser;
import fi.jyu.mit.gui.GenListChooser;
import fi.jyu.mit.gui.IStringListChooser;
import fi.jyu.mit.gui.SelectionChangeListener;
import fi.jyu.mit.gui.TextAreaOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.PrintStream;

import todo.Tehtava;
import todoswing.TodoSwing;

/**
 * @author Samu Peltonen
 * @version 23.4.2015
 * 
 * Ohjelman pääikkuna sekä toistaiseksi myös toiminta.
 *
 */
public class paaikkuna {
    
    /**
     * Toiminnallisuutta hoitava luokka
     */
    protected final TodoSwing todoswing;

    JFrame frmTodo;
    /**
     * Tehtävän kuvauksen tekstialue
     */
    public final JTextArea textArea = new JTextArea();
    /**
     * Tehtävien nimien ja aikojen listaus
     */
    public final AbstractChooser<Tehtava> tehtavaList = new GenListChooser<Tehtava>();
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu mnTiedosto = new JMenu("Tiedosto");
    private final JMenu mnMuokkaa = new JMenu("Muokkaa");
    private final JMenu mnNyt = new JMenu("N\u00E4yt\u00E4");
    private final JMenuItem mntmTallenna = new JMenuItem("Tallenna");
    private final JMenuItem mntmLopeta = new JMenuItem("Lopeta");
    private final JMenuItem mntmLisUusiTehtv = new JMenuItem("Lis\u00E4\u00E4 uusi teht\u00E4v\u00E4");
    private final JMenuItem mntmPoistaTehtv = new JMenuItem("Poista teht\u00E4v\u00E4");
    private final JMenuItem mntmNewMenuItem = new JMenuItem("P\u00E4iv\u00E4");
    private final JMenuItem mntmViikko = new JMenuItem("Viikko");
    private Tehtava tehtavaKohdalla;
    private final JMenuItem mntmKaikki = new JMenuItem("Kaikki");

    /**
     * Ohjelman käynnistys
     * @param args Ei käytössä
     * 
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    paaikkuna window = new paaikkuna();
                    window.frmTodo.setVisible(true);
                    window.lueTiedosto("tehtavat"); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Ohjelman luonti
     */
    public paaikkuna() {
        todoswing = new TodoSwing(this); 
        initialize();
        
    }

    /**
     * Pääikkunan sisällön luonti
     */
    private void initialize() {
        
        frmTodo = new JFrame();
        frmTodo.setTitle("TODO");
        frmTodo.setBounds(100, 100, 800, 600);
        frmTodo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea.setEditable(false);
        frmTodo.getContentPane().add(textArea, BorderLayout.CENTER);
        tehtavaList.addSelectionChangeListener(new SelectionChangeListener<Tehtava>() {
            @Override
            public void selectionChange(IStringListChooser<Tehtava> sender) {
                todoswing.naytaKuvaus();
            }
        });

        tehtavaList.setKohteet(todoswing.getkohteet);
        
        frmTodo.getContentPane().add(tehtavaList, BorderLayout.WEST);
        
        frmTodo.setJMenuBar(menuBar);
        
        menuBar.add(mnTiedosto);
        mntmTallenna.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoswing.tallenna();
            }
        });
        
        mnTiedosto.add(mntmTallenna);
        mntmLopeta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lopeta();
            }
        });
        
        mnTiedosto.add(mntmLopeta);
        
        menuBar.add(mnMuokkaa);
        mntmLisUusiTehtv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoswing.avaaLomake();
            }
        });
        
        mnMuokkaa.add(mntmLisUusiTehtv);
        mntmPoistaTehtv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoswing.poistaTeht();
            }
        });
        
        mnMuokkaa.add(mntmPoistaTehtv);
        
        menuBar.add(mnNyt);
        mntmNewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoswing.paivaNakyma();
            }
        });
        
        mnNyt.add(mntmNewMenuItem);
        mntmViikko.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoswing.viikkoNakyma();
            }
        });
        
        mnNyt.add(mntmViikko);
        mntmKaikki.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoswing.kaikkiNakyma();
            }
        });
        mnNyt.add(mntmKaikki);
    }

    /**
     * Lukee tiedostosta tehtävän tiedot
     * @param nimi tiedosto josta tehtävän tiedot luetaan
     */
    protected void lueTiedosto(String nimi) {
        String virhe = todoswing.lueTiedosto(nimi);
        if (virhe != null) JOptionPane.showMessageDialog(null, virhe);
    }
    
    /**
     * Sulkee ikkunan
     */
    protected void lopeta() {
        System.exit(0);
    }
    
    /**
     * Tulostaa tehtävän kuvauksen tekstilaatikkoon
     */
    public void naytaKuvaus() {
        int ind = tehtavaList.getSelectedIndex();
        if (ind < 0) return;
        tehtavaKohdalla = tehtavaList.getSelectedObject(); 
        if (tehtavaKohdalla == null) return;
        textArea.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(textArea)) {
            //yritetään tulostusta
        }
    }

}
