package service;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Andreas on 10.09.2015.
 */
public class Model {
    private DBHandler dbHandler;

    public Model(Context applicationContext){
        dbHandler = new DBHandler(applicationContext);
    }

    public Model(){}

    public void getDetails(GetDetails req) {
        try {
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

    public void addFavorite(Location loc) {
        try {
            dbHandler.addFavorite(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Location> getFavorites(){
        ArrayList<Location> favorites = new ArrayList<>();
        try {
            favorites = dbHandler.getFavorites();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return favorites;
    }
    public Bitmap getLocationBitmap(String id){
        Bitmap bitmap = null;
        try {
            bitmap = dbHandler.getLocationBitmap(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    public void deleteFavorite(String id){
        try {
            dbHandler.deleteFavorite(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkFavoriteExists(String id){
        boolean res = false;
        try{
            res = dbHandler.checkFavoriteExists(id);
        } catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
