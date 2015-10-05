package service;

import android.content.Context;

/**
 * Created by Andreas on 10.09.2015.
 */
public class Model {

    public void getDetails(GetDetails req) {
        try {
            GetDetails getDetails = new GetDetails();
            req.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendImage(byte[] data) {
        try {
            PostImageToFS postImageToFS = new PostImageToFS();
            postImageToFS.execute(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFavorite(String id, Context applicationContext) {
        try {
            DBHandler dbHandler = new DBHandler(applicationContext);
            dbHandler.addFavorite(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFavorites(Context applicationContext){
        String favorites = "No favorites";
        try {
            DBHandler dbHandler = new DBHandler(applicationContext);
            favorites = dbHandler.getFavorites();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favorites;
    }
}
