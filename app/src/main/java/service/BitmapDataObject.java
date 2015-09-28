package service;

import java.io.Serializable;

/**
 * Created by andreaskalstad on 28/09/15.
 */
public class BitmapDataObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String value;
    byte[] serializedBitmap;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte[] getSerializedBitmap() {
        return serializedBitmap;
    }

    public void setSerializedBitmap(byte[] serializedBitmap) {
        this.serializedBitmap = serializedBitmap;
    }

}
