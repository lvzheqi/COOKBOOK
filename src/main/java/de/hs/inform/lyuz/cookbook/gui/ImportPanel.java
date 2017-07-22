package de.hs.inform.lyuz.cookbook.gui;

import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ImportPanel extends MyPanel {

    private ArrayList<File> files;
    private DefaultListModel list;

    public static boolean action = false;

    /**
     * Creates new form ImportPanel
     *
     * @param cookConvert
     */
    public ImportPanel(CookMainJFrame cookConvert) {
        super(cookConvert);

        initComponents();
        init();

    }

    private void init() {
        this.files = new ArrayList<>();
        this.list = new DefaultListModel();
        fileList.setModel(list);
    }

    //check ob file is da.
    @Override
    protected void reload() {
        ArrayList<File> filesCheck = new ArrayList<>();
        DefaultListModel listCheck = new DefaultListModel();
        files.stream().filter((f) -> (f.exists())).map((f) -> {
            filesCheck.add(f);
            return f;
        }).forEachOrdered((f) -> {
            listCheck.addElement(f.getName() + "  " + f.getPath());
        });

        if (filesCheck.size() != files.size()) {
            ImportPanel.action = true;
            files = filesCheck;
            list = listCheck;
            fileList.setModel(list);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importPanel = new javax.swing.JPanel();
        fileListSP = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        openBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        openTippSP = new javax.swing.JScrollPane();
        tippTA = new javax.swing.JTextArea();
        toCatBtn = new javax.swing.JButton();
        fileDeleteBtn = new javax.swing.JButton();
        codeCB = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(560, 370));

        fileListSP.setViewportView(fileList);

        openBtn.setText("hinzufügen");
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setText("Liste der importierten Dateien");

        tippTA.setEditable(false);
        tippTA.setBackground(new java.awt.Color(238, 238, 238));
        tippTA.setColumns(2);
        tippTA.setLineWrap(true);
        tippTA.setRows(2);
        tippTA.setText("Tipp: Sie können mehr als eine Datei importieren und dann als eine Datei exportieren.");
        openTippSP.setViewportView(tippTA);

        toCatBtn.setText("vor");
        toCatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toCatBtnActionPerformed(evt);
            }
        });

        fileDeleteBtn.setText("löschen");
        fileDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileDeleteBtnActionPerformed(evt);
            }
        });

        codeCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "UTF-8", "ISO-8859-1" }));
        codeCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeCBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout importPanelLayout = new javax.swing.GroupLayout(importPanel);
        importPanel.setLayout(importPanelLayout);
        importPanelLayout.setHorizontalGroup(
            importPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, importPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(importPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(importPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(importPanelLayout.createSequentialGroup()
                            .addComponent(fileListSP, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31)
                            .addGroup(importPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(importPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(openBtn)
                                    .addComponent(fileDeleteBtn))
                                .addComponent(codeCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel3)
                        .addComponent(openTippSP, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(toCatBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );

        importPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {codeCB, fileDeleteBtn, openBtn});

        importPanelLayout.setVerticalGroup(
            importPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(importPanelLayout.createSequentialGroup()
                .addGroup(importPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(importPanelLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileDeleteBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(codeCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(importPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fileListSP, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(openTippSP, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(toCatBtn)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(importPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 27, Short.MAX_VALUE)
                .addComponent(importPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBtnActionPerformed

        JFileChooser fc = new JFileChooser() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith("mcb") || f.isDirectory()
                        || f.getName().endsWith("cml")
                        || f.getName().endsWith("mm")
                        || f.getName().endsWith("bs");
            }
        };
        fc.setMultiSelectionEnabled(true);
        int result = fc.showOpenDialog(this);

        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                File file = file = fc.getSelectedFile();
                for (int i = 0; i < files.size(); i++) {
                    if (files.get(i).getAbsolutePath().equals(file.getAbsolutePath())) {
                        JOptionPane.showMessageDialog(null, "Die Datei ist bereits vorhanden", "Warnung", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                files.add(file);
                list.addElement(file.getName() + "  " + file.getPath());
                action = true;
                break;
            default:
                break;
        }
    }//GEN-LAST:event_openBtnActionPerformed

    private void toCatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toCatBtnActionPerformed
        if (files.isEmpty()) {
            Object[] options = {"zurück", "nächst"};
            int n = JOptionPane.showOptionDialog(null, "Keine Dateien werden importiert."
                    + "\n Wollen Sie weiter machen?", "Warnung", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == 0) {
                return;
            }
        }
        cookConvert.getCookTabPane().setSelectedComponent(cookConvert.getCategoryPanel());
    }//GEN-LAST:event_toCatBtnActionPerformed


    private void fileDeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileDeleteBtnActionPerformed
        if (fileList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte ein File auswählen", "Warnung", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<String> deleteList = (ArrayList<String>) fileList.getSelectedValuesList();

        for (String dlString : deleteList) {
            list.removeElement((Object) dlString);
            String[] split = dlString.split(" ");
            String name = split[0];
            String path = split[2];
            for(int i =3; i<split.length; i++){
                path += " "+split[i];
            }
            for (File f : files) {
                System.out.println(f.getPath());
                System.out.println(dlString);
                if (f.getPath().equals(path) && f.getName().equals(name)) {
                    files.remove(f);
                    
                    action = true;
                    break;
                }
            }
        }
    }//GEN-LAST:event_fileDeleteBtnActionPerformed

    private void codeCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeCBActionPerformed
        String s = (String) codeCB.getSelectedItem();
        switch(s){
            case "UTF-8":
                FilesUtils.ENCODING = "UTF-8";
                break;
            case "ISO-8859-1":
                FilesUtils.ENCODING = "ISO-8859-1";
                break;
        }
    }//GEN-LAST:event_codeCBActionPerformed

    public ArrayList<File> getFiles() {
        return files;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> codeCB;
    private javax.swing.JButton fileDeleteBtn;
    private javax.swing.JList<String> fileList;
    private javax.swing.JScrollPane fileListSP;
    private javax.swing.JPanel importPanel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton openBtn;
    private javax.swing.JScrollPane openTippSP;
    private javax.swing.JTextArea tippTA;
    private javax.swing.JButton toCatBtn;
    // End of variables declaration//GEN-END:variables

}
