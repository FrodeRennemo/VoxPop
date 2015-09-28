package com.example.michael.voxpop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import service.Location;

public class ResultActivity extends AppCompatActivity {

    public ArrayList<Location> resultLocations;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Bitmap[] bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        resultLocations = new Gson().fromJson(i.getStringExtra("results"), type);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mAdapter = new MyAdapter(resultLocations, bitmaps, getApplicationContext(), this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new LandingAnimator());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        // or call onBackPressed()
        return true;
    }

    private void goToDetails(int position) {
        Intent i = new Intent(this, DetailsActivity.class);
        Type type = new TypeToken<Location>(){}.getType();
        String json = new Gson().toJson(resultLocations.get(position), type);
        i.putExtra("selected", json);
        startActivity(i);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Location> mDataset;
        private Bitmap[] bitmaps;
        ResultActivity activity;
        ImageLoaderConfiguration config;
        ImageLoader imageLoader;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            CardView container;
            public TextView mAddress;
            public ProgressBar _progress;
            ImageView img;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.info_text);
                container = (CardView) v.findViewById(R.id.card_view);
                mAddress = (TextView) v.findViewById(R.id.address_text);
                _progress = (ProgressBar) v.findViewById(R.id.progressBar);
                img = (ImageView) v.findViewById(R.id.imageView);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<Location> myDataset, Bitmap[] bitmaps, Context context, ResultActivity activity) {
            mDataset = myDataset;
            this.activity = activity;
            this.bitmaps = bitmaps;
            config = new ImageLoaderConfiguration.Builder(this.activity).build();
            ImageLoader.getInstance().init(config);
            imageLoader = ImageLoader.getInstance();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_result, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final ImageView currentImage = holder.img;
            final ProgressBar _progress = holder._progress;
            holder.mTextView.setText(mDataset.get(position).getName());
            String[] features = mDataset.get(position).getFeatures();
            String featDisplay = "";
            for(int i=0; i<features.length; i++){
                if((i == 0 && features.length == 1) || i == features.length-1){
                    featDisplay += features[i];
                }else {
                    featDisplay += features[i]+", ";
                }
            }
            holder.mAddress.setText(featDisplay);
            //holder.img.setImageBitmap(bitmaps[0]);
            CustomClickListener ccl = new CustomClickListener(activity);
            ccl.setPos(position);
            holder.container.setOnClickListener(ccl);
            imageLoader.loadImage("http://voxpop-app.herokuapp.com/nightclubs/" + mDataset.get(position).getId() + "/profile_image", new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    currentImage.setImageBitmap(loadedImage);
                    _progress.setVisibility(View.GONE);
                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


        public class CustomClickListener implements View.OnClickListener {
            public int position;
            ResultActivity activity;

            public CustomClickListener(ResultActivity activity){
                this.activity = activity;

            }
            public void setPos(int pos){
                position = pos;
            }
            @Override
            public void onClick(View v) {
                activity.goToDetails(position);
            }
        }
    }
}
