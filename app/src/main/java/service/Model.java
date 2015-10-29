package service;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Andreas on 10.09.2015.
 */
public class Model {
    private DBHandler dbHandler;
    private static String city = "5628ceed64e18c1020f122be";

    public Model(Context applicationContext){
        dbHandler = new DBHandler(applicationContext);
    }

    public Model(){}

    public void getDetails(GetDetails req) {
        try {
            req.execute(city);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCity(String city){
        if(!(city == null)) {
            this.city = city;
        } else {
            this.city = "Trondheim";
        }
    }

    public void sendImage(PostImageToFS postImageToFS, byte[] data, String id, String nightclub, int cameraId) {
        try {
            HerokuImagePost herokuImagePost = new HerokuImagePost();
            herokuImagePost.execute(id, city, nightclub);
            ModelHelper modelHelper = new ModelHelper(data, id, cameraId);
            postImageToFS.execute(modelHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getImageId(String nightclub, Context ctx, AmazonS3Listener amazonS3Listener) {
        try {
            HerokuImageGet herokuImageGet = new HerokuImageGet();
            herokuImageGet.setAmazonS3Listener(amazonS3Listener);
            ModelHelper modelHelper = new ModelHelper(city, nightclub, ctx);
            herokuImageGet.execute(modelHelper);
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
