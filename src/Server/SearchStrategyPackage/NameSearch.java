package Server.SearchStrategyPackage;




import library.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 16.3.17.
 */
public class NameSearch implements SearchStrategy {
    private String lastName;

    public NameSearch(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public List<Student> execute(List<Student> students) {
        List<Student> searchStudent= new ArrayList<Student>();
        for(Student student:students)
            if(Find.correctName(lastName,student.getLastName()))
                searchStudent.add(student);
        return searchStudent;
    }



}
