package client.Dialogs;





import library.AddComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by alex on 30.3.17.
 */
public class Dialog  {
    private final String LAST_NAME = "Фамилия:";
    private final String GROUP = "Группа:";
    private final String SOCIAL_WORK = "Общественная работа:";
    private final String COUNT_OF_SOCIAL_WORK = "Каличество общественной работы:";
    private JTextField lastName;
    private JTextField group;
    private JComboBox minCount;
    private JComboBox maxCount;

    private JTextField socialWork;
    private JFrame frame;

    public Dialog(String dialogType,ActionListener actionListener) {
        frame = new JFrame(dialogType);
        JLabel labelText = new JLabel();
        JPanel jPanelID = new JPanel();
        jPanelID.setLayout(new GridBagLayout());
        labelText.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText, 0, 0, 3, 1);

        String[] labelString = {LAST_NAME, GROUP,SOCIAL_WORK, COUNT_OF_SOCIAL_WORK};
        labelText = new JLabel(labelString[0]);
        AddComponent.add(jPanelID,labelText, 0, 1, 1, 1);

        lastName = new JTextField(30);
        AddComponent.add(jPanelID, lastName, 1, 1, 3, 1);
        labelText = new JLabel(labelString[1]);
        AddComponent.add(jPanelID, labelText, 0, 2, 1, 1);

        group = new JTextField(30);
        AddComponent.add(jPanelID, group, 1, 2, 3, 1);
        labelText = new JLabel(labelString[2]);
        AddComponent.add(jPanelID, labelText, 0, 3, 1, 1);

        socialWork = new JTextField(30);
        AddComponent.add(jPanelID, socialWork, 1, 3, 3, 1);
        String[] markString = {"-","1","2","3", "4", "5", "6", "7", "8", "9", "10"};
        labelText = new JLabel(labelString[3]);
        labelText.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText, 0, 4, 1, 1);
        minCount = new JComboBox(markString);
        AddComponent.add(jPanelID, minCount, 1, 4, 1, 1);
        maxCount = new JComboBox(markString);
        AddComponent.add(jPanelID, maxCount, 2, 4, 1, 1);

        frame.add(jPanelID, BorderLayout.NORTH);
        JButton deleteButton = new JButton(dialogType);
        deleteButton.addActionListener(actionListener);
        frame.add(deleteButton, BorderLayout.SOUTH);
    }

    public JFrame getFrame() {
        return frame;
    }
    public String getLastName() {
        return lastName.getText();
    }

    public String getGroup() {
        return group.getText();
    }

    public String getMinCount() {
        return String.valueOf(minCount.getSelectedItem());
    }

    public String getMaxCount() {
        return String.valueOf(maxCount.getSelectedItem());
    }

    public String getSocialWork() {
        return socialWork.getText();
    }



}
