package com.example.michael.voxpop;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import activitySupport.Mood;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import service.AsyncListener;
import service.HTTPRequest;
import service.Location;
import service.Model;

public class MainActivity extends AppCompatActivity implements AsyncListener {

    public static ArrayList<String> displayMoods;
    public static ArrayList<String> allMoods;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView filterView;
    private String filter = "";
    private Model model;
    private ArrayList<Location> locations;
    private ArrayList<Mood> moodCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        filterView = (TextView) findViewById(R.id.filter);
        mRecyclerView.setItemAnimator(new LandingAnimator());

        HTTPRequest req = new HTTPRequest();
        req.setAsyncListener(this);
        Model model = new Model(req);
        model.httpGet();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void goToResults(View v){
        ArrayList<Location> results = new ArrayList<Location>();
        String[] selected = filter.split(",");
        for(Location l : locations){
            String[] feats = l.getFeatures();
            for(int i=0; i<feats.length; i++){
                feats[i] = feats[i].trim().toLowerCase();
            }
            List<String> ar = Arrays.asList(feats);
            boolean include = true;
            for(String s : selected){
                if(!ar.contains(s.trim().toLowerCase())){
                    include = false;
                }
            }
            if(include){
                results.add(l);
            }
        }
        Intent i = new Intent(this, ResultActivity.class);
        Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        String json = new Gson().toJson(results, type);
        i.putExtra("results", json);
        startActivity(i);
    }

    public void selectMood(int position){
        if(filter.equals("")){
            filter += displayMoods.get(position);
        }
        else {
            filter += ", " + displayMoods.get(position);
        }
        filterView.setText(filter);
        displayMoods.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    public void resetFilter(View v){
        filter = "";
        filterView.setText(filter);
        displayMoods.clear();
        for(String s : allMoods){
            displayMoods.add(s);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void asyncDone(ArrayList<Location> res) {
        locations = res;
        moodCount = new ArrayList<Mood>();
        displayMoods = new ArrayList<String>();
        allMoods = new ArrayList<String>();
        for(int i=0; i<res.size(); i++){
            for(int j=0; j<res.get(i).getFeatures().length; j++){
                if(!displayMoods.contains(res.get(i).getFeatures()[j].toLowerCase())){
                    displayMoods.add(res.get(i).getFeatures()[j].toLowerCase());
                    moodCount.add(new Mood(res.get(i).getFeatures()[j]));
                } else {
                    for(int k=0; k<moodCount.size(); k++){
                        if(res.get(i).getFeatures()[j].equalsIgnoreCase(moodCount.get(k).getName())){
                            moodCount.get(k).increaseSize();
                        }
                    }
                }
            }
        }
        for(String s : displayMoods){
            allMoods.add(s);
        }
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(displayMoods, moodCount, getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<String> mDataset;
        private ArrayList<Mood> moodCount;
        MainActivity activity;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            CardView container;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.info_text);
                container = (CardView) v.findViewById(R.id.card_view);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<String> myDataset, ArrayList<Mood> moodCount, Context context, MainActivity activity) {
            mDataset = myDataset;
            this.moodCount = moodCount;
            this.activity = activity;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_card, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset.get(position));
            holder.mTextView.setTextSize(moodCount.get(position).getSize());
            CustomClickListener ccl = new CustomClickListener(activity);
            ccl.setPos(position);
            holder.container.setOnClickListener(ccl);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


        public class CustomClickListener implements View.OnClickListener {
            public int position;
            MainActivity activity;

            public CustomClickListener(MainActivity activity){
                this.activity = activity;

            }
            public void setPos(int pos){
                position = pos;
            }
            @Override
            public void onClick(View v) {
                activity.selectMood(position);
            }
        }

    }
}


