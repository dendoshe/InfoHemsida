/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informatikhemsida;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;

/**
 *
 * @author Diana Jumaili
 */
public class SkapaAnslagFonster extends javax.swing.JFrame {

    /**
     * "anslag" lagrar anslagsdata (anslagstext, rubrik, kategori och fil)
     * ifylld av användare, och skickas sedan till databas
     */
    Map<Object, Object> anslag = new HashMap<>();
    File selectedFile = null;
    int selectedCategory;

    /**
     * Creates new form skapaAnslag2
     */
    public SkapaAnslagFonster() {
        initComponents();
        header.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kategori = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea("Skriv nåt fint");
        header = new javax.swing.JTextField(" Rubrik");
        addFile = new java.awt.Button();
        publishBtn = new java.awt.Button();
        attachmentLbl = new java.awt.Label();
        utbildningBtn = new javax.swing.JRadioButton();
        forskningBtn = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Skapa anslag");

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textAreaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textAreaFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(textArea);

        header.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        header.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                headerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                headerFocusLost(evt);
            }
        });

        addFile.setLabel("📎");
        addFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFileActionPerformed(evt);
            }
        });

        publishBtn.setLabel("Publicera");
        publishBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publishBtnActionPerformed(evt);
            }
        });

        kategori.add(utbildningBtn);
        utbildningBtn.setText("Utbildning");
        utbildningBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                utbildningBtnActionPerformed(evt);
            }
        });

        kategori.add(forskningBtn);
        forskningBtn.setText("Forskning");
        forskningBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forskningBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(header)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(forskningBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(utbildningBtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(addFile, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(attachmentLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(publishBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(forskningBtn)
                    .addComponent(utbildningBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attachmentLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(publishBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFileActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Välj bilaga");

        int alt = fc.showOpenDialog(fc);
        if (alt == JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.getSelectedFile();
            attachmentLbl.setText("" + selectedFile.getName());
        } else if (alt == JFileChooser.CANCEL_OPTION) {
            System.out.println("User cancelled file selection");
        }
    }//GEN-LAST:event_addFileActionPerformed

    private void publishBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishBtnActionPerformed
        anslag.put("AInnehåll", textArea.getText());
        anslag.put("Kategori", selectedCategory);
        anslag.put(" Rubrik", header.getText());
        anslag.put("Fil", selectedFile);
        this.dispose(); //stänger fönstret
        DataAccess da = new DataAccess("Milky", "milkmaster");
        da.laggUppAnslag(anslag);
        da.hamtaFil(1);
    }//GEN-LAST:event_publishBtnActionPerformed

    private void forskningBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forskningBtnActionPerformed
        selectedCategory = 1;
        this.getContentPane().setBackground(new Color(168, 149, 87));
    }//GEN-LAST:event_forskningBtnActionPerformed

    private void utbildningBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_utbildningBtnActionPerformed
        selectedCategory = 2;
        this.getContentPane().setBackground(new Color(176, 142, 148));
    }//GEN-LAST:event_utbildningBtnActionPerformed

    private void headerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_headerFocusGained
        if (header.getText().equals(" Rubrik")) {
            header.setText("");
        }
    }//GEN-LAST:event_headerFocusGained

    private void headerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_headerFocusLost
        if (header.getText().isEmpty()) {
            header.setText(" Rubrik");
        }
    }//GEN-LAST:event_headerFocusLost

    private void textAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textAreaFocusGained
        if (textArea.getText().equals("Skriv nåt fint")) {
            textArea.setText("");
        }
    }//GEN-LAST:event_textAreaFocusGained

    private void textAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textAreaFocusLost
        if (textArea.getText().isEmpty()) {
            textArea.setText("Skriv nåt fint");
        }
    }//GEN-LAST:event_textAreaFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SkapaAnslagFonster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SkapaAnslagFonster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SkapaAnslagFonster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SkapaAnslagFonster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SkapaAnslagFonster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button addFile;
    private java.awt.Label attachmentLbl;
    private javax.swing.JRadioButton forskningBtn;
    private javax.swing.JTextField header;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.ButtonGroup kategori;
    private java.awt.Button publishBtn;
    private javax.swing.JTextArea textArea;
    private javax.swing.JRadioButton utbildningBtn;
    // End of variables declaration//GEN-END:variables
}
