package com.example.michael.voxpop;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import service.AsyncListener;
import service.GetDetails;
import service.Location;
import service.Model;

public class SearchActivity extends AppCompatActivity implements AsyncListener {

    public ArrayList<Location> locations;
    public List<String> locationNames;
    public TextView _search_results;

    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _search_results = (TextView) findViewById(R.id.search_results);
        Intent i = getIntent();
        Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        GetDetails req = new GetDetails();
        req.setAsyncListener(this);
        Model model = new Model(this.getApplicationContext());
        model.getDetails(req);
    }

    public void asyncDone(ArrayList<Location> res) {
        locations = res;
        locationNames = new ArrayList<>();
        for(Location l : locations){
            locationNames.add(l.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals("")){
                    _search_results.setText("");
                }else {
                    callSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                if(newText.equals("")){
                    _search_results.setText("");
                }else {
                    callSearch(newText);
                }
                return true;
            }

            public void callSearch(String query) {
                List<String> searchResult = new ArrayList<String>();
                String res = "";
                for (String string : locationNames) {
                    if (string.matches("(?i)(" + query + ").*")) {
                        searchResult.add(string);
                        res += string+"\n";
                    }
                }
                _search_results.setText(res);
            }

        });
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
}
