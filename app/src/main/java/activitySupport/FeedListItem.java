package activitySupport;

/**
 * Created by andreaskalstad on 04/11/15.
 */
public class FeedListItem {
    private  String dateMessage;
    private String nightclub;
    private String message;

    public FeedListItem(String message, String nightclub, String dateMessage){
        this.message = message;
        this.nightclub = nightclub;
        this.dateMessage = dateMessage;
    }

    public String getDateMessage() {
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
}
