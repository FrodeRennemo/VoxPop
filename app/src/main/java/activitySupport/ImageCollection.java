package activitySupport;

import java.util.ArrayList;

/**
 * Created by Andreas on 21.10.2015.
 */
public class ImageCollection{

    private static ArrayList<String> imageCollection;

    public ImageCollection(ArrayList<String>  imageCollection){
        this.imageCollection = imageCollection;
    }

    public ArrayList<String> getImageCollection(){
        return imageCollection;
    }
}
