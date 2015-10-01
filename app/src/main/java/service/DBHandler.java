package service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void addFavorite(String id){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, id);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                null,
                values);
    }

    public String getFavorites(){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_ENTRY_ID
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );

        String favorite[] = new String[8];
        cursor.moveToFirst();
        favorite[0] = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );
        favorite[1] = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ID)
        );
        cursor.moveToNext();
        favorite[2] = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );
        favorite[3] = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ID)
        );
        cursor.moveToNext();
        favorite[4] = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );
        favorite[5] = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ID)
        );
        String favoriteS = favorite[0] + favorite[1] + favorite[2] + favorite[3] + favorite[4];
        return favoriteS;
    }
}
