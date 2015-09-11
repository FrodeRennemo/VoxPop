package activitySupport;

/**
 * Created by andreaskalstad on 11/09/15.
 */
public class Mood {
    private String name;
    private int size;

    public Mood(String name){
        this.name = name;
        size = 10;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void increaseSize() {
        size = size*2;
    }
}
