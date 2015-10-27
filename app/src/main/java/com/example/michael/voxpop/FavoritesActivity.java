package com.example.michael.voxpop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Type;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import service.Location;
import service.Model;

public class FavoritesActivity extends AppCompatActivity {

    private Model model;
    public ArrayList<Location> favorites;
    private FloatingActionMenu _fam;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView _buttonHint;
    private ArrayList<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        getSupportActionBar().setTitle("VoxPop");
        String[] cityarray = {"Trondheim", "Kristiansand", "Oslo"};
        cities = new ArrayList<>();
        for(String s : cityarray){
            cities.add(s);
        }
        model = new Model(this.getApplicationContext());
        favorites = model.getFavorites();
        _buttonHint = (ImageView) findViewById(R.id.buttonHint);
        _fam = (FloatingActionMenu) findViewById(R.id.menu);
        checkDisplayArrow();
        mRecyclerView = (RecyclerView) findViewById(R.id.favorites_view);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mAdapter = new MyAdapter(favorites, this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new LandingAnimator());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        View view1 = MenuItemCompat.getActionView(item);
        if (view1 instanceof Spinner)
        {
            Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
            spinner.setAdapter(adapter); // set the adapter to provide layout of rows and conten
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    Toast.makeText(getApplicationContext(), "Item "+arg2+" selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.slide_right_exit, R.anim.slide_left_exit);
        // or call onBackPressed()
        return true;
    }
    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_right_exit, R.anim.slide_left_exit);
    }

    @Override
    public void onResume(){
        super.onResume();
        favorites = model.getFavorites();
        mAdapter = new MyAdapter(favorites, this);
        mRecyclerView.setAdapter(mAdapter);
        checkDisplayArrow();
    }

    public void checkDisplayArrow(){
        if(favorites.size() == 0){
            _buttonHint.setVisibility(View.VISIBLE);
        }else {
            _buttonHint.setVisibility(View.INVISIBLE);
        }
    }

    private void goToDetails(int position) {
        Intent i = new Intent(this, DetailsActivity.class);
        Type type = new TypeToken<Location>(){}.getType();
        Location noPic = favorites.get(position);
        noPic.setPicture(null);
        String json = new Gson().toJson(noPic, type);
        i.putExtra("selected", json);
        startActivity(i);
        _fam.close(false);
    }

    public void goToSearch(View v){
        startActivity(new Intent(this, SearchActivity.class));
        _fam.close(false);
    }


    public void goToTags(View v){
        startActivity(new Intent(this, MainActivity.class));
        _fam.close(false);
    }

    public void goToMaps(View v){
        ArrayList<Location> locsNoPic = new ArrayList<Location>();
        for(Location l : favorites){
            Location noPic =  l;
            noPic.setPicture(null);
            locsNoPic.add(noPic);
        }
        Intent i = new Intent(this, MapActivity.class);
        Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        String json = new Gson().toJson(locsNoPic, type);
        i.putExtra("locations", json);
        startActivity(i);
        _fam.close(false);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Location> mDataset;
        FavoritesActivity activity;
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
        public MyAdapter(ArrayList<Location> myDataset,  FavoritesActivity activity) {
            mDataset = myDataset;
            this.activity = activity;
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
            String[] features = mDataset.get(position).getMeta().split(",");
            String featDisplay = "";
            for(int i=0; i<features.length; i++){
                if((i == 0 && features.length == 1) || i == features.length-1){
                    featDisplay += features[i];
                }else {
                    featDisplay += features[i]+", ";
                }
            }
            holder.mAddress.setText(featDisplay);
            CustomClickListener ccl = new CustomClickListener(activity);
            ccl.setPos(position);
            holder.container.setOnClickListener(ccl);
            currentImage.setImageBitmap(mDataset.get(position).getPicture());
            _progress.setVisibility(View.GONE);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


        public class CustomClickListener implements View.OnClickListener {
            public int position;
            FavoritesActivity activity;

            public CustomClickListener(FavoritesActivity activity){
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
