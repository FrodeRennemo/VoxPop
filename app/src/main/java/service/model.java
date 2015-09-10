package service;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by andreaskalstad on 09/09/15.
 */
public class Model {
    private HTTPRequest req;

    public Model(HTTPRequest req){
        this.req = req;
    }

    public Model () {}

    public void httpGet() {
        String test = "";
        try {
            req.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
