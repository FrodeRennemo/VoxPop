package com.example.michael.voxpop;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import service.AsyncListener;
import service.HTTPRequest;
import service.Location;
import service.Model;

public class MainActivity extends AppCompatActivity implements AsyncListener {

    public static ArrayList<String> moods;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView filterView;
    private String filter = "";
    private Model model;

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
        Intent i = new Intent(this, ResultActivity.class);
        
        startActivity(i);
    }

    public void selectMood(int position){
        if(filter.equals("")){
            filter += moods.get(position);
        }
        else {
            filter += ", " + moods.get(position);
        }
        filterView.setText(filter);
        moods.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void asyncDone(ArrayList<Location> res) {
        moods = new ArrayList<String>();
        for(int i=0; i<res.size(); i++){
            for(int j=0; j<res.get(i).getFeatures().length; j++){
                if(!moods.contains(res.get(i).getFeatures()[j].toLowerCase())){
                    moods.add(res.get(i).getFeatures()[j].toLowerCase());
                }
            }
        }

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(moods, getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<String> mDataset;
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
        public MyAdapter(ArrayList<String> myDataset, Context context, MainActivity activity) {
            mDataset = myDataset;
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
            Random r = new Random();
            holder.mTextView.setTextSize(r.nextInt(50 - 10 + 1) + 10);
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


