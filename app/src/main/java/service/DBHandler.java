package service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;

import service.FeedReaderContract.FeedEntry;

/**
 * Created by andreaskalstad on 01/10/15.
 */
public class DBHandler {
    private FeedReaderDBHelper mDbHelper;
    private BitmapConverter bitmapConverter;
    private static boolean favoritesUpdated = true;

    public DBHandler (Context applicationContext){
        mDbHelper = new FeedReaderDBHelper(applicationContext);
        bitmapConverter = new BitmapConverter();
    }
    public boolean getFavoritesUpdated(){
        return favoritesUpdated;
    }

    public void setFavoritesUpdated(boolean b){
        favoritesUpdated = b;
    }

    public boolean addFavorite(Location loc){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
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
        values.put(FeedEntry.COLUMN_NAME_ENTRY_CITY_ID, loc.getCity_id());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_CITY_NAME, loc.getCity_name());
        if(loc.getPicture() != null) {
            values.put(FeedEntry.COLUMN_NAME_ENTRY_PICTURE, bitmapConverter.BitMapToString(loc.getPicture()));
        }
        values.put(FeedEntry.COLUMN_NAME_ENTRY_META, loc.getMeta());
        values.put(FeedEntry.COLUMN_NAME_ENTRY_PAGE_ID, loc.getPageId());

        // Insert the new row, returning the primary key value of the new row
        long res = db.insert(
                FeedEntry.TABLE_NAME,
                null,
                values);
        db.close();

        if(res == -1){
            return false;
        }
        favoritesUpdated = true;
        return true;
    }

    public ArrayList<Location> getFavorites() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
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
        cursor.moveToFirst();
        ArrayList<Location> favorites = new ArrayList<>();
        for (int i = 0; i<cursor.getCount(); i++){
            Location loc = new Location(
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_LOC_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_TLF)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_OPENING_HOURS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_AGE_LIMIT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_META)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_CITY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_CITY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_PAGE_ID)));
            loc.setPicture(bitmapConverter.StringToBitMap(cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_PICTURE))));

            favorites.add(loc);
            cursor.moveToNext();
        }
        db.close();
        return favorites;
    }

    public boolean deleteFavorite(String id){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] a = {id};
        int res = db.delete(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_NAME_ENTRY_LOC_ID + "=?", a);
        db.close();
        if(res == 0){
            return false;
        }
        favoritesUpdated = true;
        return true;
    }

    public boolean checkFavoriteExists(String id){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry.COLUMN_NAME_ENTRY_LOC_ID + "='" + id + "'", null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public Bitmap getLocationBitmap(String id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] a = {id};
        Cursor cursor = db.query(FeedEntry.TABLE_NAME, null, FeedEntry.COLUMN_NAME_ENTRY_LOC_ID + "=?", a, null, null, null);
        cursor.moveToFirst();
        Bitmap b = bitmapConverter.StringToBitMap(cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_PICTURE)));
        db.close();
        return b;
    }
}
