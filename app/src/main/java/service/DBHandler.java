package service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import service.FeedReaderContract.FeedEntry;

/**
 * Created by andreaskalstad on 01/10/15.
 */
public class DBHandler {
    private FeedReaderDBHelper mDbHelper;
    private String id;
    private SQLiteDatabase db;

    public DBHandler (Context applicationContext){
        mDbHelper = new FeedReaderDBHelper(applicationContext);
        db = mDbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public void addFavorite(Location loc){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_LOC_ID, loc.getId());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_NAME, loc.getName());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ADDRESS, loc.getAddress());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_AGE_LIMIT, loc.getAge_limit());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_EMAIL, loc.getEmail());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_LOCATION, loc.getLocation());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_OPENING_HOURS, loc.getOpening_hours());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_TLF, loc.getTlf());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_META, loc.getMeta());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                null,
                values);
    }

    public ArrayList<Location> getFavorites() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_ENTRY_LOC_ID,
                FeedEntry.COLUMN_NAME_ENTRY_ADDRESS,
                FeedEntry.COLUMN_NAME_ENTRY_AGE_LIMIT,
                FeedEntry.COLUMN_NAME_ENTRY_EMAIL,
                FeedEntry.COLUMN_NAME_ENTRY_LOCATION,
                FeedEntry.COLUMN_NAME_ENTRY_META,
                FeedEntry.COLUMN_NAME_ENTRY_NAME,
                FeedEntry.COLUMN_NAME_ENTRY_OPENING_HOURS,
                FeedEntry.COLUMN_NAME_ENTRY_TLF
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                null,
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );

        ArrayList<Location> favorites = new ArrayList<>();
        for (int i = 0; i<cursor.getCount(); i++){
            if(i==0){
                cursor.moveToFirst();
            }
            Location loc = new Location(
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_LOC_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_TLF)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_OPENING_HOURS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_AGE_LIMIT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_META)));
            favorites.add(loc);
            cursor.moveToNext();
        }
        return favorites;
    }

    public void deleteFavorite(String id){
        String[] a = {id};
        db.delete(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_NAME_ENTRY_LOC_ID + "=?", a);
    }

    public boolean checkFavoriteExists(String id){
        Cursor cursor = db.rawQuery("SELECT * FROM "+FeedEntry.TABLE_NAME+" WHERE "+FeedEntry.COLUMN_NAME_ENTRY_LOC_ID+"='" + id + "'", null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
