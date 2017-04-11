package Server.SearchStrategyPackage;



import library.SocialWork;

import java.util.List;

/**
 * Created by alex on 31.3.17.
 */
public class Find {
    public static boolean correctGroup(String group, String searchGroup) {
        return group.equals(searchGroup);
    }

    public static boolean correctName(String name,String searchName){
        return name.equals(searchName);
    }

    public static boolean findSocialWorkBitweenMinAndMax(String searchSocialWork, List<SocialWork> student, String minCount, String maxCount) {
        if (minCount.equals("-") && maxCount.equals("-"))
            return findSocialWork(searchSocialWork,student);

        int min = 0;
        if (!minCount.equals("-"))
            min = Integer.parseInt(minCount);
        int max = 0;
        if (!maxCount.equals("-"))
            max = Integer.parseInt(maxCount);

        int count = 0;
        for (SocialWork elOfSocialWork : student) {
            if (elOfSocialWork.getWork().equals(searchSocialWork))
                count++;
        }
        return count >= min && count <= max;
    }

    private static boolean findSocialWork(String searchSocialWork, List<SocialWork> student) {
        for (SocialWork elOfSocialWork : student) {
            if (elOfSocialWork.getWork().equals(searchSocialWork))
                return true;
        }
        return false;
    }
}
