package service;

import java.util.ArrayList;

/**
 * Created by andreaskalstad on 26/10/15.
 */
public interface DownloadListener {
    public ArrayList<Boolean> completed(ArrayList<Boolean> downloads);
}
