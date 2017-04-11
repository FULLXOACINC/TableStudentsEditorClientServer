package client;



import library.AddComponent;
import library.Constants;
import library.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 15.3.17.
 */
public class StudentTable extends JComponent {

    private TableModel tableModel;
    private JScrollPane scrollTable;
    private int currentPage = 1;
    private int studentOnPage = 5;
    private Client client;
    private String namePanel;
    private int studentSize;

    public StudentTable() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tableModel = new TableModel();
        makePanel();
    }

    private void makePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(makeTable(), BorderLayout.NORTH);
        scrollTable = new JScrollPane(tablePanel);
        scrollTable.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
                updateScrollTable();
            }
        });
        add(scrollTable);
        add(makeToolsPanel());
    }

    private JPanel makeTable() {
        JPanel table = new JPanel();
        table.setLayout(new GridBagLayout());
        List<Student> students = tableModel.getStudents();
        add(table, "ФИО", 0, 0, 1, 3);
        add(table, "Группа", 1, 0, 1, 3);
        add(table, "Общественная работа", 2, 0, tableModel.SEMESTER_NUMBER * 2, 1);
        for (int i = 0, x = 2; i < tableModel.SEMESTER_NUMBER; i++, x++) {
            add(table, (i + 1) + " сем.", x, 2, 1, 1);
        }
        int lineInHeaderTable = 3;
        for (int y = lineInHeaderTable;y<students.size()+lineInHeaderTable;y++) {
            for (int i = 0; i < tableModel.SEMESTER_NUMBER + 2; i++) {
                String write = getFieldForStudent(students.get(y-lineInHeaderTable), i);
                add(table, write, i, y, 1, 1);
            }
        }
        add(table, "Страница:" + currentPage + "/" + getNumberMaxPage() + " Студентов на странице:" + tableModel.getStudents().size() + " Всего студентов:" + studentSize, 0, studentOnPage + lineInHeaderTable, tableModel.SEMESTER_NUMBER * 2, 3);
        return table;
    }

    private JToolBar makeToolsPanel() {

        JToolBar panel = new JToolBar();
        panel.add(AddComponent.makeButton(new JButton(), "first.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firstPage();
                updateComponent();
            }
        }));

        panel.add(AddComponent.makeButton(new JButton(), "last.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lastPage();
                updateComponent();
            }
        }));
        panel.add(AddComponent.makeButton(new JButton(), "prev.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prevPage();
                updateComponent();
            }
        }));
        panel.add(AddComponent.makeButton(new JButton(), "next.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextPage();
                updateComponent();
            }
        }));
        String[] studentsOnPage = {"5", "10", "50"};
        JComboBox sizeBox = new JComboBox(studentsOnPage);
        sizeBox.setSelectedIndex(Arrays.asList(studentsOnPage).indexOf(Integer.toString(studentOnPage)));
        sizeBox.setMaximumSize(new Dimension(70, 100));
        sizeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String change = (String) cb.getSelectedItem();
                if (studentOnPage != Integer.parseInt(change))
                    chengeStudentOnPage(change);
                studentOnPage=Integer.parseInt(change);
                firstPage();
            }
        });

        panel.add(sizeBox);
        return panel;
    }

    private String getFieldForStudent(Student student, int i) {
        if (i == 0) return student.getLastName() + " " + student.getFirstName() + " " + student.getFatherName();
        else if (i == 1) return student.getGroupNumber();
        else {
            return student.getSocialWork().get(i - 2).getWork();
        }
    }

    private int getNumberMaxPage() {
        return (int)((studentSize - 1)/ studentOnPage) + 1;
    }

    public void nextPage() {
            client.sendToServer(Constants.NEXT_PAGE);
            client.sendToServer(getNamePanel());
            update();
    }

    public void chengeStudentOnPage(String size) {
        client.sendToServer(Constants.CHANGE_NUMBER_STUDENT_ON_PAGE);
        client.sendToServer(getNamePanel());
        client.sendToServer(size);
        update();
    }

    public void prevPage() {
        client.sendToServer(Constants.PREV_PAGE);
        client.sendToServer(getNamePanel());
        update();
    }

    public void firstPage() {
        if (currentPage > 1) {
            client.sendToServer(Constants.FIRST_PAGE);
            client.sendToServer(getNamePanel());
            update();
        }
    }

    public void lastPage() {
        client.sendToServer(Constants.LAST_PAGE);
        client.sendToServer(getNamePanel());
        update();
    }
    public void updatePanel(){
        client.getUpdatePanel(namePanel);
    }

    public void update(){
        updatePanel();
        updateComponent();
    }

    public void updateComponent(){
        removeAll();
        makePanel();
        revalidate();
        repaint();
    }

    private void updateScrollTable() {
        scrollTable.revalidate();
        scrollTable.repaint();
    }

    private void add(JPanel panel, String nameLabel, int gridX, int gridY, int gridWidth, int gridHeight) {
        JLabel label = new JLabel(nameLabel);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        label.setHorizontalAlignment(JLabel.CENTER);
        GridBagConstraints gridBagConstraints = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(label, gridBagConstraints);
    }

    public String getNamePanel() {
        return namePanel;
    }

    public void setNamePanel(String namePanel) {
        this.namePanel = namePanel;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public TableModel getTableModel() {
        return tableModel;
    }

    public void setStudentSize(int studentSize) {
        this.studentSize = studentSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setStudentOnPage(int studentOnPage) {
        this.studentOnPage = studentOnPage;
    }

    public Client getClient() {
        return client;
    }
}
