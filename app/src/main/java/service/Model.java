package service;

/**
 * Created by Andreas on 10.09.2015.
 */
public class Model {
    private HTTPGet req;
    private HTTPPost post;

    public Model(HTTPGet req){
        this.req = req;
    }

    public Model(HTTPPost post){
        this.post = post;
    }

    public Model () {}

    public void httpGet() {
        try {
            req.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void httpPost(byte[] data) {
        try {
            post.execute(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
