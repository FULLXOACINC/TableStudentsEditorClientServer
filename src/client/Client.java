package client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import library.Constants;
import library.Student;
import org.apache.log4j.Logger;
/**
 * Created by alex on 4.4.17.
 */
public class Client {

    private static final Logger log = Logger.getLogger(Client.class);
    private MainWindow mainWindow;
    private StudentTable studentTable;
    private StudentTable searchPanel;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String host;
    private Socket socket;
    private int port;

    Client(MainWindow mainWindow, String host, int port) {
        this.mainWindow = mainWindow;
        studentTable = mainWindow.getStudentTable();
        searchPanel = mainWindow.getSearchPanel();
        this.host = host;
        this.port = port;
        this.socket = null;
        createSocket();
        if(this.socket == null)
            return;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.error("Не удалось установить соединение ");
            JOptionPane.showMessageDialog
                    (null, "Не удалось установить соединение ", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendToServer(Object object){
        try {
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            log.error("Информация не прислана с сервера ");
            JOptionPane.showMessageDialog
                    (null, "Информация не прислана с сервера ", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    void getUpdatePanel(String where) {
        if (where.equals(Constants.MAIN_PANEL)) {
            getUpdatePanel(studentTable);
        } else {
            getUpdatePanel(searchPanel);
        }
    }

    private void getUpdatePanel(StudentTable studentTable) {
        try {
            studentTable.getTableModel().setStudents((List<Student>) inputStream.readObject());
            studentTable.setStudentSize((int) inputStream.readObject());
            studentTable.setCurrentPage((int) inputStream.readObject());
            studentTable.setStudentOnPage((int) inputStream.readObject());
        } catch (Exception e) {
            log.info("Не удалось прочитать информацию ");
            JOptionPane.showMessageDialog
                    (null, "Не удалось прочитать информацию ", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createSocket() {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(3000);
            mainWindow.setConnect(true);
            JOptionPane.showMessageDialog
                    (null, "Вы подключились к " + host + ":" + port, "INFO", JOptionPane.INFORMATION_MESSAGE);
        } catch (UnknownHostException e) {
            log.error("Неизвестный Host" + host);
            JOptionPane.showMessageDialog
                    (null, "Неизвестный Host " + host, "ERROR", JOptionPane.ERROR_MESSAGE);
            mainWindow.setConnect(false);
        } catch (IOException e) {
            log.error("Ошибка при создании соединения " + host + ":" + port);
            JOptionPane.showMessageDialog
                    (null, "Ошибка при создании соединения " + host + ":" + port, "ERROR", JOptionPane.ERROR_MESSAGE);
            mainWindow.setConnect(false);
        }
    }

}