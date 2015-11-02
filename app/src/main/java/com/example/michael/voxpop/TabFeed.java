package com.example.michael.voxpop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    private AccessToken accessToken;
    private boolean loggedIn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView _tv;
    ArrayList<String> news;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());
        View v =inflater.inflate(R.layout.tab_feed,container,false);
        callbackManager = CallbackManager.Factory.create();
        _tv = (TextView) v.findViewById(R.id.no_news_message);
        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setPublishPermissions("manage_pages");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.feed_view);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        news = new ArrayList<>();
        news.add("We got some news over here. This awesome place is having the party of a lifetime. Good stuff! Click here to view more!");
        news.add("Ja dette er også noen veldig gode nyheter for dere som liker å dra ut og drikke alkohol og kose dere med skaka rumpa og masse god viin!");
        news.add("Nå har det kommet noen andre nyheter her gutter. Dette er ikke like langt da.");
        news.add("We got some news over here. This awesome place is having the party of a lifetime. Good stuff! Click here to view more!");
        news.add("Ja dette er også noen veldig gode nyheter for dere som liker å dra ut og drikke alkohol og kose dere med skaka rumpa og masse god viin!");
        news.add("Nå har det kommet noen andre nyheter her gutter. Dette er ikke like langt da.");
        news.add("We got some news over here. This awesome place is having the party of a lifetime. Good stuff! Click here to view more!");
        news.add("Ja dette er også noen veldig gode nyheter for dere som liker å dra ut og drikke alkohol og kose dere med skaka rumpa og masse god viin!");
        news.add("Nå har det kommet noen andre nyheter her gutter. Dette er ikke like langt da.");



        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                boolean loggedIn = true;
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
        if(loggedIn) {
            initiateFeed();
        }

        if(news.size() == 0){
            mRecyclerView.setVisibility(View.GONE);
        } else {
            _tv.setVisibility(View.GONE);
            mAdapter = new MyAdapter(news);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new LandingAnimator());
            mRecyclerView.setAdapter(mAdapter);
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void initiateFeed(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/294677020558377/feed",
                null,
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
                        String data =  jsonParser.parseFeed(jArray).get(0);
//                        news.add(data);
//                        if(news.size() != 0){
//                            mRecyclerView.setAdapter(mAdapter);
//                        }
                    }
                }
        ).executeAsync();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<String> mDataset;
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
        public MyAdapter(ArrayList<String> myDataset) {
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
            holder.mTextView.setText(mDataset.get(position));
            holder.timestamp.setText("31.10.15");
            holder.club_name.setText("Diskoteket");

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

//
//        public class CustomClickListener implements View.OnClickListener {
//            public int position;
//            TabFeed activity;
//
//            public CustomClickListener(TabFeed activity){
//                this.activity = activity;
//
//            }
//            public void setPos(int pos){
//                position = pos;
//            }
//            @Override
//            public void onClick(View v) {
//                //activity.goToDetails(position);
//            }
//        }
    }
}
