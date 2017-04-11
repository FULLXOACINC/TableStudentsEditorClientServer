package client.Dialogs;



import client.StudentTable;
import client.TableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by alex on 29.3.17.
 */
public class SearchDialog {

    private final Dialog dialog;
    private TableModel tableModel;
    private JFrame frame;
    private StudentTable searchStudentTable;

    public SearchDialog(StudentTable searchStudentTable) {
        this.tableModel = searchStudentTable.getTableModel();
        this.searchStudentTable=searchStudentTable;
        dialog = new Dialog("Поиск студентов", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        frame = dialog.getFrame();
        frame.add(searchStudentTable, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(new Dimension(850, 600));
    }


    private void searchStudent() {
        if (!dialog.getLastName().equals("")) {

            searchStudentTable.getClient().sendToServer("SEARCH_MODE");
            searchStudentTable.getClient().sendToServer(dialog.getLastName());
            searchStudentTable.getClient().sendToServer(dialog.getGroup());
            searchStudentTable.getClient().sendToServer(dialog.getSocialWork());
            searchStudentTable.getClient().sendToServer(dialog.getMinCount());
            searchStudentTable.getClient().sendToServer(dialog.getMaxCount());

            searchStudentTable.update();
            frame.add(searchStudentTable, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();

        } else {
            JOptionPane.showMessageDialog
                    (null, "Информация не корректна", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


}
