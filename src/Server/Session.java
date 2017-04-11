package Server;

import Server.SearchStrategyPackage.*;
import library.*;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 4.4.17.
 */
public class Session implements Runnable {

    private Server server;
    private JTextArea jTextArea;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Model tableModel;
    private Model searchTableModel;

    public Session(Socket socket, Server server) {
        jTextArea = server.getTextArea();
        jTextArea.append("New session \n");
        tableModel = new Model();
        this.server = server;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            runSession();
        } catch (Exception e) {
            jTextArea.append("ERROR.\n");
            e.printStackTrace();
        }
    }

    public void runSession() throws IOException, ClassNotFoundException {
        jTextArea.append("Run session\n");
        String command;
        jTextArea.append("Client connected\n");
        while (server.getRun()) {
            command = (String) inputStream.readObject();
            if (command.equals("Exit")) break;
            jTextArea.append("New command from client " + command + "\n");
            switch (command) {
                case Constants.OPEN_FILE:
                    openFile();
                    break;
                case Constants.SAVE_FILE:
                    saveFile();
                    break;
                case Constants.ADD_STUDENT:
                    addStudent();
                    break;
                case Constants.NEXT_PAGE:
                    nextPage();
                    break;
                case Constants.PREV_PAGE:
                    prevPage();
                    break;
                case Constants.LAST_PAGE:
                    lastPage();
                    break;
                case Constants.FIRST_PAGE:
                    firstPage();
                    break;
                case Constants.SEARCH_MODE:
                    searchStudent();
                    break;
                case Constants.DELETE_MODE:
                    deleteStudent();
                    break;
                case Constants.CHANGE_NUMBER_STUDENT_ON_PAGE:
                    changeStudentOnPage();
                    break;
                default:
                    jTextArea.append("Wrong command " + command);
                    break;
            }
        }
        server.getTextArea().append("Client exit\n");
    }

    private void deleteStudent() throws IOException, ClassNotFoundException {
        jTextArea.append("Delete student... \n");
        List<Student> searchStudent = new SearchContext(getSearchContext()).executeSearchStrategy(tableModel.getStudents());
        tableModel.getStudents().removeAll(searchStudent);
        sendStudentArray(tableModel);
    }

    private void searchStudent() throws IOException, ClassNotFoundException {
        jTextArea.append("Search student... \n");
        searchTableModel = new Model();
        List<Student> searchStudent = new SearchContext(getSearchContext()).executeSearchStrategy(tableModel.getStudents());
        for (Student student : searchStudent)
            searchTableModel.addStudent(student);
        sendStudentArray(searchTableModel);
    }

    private SearchStrategy getSearchContext() throws IOException, ClassNotFoundException {
        String lastName = (String) inputStream.readObject();
        String group = (String) inputStream.readObject();
        String socialWork = (String) inputStream.readObject();
        String minCount = (String) inputStream.readObject();
        String maxCount = (String) inputStream.readObject();
        if (group.equals("") && socialWork.equals(""))
            return new NameSearch(lastName);
        if (socialWork.equals(""))
            return new NameAndGroupSearch(lastName, group);
        if (group.equals(""))
            return new NameAndSocialWorkSearch(lastName, socialWork, minCount, maxCount);
        return new NameAndSocialWorkAndGroupSearch(lastName, group, socialWork, minCount, maxCount);

    }

    private void addStudent() throws IOException, ClassNotFoundException {
        Student student = (Student) inputStream.readObject();
        jTextArea.append("Add new student " + student.getLastName() + " "
                + student.getFirstName() + "\n");
        tableModel.addStudent(student);
        sendStudentArray(tableModel);
    }

    private void saveFile() throws IOException, ClassNotFoundException {
        String fileName = (String) inputStream.readObject();
        jTextArea.append("Try save file " + fileName + "\n");
        FileWorker fileHandler = new FileWorker(this);
        fileHandler.saveFile(fileName);
    }

    private void openFile() throws IOException, ClassNotFoundException {
        String fileName = (String) inputStream.readObject();
        jTextArea.append("Try open file " + fileName + "\n");
        FileWorker fileHandler = new FileWorker(this);
        fileHandler.openXMLFile(fileName);
        sendStudentArray(tableModel);
    }

    private void sendStudentArray(Model tableModel) throws IOException {
        List<Student> sendStudents = new ArrayList<Student>();
        List<Student> students = tableModel.getStudents();
        int currentPage = tableModel.getCurrentPage();
        int studentOnPage = tableModel.getStudentOnPage();

        int firstStudentOnPage = studentOnPage * (currentPage - 1);
        for (int numberStudent = firstStudentOnPage;
             numberStudent < firstStudentOnPage + studentOnPage && numberStudent < students.size();
             numberStudent++) {
            sendStudents.add(students.get(numberStudent));
        }
        outputStream.writeObject(sendStudents);
        outputStream.writeObject(students.size());
        outputStream.writeObject(currentPage);
        outputStream.writeObject(studentOnPage);
        outputStream.flush();
    }

    private void nextPage() throws IOException, ClassNotFoundException {
        String where = (String) inputStream.readObject();
        jTextArea.append("Command get from " + where + "\n");
        Model table = (where.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.nextPage();
        sendStudentArray(table);
    }

    private void prevPage() throws IOException, ClassNotFoundException {
        String where = (String) inputStream.readObject();
        jTextArea.append("Command get from " + where + "\n");
        Model table = (where.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.prevPage();
        sendStudentArray(table);
    }

    private void firstPage() throws IOException, ClassNotFoundException {
        String where = (String) inputStream.readObject();
        jTextArea.append("Command get from " + where + "\n");
        Model table = (where.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.firstPage();
        sendStudentArray(table);
    }

    private void lastPage() throws IOException, ClassNotFoundException {
        String where = (String) inputStream.readObject();
        jTextArea.append("Command get from " + where + "\n");
        Model table = (where.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.lastPage();
        sendStudentArray(table);
    }

    private void changeStudentOnPage() throws IOException, ClassNotFoundException {
        String where = (String) inputStream.readObject();
        jTextArea.append("Command get from " + where + "\n");
        Model table = (where.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        String change = (String) inputStream.readObject();
        jTextArea.append("Change student on page number on" + change + "\n");
        table.setStudentOnPage(Integer.parseInt(change));
        sendStudentArray(table);
    }

    public JTextArea getTextArea() {
        return jTextArea;
    }

    @Override
    public void run() {
        try {
            runSession();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Model getTableModel() {
        return tableModel;
    }

    public void stopSession(){
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
