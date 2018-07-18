package chess;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextField;

abstract class Configuration extends JFrame {

    Configuration() {
        initComponents();
        setSize(537, 487);
        setLocation(Chess.INITBOUNDS.width / 2 - getWidth() / 2,
                Chess.INITBOUNDS.height / 2 - getHeight() / 2);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                updateValues();
                if (!chkDiffNames() || !(chkName(TF_p1Name) && chkName(TF_p2Name))) {
                    setVisible(true);
                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
                setLocation(Chess.INITBOUNDS.width / 2 - getWidth() / 2,
                        Chess.INITBOUNDS.height / 2 - getHeight() / 2);
            }
        });
    }

    abstract void updateValues();

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        names = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        configPane = new javax.swing.JPanel();

        setTitle("Configuration");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        title.setFont(new java.awt.Font("Segoe Script", 3, 60)); // NOI18N
        title.setForeground(new java.awt.Color(255, 0, 0));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Classic Chess");
        title.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(153, 153, 255), new java.awt.Color(153, 51, 255)));

        content.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(153, 153, 255), new java.awt.Color(153, 51, 255)));

        names.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 1, true));

        TF_p1Name.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        TF_p1Name.setForeground(new java.awt.Color(51, 0, 204));
        TF_p1Name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TF_p1Name.setText("WHITE");
        TF_p1Name.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                checkName(evt);
            }
        });

        TF_p2Name.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        TF_p2Name.setForeground(new java.awt.Color(51, 0, 204));
        TF_p2Name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TF_p2Name.setText("BLACK");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 51, 0));
        jLabel8.setText("Player2 name :");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 51, 0));
        jLabel7.setText("Player1 name :");

        javax.swing.GroupLayout namesLayout = new javax.swing.GroupLayout(names);
        names.setLayout(namesLayout);
        namesLayout.setHorizontalGroup(
            namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namesLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TF_p1Name, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TF_p2Name, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        namesLayout.setVerticalGroup(
            namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(TF_p1Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(TF_p2Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        configPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 1, true));

        optionsPane.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true), "Options", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Comic Sans MS", 1, 14), new java.awt.Color(255, 0, 0))); // NOI18N

        CB_showAnim.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        CB_showAnim.setSelected(true);
        CB_showAnim.setText("Show Animations");
        CB_showAnim.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                CB_showAnimEvent(evt);
            }
        });

        CB_showBorder.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        CB_showBorder.setSelected(true);
        CB_showBorder.setText("Show Border");

        CB_showLast.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        CB_showLast.setSelected(true);
        CB_showLast.setText("Show Last Move");

        CB_showTips.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        CB_showTips.setSelected(true);
        CB_showTips.setText("Show Tips");

        CB_showValid.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        CB_showValid.setSelected(true);
        CB_showValid.setText("Show Valid Moves");

        CB_showCaptured.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        CB_showCaptured.setSelected(true);
        CB_showCaptured.setText("Show Captured Pieces");

        javax.swing.GroupLayout optionsPaneLayout = new javax.swing.GroupLayout(optionsPane);
        optionsPane.setLayout(optionsPaneLayout);
        optionsPaneLayout.setHorizontalGroup(
            optionsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(optionsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CB_showCaptured, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CB_showAnim)
                    .addComponent(CB_showBorder, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CB_showLast, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CB_showTips, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CB_showValid, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        optionsPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {CB_showAnim, CB_showBorder, CB_showCaptured, CB_showLast, CB_showTips, CB_showValid});

        optionsPaneLayout.setVerticalGroup(
            optionsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPaneLayout.createSequentialGroup()
                .addComponent(CB_showAnim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(CB_showBorder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(CB_showLast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(CB_showTips, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(CB_showValid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(CB_showCaptured, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        animPane.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true), "Animation", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Comic Sans MS", 1, 14), new java.awt.Color(255, 0, 0))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("King");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Queen");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Pawn");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Knight");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Bishop");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Rook");

        ChB_king.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ChB_king.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Slow", "Moderate", "Fast", "LightSpeed" }));
        ChB_king.setSelectedIndex(1);

        ChB_queen.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ChB_queen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Slow", "Moderate", "Fast", "LightSpeed" }));
        ChB_queen.setSelectedIndex(2);

        ChB_pawn.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ChB_pawn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Slow", "Moderate", "Fast", "LightSpeed" }));
        ChB_pawn.setSelectedIndex(1);

        ChB_knight.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ChB_knight.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Slow", "Moderate", "Fast", "LightSpeed" }));
        ChB_knight.setSelectedIndex(1);

        ChB_bishop.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ChB_bishop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Slow", "Moderate", "Fast", "LightSpeed" }));
        ChB_bishop.setSelectedIndex(1);

        ChB_rook.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ChB_rook.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Slow", "Moderate", "Fast", "LightSpeed" }));

        javax.swing.GroupLayout animPaneLayout = new javax.swing.GroupLayout(animPane);
        animPane.setLayout(animPaneLayout);
        animPaneLayout.setHorizontalGroup(
            animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animPaneLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(27, 27, 27)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ChB_queen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChB_pawn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChB_knight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChB_bishop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChB_rook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChB_king, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        animPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ChB_bishop, ChB_king, ChB_knight, ChB_pawn, ChB_queen, ChB_rook});

        animPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        animPaneLayout.setVerticalGroup(
            animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animPaneLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ChB_king, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ChB_queen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ChB_pawn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ChB_knight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ChB_bishop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ChB_rook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout configPaneLayout = new javax.swing.GroupLayout(configPane);
        configPane.setLayout(configPaneLayout);
        configPaneLayout.setHorizontalGroup(
            configPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configPaneLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(optionsPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(animPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        configPaneLayout.setVerticalGroup(
            configPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configPaneLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(configPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(animPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionsPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        configPaneLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {animPane, optionsPane});

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(names, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(configPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        contentLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {configPane, names});

        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(names, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(configPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout rootLayout = new javax.swing.GroupLayout(root);
        root.setLayout(rootLayout);
        rootLayout.setHorizontalGroup(
            rootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(content, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rootLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {content, title});

        rootLayout.setVerticalGroup(
            rootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rootLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CB_showAnimEvent(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CB_showAnimEvent
        boolean tf = CB_showAnim.isSelected();
        ChB_bishop.setEnabled(tf);
        ChB_king.setEnabled(tf);
        ChB_knight.setEnabled(tf);
        ChB_pawn.setEnabled(tf);
        ChB_queen.setEnabled(tf);
        ChB_rook.setEnabled(tf);
    }//GEN-LAST:event_CB_showAnimEvent

    private void checkName(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_checkName
        chkName(TF_p1Name);
        chkName(TF_p2Name);
    }//GEN-LAST:event_checkName

    private boolean chkDiffNames() {
        String n1 = TF_p1Name.getText();
        String n2 = TF_p2Name.getText();
        if (n1.equals(n2)) {
            showMessageDialog(this, "Please enter different names.", "Invalid Input.",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean chkName1 = chkName(TF_p1Name);
        boolean chkName2 = chkName(TF_p2Name);
        return (chkName1 && chkName2);
    }

    private boolean chkName(JTextField tf) {
        String name = tf.getText().trim();
        if (name == null || name.isEmpty()) {
            tf.setText("");
            showMessageDialog(this, "Please enter a valid name for player "
                    + (tf == TF_p1Name ? "1" : "2") + ".", "Bad name...", JOptionPane.ERROR_MESSAGE);
            tf.requestFocus();
            return false;
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    final javax.swing.JCheckBox CB_showAnim = new javax.swing.JCheckBox();
    final javax.swing.JCheckBox CB_showBorder = new javax.swing.JCheckBox();
    final javax.swing.JCheckBox CB_showCaptured = new javax.swing.JCheckBox();
    final javax.swing.JCheckBox CB_showLast = new javax.swing.JCheckBox();
    final javax.swing.JCheckBox CB_showTips = new javax.swing.JCheckBox();
    final javax.swing.JCheckBox CB_showValid = new javax.swing.JCheckBox();
    final javax.swing.JComboBox ChB_bishop = new javax.swing.JComboBox();
    final javax.swing.JComboBox ChB_king = new javax.swing.JComboBox();
    final javax.swing.JComboBox ChB_knight = new javax.swing.JComboBox();
    final javax.swing.JComboBox ChB_pawn = new javax.swing.JComboBox();
    final javax.swing.JComboBox ChB_queen = new javax.swing.JComboBox();
    final javax.swing.JComboBox ChB_rook = new javax.swing.JComboBox();
    final javax.swing.JTextField TF_p1Name = new javax.swing.JTextField();
    final javax.swing.JTextField TF_p2Name = new javax.swing.JTextField();
    final javax.swing.JPanel animPane = new javax.swing.JPanel();
    private javax.swing.JPanel configPane;
    final javax.swing.JPanel content = new javax.swing.JPanel();
    final javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    final javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
    final javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
    final javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
    final javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
    final javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel names;
    final javax.swing.JPanel optionsPane = new javax.swing.JPanel();
    final javax.swing.JPanel root = new javax.swing.JPanel();
    final javax.swing.JLabel title = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables
}
