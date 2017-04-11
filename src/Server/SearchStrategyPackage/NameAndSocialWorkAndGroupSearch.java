package Server.SearchStrategyPackage;





import library.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 31.3.17.
 */
public class NameAndSocialWorkAndGroupSearch implements SearchStrategy {
    private String lastName;
    private String group;
    private String minCount;
    private String maxCount;
    private String socialWork;
    public NameAndSocialWorkAndGroupSearch(String lastName, String group, String minCount, String maxCount, String socialWork){

        this.lastName = lastName;
        this.group = group;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.socialWork = socialWork;
    }

    @Override
    public List<Student> execute(List<Student> students) {
        List<Student> searchStudent= new ArrayList<Student>();
        for(Student student:students){
            boolean correctNameAndGroupAndSocialWork =Find.correctName(lastName,student.getLastName())|| Find.correctGroup(group,student.getGroupNumber())|| Find.findSocialWorkBitweenMinAndMax(socialWork,student.getSocialWork(), minCount, maxCount);

            if(correctNameAndGroupAndSocialWork)
                searchStudent.add(student);
        }

        return searchStudent;
    }
}
