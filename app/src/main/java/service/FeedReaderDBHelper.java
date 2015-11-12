package service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import service.FeedReaderContract.FeedEntry;

/**
 * Created by andreaskalstad on 29/09/15.
 */
public class FeedReaderDBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_ENTRY_LOC_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_TLF + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_OPENING_HOURS + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_META + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_EMAIL + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_AGE_LIMIT + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_PICTURE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_CITY_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_CITY_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_LOCATION + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_PAGE_ID + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VoxPop.db";

    public FeedReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void deleteTable(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

