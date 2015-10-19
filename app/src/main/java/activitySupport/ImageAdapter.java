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

import service.Model;

/**
 * Created by andreaskalstad on 12/10/15.
 */

public class ImageAdapter extends PagerAdapter {
    Context context;
    Model model;

    private int[] GalImages = new int[] {
            R.drawable.frodemarker,
            R.drawable.samfundet,
    };

    public ImageAdapter(Context context){
        this.context=context;
        model = new Model();
    }

    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        model.getImage("test", context);
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Drawable d = Drawable.createFromPath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloadtest.txt");
        imageView.setImageDrawable(d);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}

