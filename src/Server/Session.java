package Server;

import Server.SearchStrategyPackage.*;
import library.Constants;
import library.Student;

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
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Model tableModel;
    private Model searchTableModel;

    Session(Socket socket, Server server) {
        server.log("New session \n");
        tableModel = new Model();
        this.server = server;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            runSession();
        } catch (Exception e) {
            server.log("ERROR.\n");
            e.printStackTrace();
        }
    }

    Model getTableModel() {
        return tableModel;
    }

    void stopSession() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Server getServer() {
        return server;
    }

    private void runSession() throws IOException, ClassNotFoundException {
        server.log("Run session\n");
        String command;
        server.log("Client connected\n");
        while (true) {
            command = (String) inputStream.readObject();
            if (command.equals("Exit")||!server.getRun()) break;
            server.log("New command from client " + command + "\n");
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
                    server.log("Wrong command \n" + command);
                    break;
            }
        }
        server.log("client exit\n");
    }

    private void deleteStudent() throws IOException, ClassNotFoundException {
        server.log("Delete student \n");
        List<Student> searchStudent = new SearchContext(getSearchContext()).executeSearchStrategy(tableModel.getStudents());
        tableModel.getStudents().removeAll(searchStudent);
        sendStudentArray(tableModel);
    }

    private void searchStudent() throws IOException, ClassNotFoundException {
        server.log("Search student \n");
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
        server.log("Add new student " + student.getLastName() + " "
                + student.getFirstName() + "\n");
        tableModel.addStudent(student);
        sendStudentArray(tableModel);
    }

    private void saveFile() throws IOException, ClassNotFoundException {
        String fileName = (String) inputStream.readObject();
        server.log("Try save file " + fileName + "\n");
        FileWorker fileHandler = new FileWorker(this);
        fileHandler.saveFile(fileName);
    }

    private void openFile() throws IOException, ClassNotFoundException {
        String fileName = (String) inputStream.readObject();
        server.log("Try open file " + fileName + "\n");
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
        String typeOfPanel = (String) inputStream.readObject();
        server.log("Command get from " + typeOfPanel + "\n");
        Model table = (typeOfPanel.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.nextPage();
        sendStudentArray(table);
    }

    private void prevPage() throws IOException, ClassNotFoundException {
        String typeOfPanel = (String) inputStream.readObject();
        server.log("Command get from " + typeOfPanel + "\n");
        Model table = (typeOfPanel.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.prevPage();
        sendStudentArray(table);
    }

    private void firstPage() throws IOException, ClassNotFoundException {
        String typeOfPanel = (String) inputStream.readObject();
        server.log("Command get from " + typeOfPanel + "\n");
        Model table = (typeOfPanel.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.firstPage();
        sendStudentArray(table);
    }

    private void lastPage() throws IOException, ClassNotFoundException {
        String typeOfPanel = (String) inputStream.readObject();
        server.log("Command get from " + typeOfPanel + "\n");
        Model table = (typeOfPanel.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        table.lastPage();
        sendStudentArray(table);
    }

    private void changeStudentOnPage() throws IOException, ClassNotFoundException {
        String typeOfPanel = (String) inputStream.readObject();
        server.log("Command get from " + typeOfPanel + "\n");
        Model table = (typeOfPanel.equals(Constants.MAIN_PANEL)) ? tableModel : searchTableModel;
        String change = (String) inputStream.readObject();
        server.log("Change student on page number on " + change + "\n");
        table.setStudentOnPage(Integer.parseInt(change));
        sendStudentArray(table);
    }

    @Override
    public void run() {
        try {
            runSession();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
