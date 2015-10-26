package activitySupport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by andreaskalstad on 26/10/15.
 */
public class BitmapResizer {
    private final int imageWidth;
    private final int imageHeight;
    private final String path;
    private final BitmapFactory.Options options;

    public BitmapResizer(String path){
        this.path = path;
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
    }

    public int calculateInSampleSize(int reqWidth, int reqHeight) {
        // Raw height and width of image
        int inSampleSize = 4;

        if (imageHeight > reqHeight || imageWidth > reqWidth) {

            final int halfHeight = imageHeight / 2;
            final int halfWidth = imageWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromFile(int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}
