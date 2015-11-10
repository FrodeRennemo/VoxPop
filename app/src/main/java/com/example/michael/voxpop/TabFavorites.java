package com.example.michael.voxpop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import service.Location;
import service.Model;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabFavorites extends Fragment {

    private Model model;
    public ArrayList<Location> favorites;
    private FloatingActionMenu _fam;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView _buttonHint;
    private FloatingActionButton _go_to_search;
    private FloatingActionButton _go_to_tags;
    private FloatingActionButton _go_to_map;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_favorites,container,false);

        model = new Model(getActivity().getApplicationContext());
        Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        String a = getArguments().getString("favorites");
        final ArrayList<Location> bundledFavorites = new Gson().fromJson(a, type);
        favorites = bundledFavorites;

        _buttonHint = (ImageView) v.findViewById(R.id.buttonHint);
        _fam = (FloatingActionMenu) v.findViewById(R.id.menu);
        _go_to_search = (FloatingActionButton) v.findViewById(R.id.menu_item);
        _go_to_tags = (FloatingActionButton) v.findViewById(R.id.menu_item2);
        _go_to_map = (FloatingActionButton) v.findViewById(R.id.menu_item3);

        checkDisplayArrow();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.favorites_view);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mAdapter = new MyAdapter(favorites, this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new LandingAnimator());

        _go_to_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
                _fam.close(false);
            }
        });
        _go_to_tags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                _fam.close(false);
            }
        });
        _go_to_map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Location> locsNoPic = new ArrayList<Location>();
                for(Location l : favorites){
                    Location noPic =  l;
                    noPic.setPicture(null);
                    locsNoPic.add(noPic);
                }
                Intent i = new Intent(getActivity(), MapActivity.class);
                Type type = new TypeToken<ArrayList<Location>>(){}.getType();
                String json = new Gson().toJson(locsNoPic, type);
                i.putExtra("locations", json);
                startActivity(i);
                _fam.close(false);
            }
        });
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        String city = model.getCity();
        ArrayList<Location> fav = model.getFavorites();
        favorites.clear();
        for(Location l : fav){
            if(l.getCity_id().equals(city)){
                favorites.add(l);
            }
        }
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
        Intent i = new Intent(getActivity(), DetailsActivity.class);
        Type type = new TypeToken<Location>(){}.getType();
        Location noPic = favorites.get(position);
        noPic.setPicture(null);
        String json = new Gson().toJson(noPic, type);
        i.putExtra("selected", json);
        startActivity(i);
        _fam.close(false);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Location> mDataset;
        TabFavorites activity;
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
        public MyAdapter(ArrayList<Location> myDataset,  TabFavorites activity) {
            mDataset = myDataset;
            this.activity = activity;
            config = new ImageLoaderConfiguration.Builder(this.activity.getActivity()).build();
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
            TabFavorites activity;

            public CustomClickListener(TabFavorites activity){
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