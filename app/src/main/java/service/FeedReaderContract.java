package service;

import android.provider.BaseColumns;

/**
 * Created by andreaskalstad on 01/10/15.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_LOC_ID = "loc_id";
        public static final String COLUMN_NAME_ENTRY_NAME = "name";
        public static final String COLUMN_NAME_ENTRY_TLF = "tlf";
        public static final String COLUMN_NAME_ENTRY_EMAIL = "email";
        public static final String COLUMN_NAME_ENTRY_AGE_LIMIT = "age_limit";
        public static final String COLUMN_NAME_ENTRY_ADDRESS = "address";
        public static final String COLUMN_NAME_ENTRY_LOCATION = "location";
        public static final String COLUMN_NAME_ENTRY_OPENING_HOURS = "opening_hours";
        public static final String COLUMN_NAME_ENTRY_META = "meta";
        public static final String COLUMN_NAME_ENTRY_PICTURE = "picture";
    }

}
