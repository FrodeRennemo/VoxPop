package com.example.michael.voxpop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import activitySupport.FeedListItem;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import service.JSONParser;
import service.Location;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabFeed extends Fragment {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView _tv;
    SwipeRefreshLayout swipeLayout;
    private ArrayList<FeedListItem> news;
    private ArrayList<Location> page_ids;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());
        View v =inflater.inflate(R.layout.tab_feed,container,false);
        callbackManager = CallbackManager.Factory.create();
        _tv = (TextView) v.findViewById(R.id.no_news_message);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.feed_view);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        news = new ArrayList<>();
        page_ids = new ArrayList<>();

        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setPublishPermissions("manage_pages");
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                loginButton.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });

        if(news.size() == 0){
            mRecyclerView.setVisibility(View.GONE);
        }
        mAdapter = new MyAdapter(news);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setAdapter(mAdapter);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(AccessToken.getCurrentAccessToken() == null){
                    loginButton.setVisibility(View.VISIBLE);
                } else {
                    news.clear();
                    initiateFeed();
                }
            }
        });
        if(AccessToken.getCurrentAccessToken() == null) {
            loginButton.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.GONE);
            initiateFeed();
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) {
            initiateFeed();
        }
    }

    public void refreshFavorites(ArrayList<Location> p){
        page_ids = p;
        if(mAdapter != null && AccessToken.getCurrentAccessToken() != null) {
            news.clear();
            initiateFeed();
        }
    }

    public void initiateFeed(){
        /*Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        String a = getArguments().getString("favorites");
        final ArrayList<Location> page_ids = new Gson().fromJson(a, type); */
        if(page_ids.size() != 0) {
            _tv.setVisibility(View.GONE);

            GraphRequestBatch batch = new GraphRequestBatch();
            for (int i = 0; i < page_ids.size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putString("Nightclub", page_ids.get(i).getName());
                batch.add(new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + page_ids.get(i).getPageId() + "/feed", bundle,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        JSONParser jsonParser = new JSONParser();
                                        JSONArray jArray = new JSONArray();
                                        try {
                                            JSONObject json = response.getJSONObject();
                                            jArray = json.getJSONArray("data");
                                        } catch (JSONException e) {

                                        }
                                        ArrayList<FeedListItem> messageList = jsonParser.parseFeed(jArray);
                                        for (int i = 0; i < messageList.size(); i++) {
                                            FeedListItem feedListItem = messageList.get(i);
                                            feedListItem.setNightclub(response.getRequest().getParameters().getString("Nightclub"));
                                            news.add(feedListItem);
                                        }
                                        swipeLayout.setRefreshing(false);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                );
            }
            batch.executeAsync();
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<FeedListItem> mDataset;
        TabFeed activity;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public TextView timestamp;
            public TextView club_name;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.result_title);
                timestamp = (TextView) v.findViewById(R.id.timestamp);
                club_name = (TextView) v.findViewById(R.id.nightclub_name);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<FeedListItem> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_feed, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Collections.sort(news);
            Collections.reverse(news);
            holder.mTextView.setText(mDataset.get(position).getMessage());
            holder.timestamp.setText(mDataset.get(position).getDateMessage().toString());
            holder.club_name.setText(mDataset.get(position).getNightclub());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

    }
}
