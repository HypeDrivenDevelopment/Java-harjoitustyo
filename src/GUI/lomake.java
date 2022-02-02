package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fi.jyu.mit.gui.EditPanel;

import javax.swing.BoxLayout;

import todoswing.TodoSwing;

/**
 * Lomake jolla käyttäjä luo uuden tehtävän
 * 
 * @author Samu Peltonen
 * @version 23.4.2015
 *
 */
public class lomake extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private TodoSwing todoswing;

    /**
     * Luodaan dialogi
     * @param todoswing 
     */
    public lomake(TodoSwing todoswing) {
        this.todoswing = todoswing;
        setTitle("Teht\u00E4v\u00E4editori");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        EditPanel editPanel1 = new EditPanel();
        EditPanel editPanel2 = new EditPanel();
        EditPanel editPanel3 = new EditPanel();
        EditPanel editPanel4 = new EditPanel();
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            {
                editPanel1.setCaption("Teht\u00E4v\u00E4n nimi");
                panel.add(editPanel1);                
            }
            {
                editPanel2.setCaption("yyyymmddhhMM");
                panel.add(editPanel2);
            }
            {
                editPanel3.setCaption("kuvaus");
                panel.add(editPanel3);
            }
            {
                editPanel4.setCaption("tehtävien määrä");
                panel.add(editPanel4);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent arg0){
                        boolean oikeellisuus = oikeellisuusTarkistus(editPanel2);
                        boolean maaraOikeellisuus = maaraOikeellisuusTarkistus(editPanel4);
                        if (oikeellisuus == true) {
                            if (maaraOikeellisuus == true){
                                vieTiedot(editPanel1, editPanel2, editPanel3, editPanel4);
                                dispose();
                            }
                            else JOptionPane.showMessageDialog(null, "Määrässä virhe");

                        }
                        else JOptionPane.showMessageDialog(null, "Ajassa virhe");
                    }

                    /**
                     * Tarkistaa onko tehtävien määrä kokonaisluku
                     * @param ep tehtävien määrän editpaneelin sisältö
                     * @return onko oikeaa muotoa
                     */
                    private boolean maaraOikeellisuusTarkistus(EditPanel ep) {
                        String s = ep.getText();
                        try {
                            int maara = Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return false;
                        }
                        return true;
                    }

                    /**
                     * Tarkistaa että aikamääre on oikean pituinen
                     * @param ep aikamääreen editpaneeli
                     * @return onko oikean pituinen jono
                     */
                    private boolean oikeellisuusTarkistus(EditPanel ep) {
                        String s = ep.getText();
                        if (s.length() != 12) return false;
                        int VV; int KK; int PP; int min; int HH;
                        
                        try {
                            VV = Integer.parseInt(s.substring(0, 4));
                            KK = Integer.parseInt(s.substring(4, 6));
                            PP = Integer.parseInt(s.substring(6, 8));
                            HH = Integer.parseInt(s.substring(8, 10));
                            min = Integer.parseInt(s.substring(10, 12));
                        } catch (NumberFormatException e) {
                            return false;
                        }
                        
                        if (VV < 2015) return false;
                        if (KK > 12) return false;
                        if (KK == 0 || PP == 0) return false;
                        if (min > 59 || HH > 23) return false; 
                        
                        if (KK == 1 || KK == 3 || KK == 5 || KK == 7 || KK == 8 || KK == 10 || KK == 12){
                            if (PP > 31) return false;
                        }
                        
                        if (KK == 4 || KK == 6 || KK == 9 || KK == 11){
                            if (PP > 30) return false;
                        }
                        
                        if (KK == 2){
                            if (PP > 28) return false;
                        }
                        
                        return true;
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent arg0){
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    /**
     * Viedään editpaneelien sisällöt merkkijonoina eteenpäin
     * @param ep1 nimen editpaneelin sisältö
     * @param ep2 ajan editpaneelin sisältö
     * @param ep3 kuvauksen editpaneelin sisältö
     * @param ep4 tehtävien määrän editpaneelin sisältö
     */
    public void vieTiedot(EditPanel ep1, EditPanel ep2, EditPanel ep3, EditPanel ep4) {
        String s1 = ep1.getText();
        String s2 = ep2.getText();
        String s3 = ep3.getText();
        String s4 = ep4.getText();
        todoswing.lisaaTeht(s1, s2, s3, s4);
    }
    
}
