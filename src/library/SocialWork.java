package library;

import java.io.Serializable;

/**
 * Created by alex on 29.3.17.
 */
public class SocialWork implements Serializable {
    private String work;

    public SocialWork(String work) {
        this.work = work;
    }

    public String getWork() {
        return work;
    }
}
