package com.example.michael.voxpop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.JSONParser;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabFeed extends Fragment {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private boolean loggedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());
        View v =inflater.inflate(R.layout.tab_feed,container,false);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setPublishPermissions("manage_pages");

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
                        System.out.println(jsonParser.parseFeed(jArray).get(0));
                    }
                }
        ).executeAsync();
    }

}
