package service;

import java.util.ArrayList;

/**
 * Created by Andreas on 24.10.2015.
 */
public interface AmazonS3Listener {
    public void asyncDone(ArrayList<String> idArray);
}
