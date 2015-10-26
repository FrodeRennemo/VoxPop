package activitySupport;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.michael.voxpop.R;

import java.io.File;
import java.util.ArrayList;

import service.Model;

/**
 * Created by andreaskalstad on 12/10/15.
 */

public class ImageAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> idArray;

    public ImageAdapter(Context context){
        this.context=context;
    }

    public void setIdArray(ArrayList<String> idArray){
        this.idArray = idArray;
    }

    @Override
    public int getCount() {
        return idArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Drawable d = Drawable.createFromPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+idArray.get(position)+".txt");
        imageView.setImageDrawable(d);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}

