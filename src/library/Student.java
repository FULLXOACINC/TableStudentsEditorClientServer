package library;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 15.3.17.
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    private String lastName;
    private String firstName;
    private String fatherName;
    private String groupNumber;
    private List<SocialWork> socialWork;

    public Student(String lastName, String firstName, String middleName,
                    String groupNumber, List<SocialWork> socialWork) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.fatherName = middleName;
        this.groupNumber = groupNumber;
        this.socialWork = socialWork;

    }
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public List<SocialWork> getSocialWork() {
        return socialWork;
    }
}
