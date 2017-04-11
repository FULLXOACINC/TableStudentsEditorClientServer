package Server.SearchStrategyPackage;




import library.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 30.3.17.
 */
public class NameAndGroupSearch implements SearchStrategy {
    private String lastName;
    private String group;

    public NameAndGroupSearch(String lastName, String group) {
        this.lastName = lastName;
        this.group = group;
    }

    @Override
    public List<Student> execute(List<Student> students) {
        List<Student> searchStudent= new ArrayList<Student>();
        for(Student student:students){
            boolean correctNameAndGroup = Find.correctName(lastName,student.getLastName()) || Find.correctGroup(group,student.getGroupNumber());

            if(correctNameAndGroup)
                searchStudent.add(student);
        }
        return searchStudent;
    }



}
