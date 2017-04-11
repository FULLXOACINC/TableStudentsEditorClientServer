package client.Dialogs;


import client.StudentTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by alex on 16.3.17.
 */
public class DeleteDialog {

    private JFrame frame;
    private Dialog dialog;
    private StudentTable mainTable;

    public DeleteDialog(StudentTable studentTable) {
        this.mainTable = studentTable;
        dialog = new Dialog("Удаление студентов", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        frame = dialog.getFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }


    private void deleteStudent() {
        if (!dialog.getLastName().equals("")) {
            int countStudent=mainTable.getTableModel().getStudents().size();
            mainTable.getClient().sendToServer("DELETE_MODE");
            mainTable.getClient().sendToServer(dialog.getLastName());
            mainTable.getClient().sendToServer(dialog.getGroup());
            mainTable.getClient().sendToServer(dialog.getSocialWork());
            mainTable.getClient().sendToServer(dialog.getMinCount());
            mainTable.getClient().sendToServer(dialog.getMaxCount());

            mainTable.update();
            int countDeleteStudent=countStudent-mainTable.getTableModel().getStudents().size();
            if (countDeleteStudent>0) {
                JOptionPane.showMessageDialog
                        (null, "Удалено " + countDeleteStudent + " студентов", "Информация", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog
                        (null, "Студент не найден", "Внимание", JOptionPane.WARNING_MESSAGE);
            }
        }
    }



}

