package ve.com.strikersfran.myfamily.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class LastUpdateBD {

    private static LastUpdateBDHelper mDbHelper = null;
    private static LastUpdateBD instance = null;

    public LastUpdateBD() {
    }

    public static LastUpdateBD getInstance(Context context) {
        if (instance == null) {
            instance = new LastUpdateBD();
            mDbHelper = new LastUpdateBDHelper(context);
        }
        return instance;
    }

    public long add(String tabla, Long date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TABLA, tabla);
        values.put(FeedEntry.COLUMN_NAME_DATE, date);
        // Insert the new row, returning the primary key value of the new row
        return db.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public boolean update(String tabla, Long date){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(FeedEntry.COLUMN_NAME_DATE, date);

        String whereClause = String.format("%s=?", FeedEntry.COLUMN_NAME_TABLA);
        String[] whereArgs = {tabla};

        int resultado = db.update(FeedEntry.TABLE_NAME, values,whereClause, whereArgs);

        return resultado > 0;
    }

    public Long getDate(String tabla) {
        Long date = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("select * from " + FeedEntry.TABLE_NAME + " where "+ FeedEntry.COLUMN_NAME_TABLA + " = "+tabla, null);
            while (cursor.moveToNext()) {
                date = cursor.getLong(1);
            }
            cursor.close();
        }catch (Exception e){
            return date;
        }
        return date;
    }

    public void dropDB(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        static final String TABLE_NAME = "lastupdate";
        static final String COLUMN_NAME_TABLA = "tabla";
        static final String COLUMN_NAME_DATE = "date";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String LONG_TYPE = " LONG";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_TABLA + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_DATE + LONG_TYPE+ " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    /*Clase para el helper de Last Update*/
    private static class LastUpdateBDHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        static final int DATABASE_VERSION = 1;
        //para poder dejar una base de datos por cada usaurio iniciado la sesion
        static final String DATABASE_NAME = "LastUpdate.db_"+StaticConfig.UID;

        LastUpdateBDHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
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
}
