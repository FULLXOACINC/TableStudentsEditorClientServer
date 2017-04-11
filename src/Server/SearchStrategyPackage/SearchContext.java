package Server.SearchStrategyPackage;




import library.Student;

import java.util.List;

/**
 * Created by alex on 29.3.17.
 */
public class SearchContext {

    private SearchStrategy searchStrategy;

    public SearchContext(SearchStrategy searchStrategy) {
        this.searchStrategy=searchStrategy;
    }

    public List<Student> executeSearchStrategy(List<Student> students){
        return searchStrategy.execute(students);
    }



}
