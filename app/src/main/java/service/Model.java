package service;

/**
 * Created by Michael on 10.09.2015.
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
