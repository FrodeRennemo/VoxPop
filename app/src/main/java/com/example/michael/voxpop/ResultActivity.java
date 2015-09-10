package com.example.michael.voxpop;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class ResultActivity extends AppCompatActivity {

    public ArrayList<String> dataSet = new ArrayList<>();
    public ArrayList<String> addressNames = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] places = {"Samfundet", "Lyche", "Edgar", "Diskoteket", "Brukbar", "Bar og bær"};
        String[] addresses = {"Gate 1, Trondheim", "Gate 2, Trondheim", "Gate 3, Trondheim", "Gate 4, Trondheim", "Gate 5, Trondheim", "Gate 6, Trondheim"};
        for(int i=0; i<places.length; i++){
            dataSet.add(places[i]);
            addressNames.add(addresses[i]);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mAdapter = new MyAdapter(dataSet, addressNames, getApplicationContext(), this);
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
        i.putExtra("Title", dataSet.get(position));
        i.putExtra("Address", addressNames.get(position));
        startActivity(i);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<String> mDataset;
        private ArrayList<String> addresses;
        ResultActivity activity;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            CardView container;
            public TextView mAddress;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.info_text);
                container = (CardView) v.findViewById(R.id.card_view);
                mAddress = (TextView) v.findViewById(R.id.address_text);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<String> myDataset, ArrayList<String> addresses, Context context, ResultActivity activity) {
            mDataset = myDataset;
            this.activity = activity;
            this.addresses = addresses;
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
            holder.mTextView.setText(mDataset.get(position));
            holder.mAddress.setText(addresses.get(position));
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
