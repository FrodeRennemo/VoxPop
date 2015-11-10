package com.example.michael.voxpop;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Properties;

import activitySupport.AssetsPropertyReader;
import service.Location;
import service.Model;

public class FavoritesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Model model;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Feed","Favorites"};
    int Numboftabs =2;
    private ArrayList<String> cities;
    private Spinner spinner;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ArrayList<Location> favoritesFeed;
    public ArrayList<Location> displayFavorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_favorites);
        getSupportActionBar().setTitle("VoxPop");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        String[] cityarray = {"Trondheim", "Kristiansand", "Oslo"};
        cities = new ArrayList<>();
        for(String s : cityarray){
            cities.add(s);
        }
        model = new Model(getApplicationContext());
        favoritesFeed = model.getFavorites();
        displayFavorites = new ArrayList<Location>();

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs, displayFavorites);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        tabs.setCustomTabView(R.layout.custom_tab_view, R.id.title);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.michael.voxpop", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, cities);
        View view1 = MenuItemCompat.getActionView(item);
        if (view1 instanceof Spinner)
        {
            spinner = (Spinner) MenuItemCompat.getActionView(item);
            spinner.setAdapter(adapter); // set the adapter to provide layout of rows and conten
            spinner.setPopupBackgroundResource(R.color.app_darker);
            spinner.setOnItemSelectedListener(this);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1,
                               int arg2, long arg3) {
        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getApplicationContext());
        Properties p = assetsPropertyReader.getProperties("Cities.properties");
        model.setCity(p.getProperty(spinner.getSelectedItem().toString()));
        changeCity(spinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        adapter.notifyDataSetChanged();
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void changeCity(String city) {
        try {
            displayFavorites.clear();
            for(Location l : favoritesFeed){
                if(l.getCity_name().equals(city)){
                    displayFavorites.add(l);
                }
            }

            adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs, displayFavorites);
            pager.setAdapter(adapter);
        } catch(Exception e) {
            Log.d("err", e.getMessage());
        }

    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
        ArrayList<Location> mDataset;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, ArrayList<Location> mDataset) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
            this.mDataset = mDataset;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {
            for(int i = 0; i<mDataset.size(); i++){
                mDataset.get(i).setPicture(null);
            }
            Type type = new TypeToken<ArrayList<Location>>(){}.getType();
            String json = new Gson().toJson(mDataset, type);
            Bundle bundle = new Bundle();
            bundle.putString("favorites",json);
            if(position == 0) // if the position is 0 we are returning the First tab
            {
                TabFeed tab1 = new TabFeed();
                tab1.setArguments(bundle);
                return tab1;
            }
            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                TabFavorites tab2 = new TabFavorites();
                tab2.setArguments(bundle);
                return tab2;
            }


        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }

   @Override
    protected void onResume() {
        super.onResume();
       adapter.getItem(1);
        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }
/*
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    } */
}
