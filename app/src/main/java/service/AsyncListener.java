package service;

import java.util.ArrayList;

/**
 * Created by andreaskalstad on 10/09/15.
 */
public interface AsyncListener {
    public void asyncDone(ArrayList<Location> response);
}
