package activitySupport;

import java.util.Date;

/**
 * Created by andreaskalstad on 04/11/15.
 */
public class FeedListItem implements Comparable<FeedListItem> {
    private  Date dateMessage;
    private String nightclub;
    private String message;

    public FeedListItem(String message, String nightclub, Date dateMessage){
        this.message = message;
        this.nightclub = nightclub;
        this.dateMessage = dateMessage;
    }

    public Date getDateMessage() {
        return dateMessage;
    }

    public String getNightclub() {
        return nightclub;
    }

    public String getMessage() {
        return message;
    }

    public void setNightclub(String nightclub){
        this.nightclub = nightclub;
    }

    @Override
    public int compareTo(FeedListItem another) {
        return this.getDateMessage().compareTo(another.getDateMessage());
    }
}
