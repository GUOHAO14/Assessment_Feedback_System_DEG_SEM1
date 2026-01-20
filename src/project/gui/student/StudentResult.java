/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.student;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import project.roles.Assessment;
import project.roles.IntakeModule;
import project.roles.Lecturer;
import project.roles.Student;
import project.roles.StudentGradeAndComment;
import project.roles.StudentScore;
import project.utils.FrameFormat;
import project.utils.InteractTxt;
import project.utils.Tools;

/**
 *
 * @author joshl
 */
public class StudentResult extends FrameFormat {
    private Student sessionStudent;
    private String intakeId;
    /**
     * Creates new form StudentScore
     */
public class MultiLineTableCellRenderer extends JTextArea
        implements TableCellRenderer {

    public MultiLineTableCellRenderer() {
        super();                // important
        setLineWrap(true);      // ✅ valid
        setWrapStyleWord(true); // ✅ valid
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        setText(value == null ? "" : value.toString());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        setSize(
            table.getColumnModel().getColumn(column).getWidth(),
            getPreferredSize().height
        );

        int rowHeight = getPreferredSize().height;
        if (table.getRowHeight(row) != rowHeight) {
            table.setRowHeight(row, rowHeight);
        }

        return this;
    }
}
    public StudentResult(Student student) {
        initComponents();
        this.sessionStudent = student;
        this.intakeId = student.getIntakeId();
        
        setupTables();
        loadRegisteredClasses();
        setupClassSelectionListener();
    }
   private void setupTables() {

    /* ===============================
       CLASS TABLE (jTable1)
       =============================== */
    jTable1.setModel(new DefaultTableModel(
        new Object[][]{},
        new String[]{"Class", "Lecturer", "Comment", "Grade", "Score"}
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    });

    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    jTable1.getTableHeader().setReorderingAllowed(false);

    // Disable column resizing
    for (int i = 0; i < jTable1.getColumnModel().getColumnCount(); i++) {
        jTable1.getColumnModel().getColumn(i).setResizable(false);
    }

    /* ===============================
       ASSESSMENT TABLE (jTable2)
       =============================== */
    jTable2.setModel(new DefaultTableModel(
        new Object[][]{},
        new String[]{"Assessment", "Marks", "Full Marks", "Feedback"}
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    });

    jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    jTable2.getTableHeader().setReorderingAllowed(false);

    // Disable column resizing
    for (int i = 0; i < jTable2.getColumnModel().getColumnCount(); i++) {
        jTable2.getColumnModel().getColumn(i).setResizable(false);
    }

    /* ===============================
       MULTI-LINE RENDERER
       =============================== */
    MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();

    // jTable1 multiline columns
    jTable1.getColumnModel().getColumn(2).setCellRenderer(renderer); // Comment
    jTable1.getColumnModel().getColumn(3).setCellRenderer(renderer); // Grade
    jTable1.getColumnModel().getColumn(4).setCellRenderer(renderer); // Score

    // jTable2 multiline column
    jTable2.getColumnModel().getColumn(3).setCellRenderer(renderer); // Feedback
}


private void loadRegisteredClasses() {

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (project.roles.Class cls : sessionStudent.Stu_Classes) {

        Lecturer lec = InteractTxt.checkLecID(cls.getLecId());

        String comment = Tools.getSpecificComment(cls, sessionStudent);
        String grade   = Tools.getSpecificGrade(cls, sessionStudent);

        IntakeModule matchedIM = null;
        for (IntakeModule im : InteractTxt.allIntakeModule) {
            if (im.IM_Classes.contains(cls)) {
                matchedIM = im;
                break;
            }
        }

        String score = "NA";
        if (matchedIM != null) {
            score = Tools.calcStuScore(matchedIM, sessionStudent);
        }

        model.addRow(new Object[]{
            cls.getClassName(),
            lec != null ? lec.getName() : "N/A",
            comment,
            grade,
            score
        });
    }
}


private void setupClassSelectionListener() {

    jTable1.getSelectionModel().addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()) {

            int row = jTable1.getSelectedRow();
            if (row == -1) return;

            String className =
                    jTable1.getValueAt(row, 0).toString();

            loadAssessmentsForClass(className);
        }
    });
}
private void loadAssessmentsForClass(String className) {

    DefaultTableModel model =
            (DefaultTableModel) jTable2.getModel();

    model.setRowCount(0);

    project.roles.Class selectedClass = null;

    for (project.roles.Class c : InteractTxt.allClass) {
        if (c.getClassName().equals(className)) {
            selectedClass = c;
            break;
        }
    }

    if (selectedClass == null) return;

    IntakeModule im = null;

    for (IntakeModule x : InteractTxt.allIntakeModule) {
        if (x.IM_Classes.contains(selectedClass)) {
            im = x;
            break;
        }
    }

    if (im == null) return;

    for (Assessment ass : im.IM_Assessments) {

        StudentScore ss = null;

        for (StudentScore s : sessionStudent.Stu_Scores) {
            if (s.getAssessment().equals(ass)) {
                ss = s;
                break;
            }
        }

        String marks = "-";
        String fullMarks = "-";
        String feedback = "-";

        // Marks2 and FullMarks2 from Assessment
        String marks2 = extractNumber(ass.getAssFullMarks());
        String fullMarks2 = extractNumber(ass.getAssPercentage());

        if (ss != null) {
            marks = ss.getOrginalScore() + "/" + marks2;
            fullMarks = ss.getOriginalFullMarks() + "/" + fullMarks2;
            feedback = ss.getFeedback();
        }

        model.addRow(new Object[]{
            ass.getAssName(),
            marks,
            fullMarks,
            feedback
        });
    }
}

private String extractNumber(String text) {
    if (text == null) return "-";
    return text.split(" ")[0]; // gets "60" from "60 (Marks 2)"
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton1.setText("Home");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Comment");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jButton1)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    new StudentHomepage(sessionStudent).setVisible(true);

    // close current page
    this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
int selectedRow = jTable1.getSelectedRow();

    // 1️⃣ No class selected
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select a class first.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // 2️⃣ Get class name from table
    String className = jTable1.getValueAt(selectedRow, 0).toString();

    project.roles.Class selectedClass = null;
    for (project.roles.Class c : InteractTxt.allClass) {
        if (c.getClassName().equals(className)) {
            selectedClass = c;
            break;
        }
    }

    if (selectedClass == null) {
        JOptionPane.showMessageDialog(
                this,
                "Class not found.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // 3️⃣ Check existing comment
//    String existingComment = Tools.getSpecificComment(selectedClass, sessionStudent);

//    if (!existingComment.equalsIgnoreCase("NA")) {
//        JOptionPane.showMessageDialog(
//                this,
//                "Comment already submitted.\nYou cannot edit it.",
//                "Not Allowed",
//                JOptionPane.INFORMATION_MESSAGE
//        );
//        return;
//    }

    // 4️⃣ Input dialog (multi-line)
    JTextArea textArea = new JTextArea(5, 30);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    int input = JOptionPane.showConfirmDialog(
            this,
            new JScrollPane(textArea),
            "Enter Comment",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (input != JOptionPane.OK_OPTION) return;

    String newComment = textArea.getText().trim();

    if (newComment.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Comment cannot be empty.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // 5️⃣ Final confirmation
    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to submit this comment?\n\nThis action cannot be undone.",
            "Confirm Submission",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    // 6️⃣ Update in-memory object ONLY
    boolean updated = false;

    for (StudentGradeAndComment gc : sessionStudent.GradesAndComments) {
        if (gc.getStuClass().equals(selectedClass)) {
            gc.setComment(newComment);
            updated = true;
            break;
        }
    }

    if (!updated) {
        JOptionPane.showMessageDialog(
                this,
                "Unable to save comment.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // 7️⃣ Save using EXISTING database system
    InteractTxt.saveDatabase();

    // 8️⃣ Refresh table
    loadRegisteredClasses();
    jTable1.revalidate();
    jTable1.repaint();

    JOptionPane.showMessageDialog(
            this,
            "Comment submitted successfully.",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
    );
    }//GEN-LAST:event_jButton2ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
