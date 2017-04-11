package Server.SearchStrategyPackage;




import library.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 30.3.17.
 */
public class NameAndSocialWorkSearch implements SearchStrategy {
    private String lastName;
    private String minCount;
    private String maxCount;
    private String socialWork;

    public NameAndSocialWorkSearch(String lastName, String minCount, String maxCount, String socialWork) {
        this.lastName = lastName;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.socialWork = socialWork;
    }

    @Override
    public List<Student> execute(List<Student> students) {
        List<Student> searchStudent= new ArrayList<Student>();
        for(Student student:students){
            boolean correctNameAndSocialWork = Find.correctName(lastName,student.getLastName()) || Find.findSocialWorkBitweenMinAndMax(socialWork,student.getSocialWork(), minCount, maxCount);

            if(correctNameAndSocialWork)
                searchStudent.add(student);
        }

        return searchStudent;
    }


}
