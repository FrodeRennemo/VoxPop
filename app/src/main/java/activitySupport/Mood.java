package activitySupport;

/**
 * Created by andreaskalstad on 11/09/15.
 */
public class Mood {
    private String name;
    private int size;
    private int MAX_SIZE = 60;

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
        if(size <= MAX_SIZE){
            size = size+10;
        }
    }
}
