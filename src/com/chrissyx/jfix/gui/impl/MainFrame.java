package com.chrissyx.jfix.gui.impl;

import com.chrissyx.jfix.gui.MainView;
import com.chrissyx.jfix.modules.GuiController;
import com.chrissyx.jfix.modules.LangController;
import com.chrissyx.jfix.modules.util.ResourceUtils;

/**
 * The main window of this application.
 *
 * @author Chrissyx
 */
public class MainFrame extends javax.swing.JFrame implements MainView
{
    /**
     * Creates new main frame.
     */
    public MainFrame()
    {
        this.initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstLocalFiles = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstRemoteFiles = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaLog = new javax.swing.JTextArea();
        btnConnect = new javax.swing.JToggleButton();
        btnOptions = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnFixIt = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JFix");
        setIconImage(ResourceUtils.getImage("icon.png"));
        setName("mainFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        lstLocalFiles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstLocalFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstLocalFilesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstLocalFiles);

        lstRemoteFiles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstRemoteFiles.setEnabled(false);
        lstRemoteFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstRemoteFilesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstRemoteFiles);

        txtAreaLog.setColumns(20);
        txtAreaLog.setFont(new java.awt.Font("Monospaced", 0, 10));
        txtAreaLog.setRows(5);
        jScrollPane3.setViewportView(txtAreaLog);

        btnConnect.setText(LangController.getInstance().getString("connect"));
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        btnOptions.setText(LangController.getInstance().getString("options"));
        btnOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsActionPerformed(evt);
            }
        });

        btnExit.setText(LangController.getInstance().getString("exit"));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnFixIt.setText(LangController.getInstance().getString("fix_it"));
        btnFixIt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFixItActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(btnOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(btnFixIt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(btnConnect, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFixIt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOptions)
                        .addGap(4, 4, 4)
                        .addComponent(btnExit))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnConnectActionPerformed
    {//GEN-HEADEREND:event_btnConnectActionPerformed
        this.btnConnect.setSelected(this.btnConnect.isSelected() ? GuiController.getInstance().onConnect() : !GuiController.getInstance().onDisconnect());
        this.lstRemoteFiles.setEnabled(this.btnConnect.isSelected());
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnFixItActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFixItActionPerformed
    {//GEN-HEADEREND:event_btnFixItActionPerformed
        GuiController.getInstance().onFixFiletimes();
    }//GEN-LAST:event_btnFixItActionPerformed

    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOptionsActionPerformed
    {//GEN-HEADEREND:event_btnOptionsActionPerformed
        GuiController.getInstance().onShowOptions();
    }//GEN-LAST:event_btnOptionsActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExitActionPerformed
    {//GEN-HEADEREND:event_btnExitActionPerformed
        GuiController.getInstance().onClose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        GuiController.getInstance().onClose();
    }//GEN-LAST:event_formWindowClosed

private void lstLocalFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstLocalFilesMouseClicked
    if(evt.getClickCount() == 2)
        GuiController.getInstance().onChangeLocalDir(this.lstLocalFiles.getModel().getElementAt(this.lstLocalFiles.locationToIndex(evt.getPoint())).toString());
}//GEN-LAST:event_lstLocalFilesMouseClicked

private void lstRemoteFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstRemoteFilesMouseClicked
    if(evt.getClickCount() == 2 && this.lstRemoteFiles.getModel().getSize() != 0)
        GuiController.getInstance().onChangeRemoteDir(this.lstRemoteFiles.getModel().getElementAt(this.lstRemoteFiles.locationToIndex(evt.getPoint())).toString());
}//GEN-LAST:event_lstRemoteFilesMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnConnect;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnFixIt;
    private javax.swing.JButton btnOptions;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList lstLocalFiles;
    private javax.swing.JList lstRemoteFiles;
    private javax.swing.JTextArea txtAreaLog;
    // End of variables declaration//GEN-END:variables

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeFrame()
    {
        this.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showFrame()
    {
        this.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendLogEntry(final String logMessage)
    {
        this.txtAreaLog.append(logMessage + "\n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearLogConsole()
    {
        this.txtAreaLog.setText(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalFileList(final String[] filenames)
    {
        this.lstLocalFiles.setListData(filenames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRemoteFileList(final String[] filenames)
    {
        this.lstRemoteFiles.setListData(filenames);
    }
}
