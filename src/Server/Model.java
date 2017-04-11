package Server;

import library.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15.3.17.
 */
public class Model {
    public final int SEMESTER_NUMBER = 10;
    private List<Student> students;
    private int currentPage;
    private int studentOnPage ;

    public Model() {
        students = new ArrayList<Student>();
        currentPage = 1;
        studentOnPage = 5;
    }

    public List<Student> getStudents() {
        return students;
    }

    public int getStudentOnPage() {
        return studentOnPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void nextPage(){
        boolean hasNextPage=students.size() > studentOnPage * (currentPage - 1) + studentOnPage;
        if (hasNextPage)
            currentPage++;

    }

    public void prevPage(){
        if (currentPage > 1)
            currentPage--;

    }

    public void firstPage(){
        if (currentPage > 1)
            currentPage = 1;

    }

    public void lastPage(){
        if (currentPage != getNumberMaxPage())
            currentPage = getNumberMaxPage();

    }

    public int getNumberMaxPage() {
        return ((students.size() - 1)/ studentOnPage) + 1;
    }

    public void setStudentOnPage(int studentOnPage) {
        this.studentOnPage = studentOnPage;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }


}
