package client;

import client.Dialogs.AddDialog;
import client.Dialogs.DeleteDialog;
import client.Dialogs.SearchDialog;
import library.AddComponent;
import library.Constants;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * Created by alex on 15.3.17.
 */
public class MainWindow {

    private StudentTable studentTable;
    private StudentTable searchPanel;
    private JTextField port;
    private JTextField host;
    private Client client;
    private boolean connect = false;

    public MainWindow() {
        JFrame frame = new JFrame("Таблица общественных работ студентов");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createFileMenu());
        frame.add(createToolBar(), BorderLayout.PAGE_START);
        studentTable = new StudentTable();
        studentTable.setNamePanel(Constants.MAIN_PANEL);
        searchPanel = new StudentTable();
        searchPanel.setNamePanel(Constants.SEARCH_PANEL);
        frame.add(studentTable, BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private JMenuBar createFileMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        Font font = new Font("Verdana", Font.ITALIC, 12);
        fileMenu.setFont(font);


        JMenuItem openItem = new JMenuItem("Открыть");
        openItem.setFont(font);
        fileMenu.add(openItem);
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                fileWorker.openFile();
//                studentTable.updateComponent();
            }
        });

        JMenuItem saveItem = new JMenuItem("Сохранить");
        saveItem.setFont(font);
        fileMenu.add(saveItem);
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                fileWorker.saveFile();
            }
        });

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Выйти");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar.add(fileMenu);


        JMenu table = new JMenu("Таблица");
        table.setFont(font);

        JMenuItem add = new JMenuItem("Добавление");
        add.setFont(font);
        table.add(add);
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDialog();
            }
        });

        JMenuItem delete = new JMenuItem("Удаление");
        delete.setFont(font);
        table.add(delete);
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteDialog();
            }
        });
        JMenuItem search = new JMenuItem("Поиск");
        search.setFont(font);
        table.add(search);
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchDialog();
            }
        });


        menuBar.add(table);
        return menuBar;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        toolBar.add(AddComponent.makeButton(new JButton(), "connect.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame clientDialog = new JFrame("Настройка подключения");
                JToolBar toolBar1 = new JToolBar();
                clientDialog.setVisible(true);
                clientDialog.setSize(400, 20);
                clientDialog.setResizable(false);
                JLabel label = new JLabel("Host: ");
                toolBar1.add(label);
                host = new JTextField("127.0.0.1", 16);
                host.setMaximumSize(new Dimension(160, 20));
                toolBar1.add(host);
                label = new JLabel("Port: ");
                toolBar1.add(label);
                port = new JTextField("1337", 4);

                port.setMaximumSize(new Dimension(50, 20));
                toolBar1.add(port);

                JButton addButton = new JButton("Подключиться");
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        connect();
                    }
                });
                toolBar1.add(addButton);
                clientDialog.add(toolBar1);

            }
        }));
        toolBar.addSeparator();
        toolBar.add(AddComponent.makeButton(new JButton(), "save.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "open.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        }));
        toolBar.addSeparator();
        toolBar.add(AddComponent.makeButton(new JButton(), "search.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchDialog();
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "add.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDialog();
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "delete.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteDialog();
            }
        }));
        return toolBar;
    }

    private void addDialog() {
        if (connect) {
            new AddDialog(studentTable);
        } else {
            JOptionPane.showMessageDialog
                    (null, "Вы не подключины к серверу!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void searchDialog() {
        if (connect) {
            new SearchDialog(searchPanel);
        } else {
            JOptionPane.showMessageDialog
                    (null, "Вы не подключины к серверу!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteDialog() {
        if (connect) {
            new DeleteDialog(studentTable);
        } else {
            JOptionPane.showMessageDialog
                    (null, "Вы не подключины к серверу!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openFile() {
        if (connect) {
            String name = (String) JOptionPane.showInputDialog(null, "Открыть файл",
                    "Открыть файл", JOptionPane.QUESTION_MESSAGE, null, null, "");
            if ((name != null) && (name.length() > 0)) {
                client.sendToServer(Constants.OPEN_FILE);
                client.sendToServer(name);
                studentTable.update();
            }
        } else {
            JOptionPane.showMessageDialog
                    (null, "Вы не подключины к серверу!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveFile() {
        if (connect) {
            String name = (String) JOptionPane.showInputDialog(null, "Сохранить файл",
                    "Сохранить файл", JOptionPane.QUESTION_MESSAGE, null, null, "");
            if ((name != null) && (name.length() > 0)) {
                client.sendToServer(Constants.SAVE_FILE);
                client.sendToServer(name);
            }
        } else {
            JOptionPane.showMessageDialog
                    (null, "Вы не подключины к серверу!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void connect() {
        if (isCorrectHostAndPort()) {
            client = new Client(this, host.getText(), Integer.parseInt(port.getText()));
            studentTable.setClient(client);
            searchPanel.setClient(client);
        } else {
            client.sendToServer(Constants.CLIENT_EXIT);
            connect = false;
            JOptionPane.showMessageDialog
                    (null, "Не корректный Host или Port", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isCorrectHostAndPort() {
        Pattern pHost = Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])");
        Pattern pPort = Pattern.compile("[0-9]{1,5}");
        if (pPort.matcher(port.getText()).matches()) {
            int portInt = Integer.parseInt(port.getText());
            return (pHost.matcher(host.getText()).matches() && 0 <= portInt && portInt <= 65535);
        } else {
            return false;
        }
    }

    public StudentTable getStudentTable() {
        return studentTable;
    }

    public StudentTable getSearchPanel() {
        return searchPanel;
    }

    public Client getClient() {
        return client;
    }


    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    public static void main(String[] args) {
        final MainWindow mainWindow = new MainWindow();
        PropertyConfigurator.configure("log4j.property");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                if (mainWindow.getClient() != null) {
                    mainWindow.getClient().sendToServer(Constants.CLIENT_EXIT);
                }
            }
        }));
    }

}
