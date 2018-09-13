package ve.com.strikersfran.myfamily.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import ve.com.strikersfran.myfamily.MiFamilia;
import ve.com.strikersfran.myfamily.model.ListMiFamilia;

public class SolicitudesBD {

    private static SolicitudesBDHelper mDbHelper = null;
    private static SolicitudesBD instance = null;

    public SolicitudesBD() {
    }

    public static SolicitudesBD getInstance(Context context) {
        if (instance == null) {
            instance = new SolicitudesBD();
            mDbHelper = new SolicitudesBDHelper(context);
        }
        return instance;
    }

    public long addSolicitudes(MiFamilia familia) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ID, familia.getUid());
        values.put(FeedEntry.COLUMN_NAME_NAME, familia.getNombre());
        values.put(FeedEntry.COLUMN_NAME_PAPELLIDO, familia.getPrimerApellido());
        values.put(FeedEntry.COLUMN_NAME_SAPELLIDO, familia.getSegundoApellido());
        values.put(FeedEntry.COLUMN_NAME_EMAIL, familia.getEmail());
        values.put(FeedEntry.COLUMN_NAME_PARENTESCO, familia.getParentesco());
        values.put(FeedEntry.COLUMN_NAME_AVATAR, familia.getAvatar());
        values.put(FeedEntry.COLUMN_NAME_ESTATUS, familia.getEstatus());
        values.put(FeedEntry.COLUMN_NAME_LAST_UPDATE, familia.getLastUpdate());
        // Insert the new row, returning the primary key value of the new row
        return db.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public void dropDB(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void addListSolicitudes(ListMiFamilia listSolicitud){
        for(MiFamilia solicitud: listSolicitud.getListMiFamilia()){
            addSolicitudes(solicitud);
        }
    }

    public ListMiFamilia getListSolicitudes() {
        ListMiFamilia listSolicitudes = new ListMiFamilia();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        try {
            Cursor cursor = db.rawQuery("select * from " + FeedEntry.TABLE_NAME, null);
            while (cursor.moveToNext()) {
                MiFamilia solicitud = new MiFamilia();
                solicitud.setUid(cursor.getString(0));
                solicitud.setNombre(cursor.getString(1));
                solicitud.setPrimerApellido(cursor.getString(2));
                solicitud.setSegundoApellido(cursor.getString(3));
                solicitud.setEmail(cursor.getString(4));
                solicitud.setParentesco(cursor.getString(5));
                solicitud.setAvatar(cursor.getString(6));
                solicitud.setEstatus(cursor.getString(7));
                solicitud.setLastUpdate(cursor.getLong(8));

                listSolicitudes.getListMiFamilia().add(solicitud);
            }
            cursor.close();
        }catch (Exception e){
            return new ListMiFamilia();
        }
        return listSolicitudes;
    }

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        static final String TABLE_NAME = "solicitudes";
        static final String COLUMN_NAME_ID = "uid";
        static final String COLUMN_NAME_NAME = "nombre";
        static final String COLUMN_NAME_PAPELLIDO = "primer_apellido";
        static final String COLUMN_NAME_SAPELLIDO = "segundo_apellido";
        static final String COLUMN_NAME_EMAIL = "email";
        static final String COLUMN_NAME_PARENTESCO = "parentesco";
        static final String COLUMN_NAME_AVATAR = "avatar";
        static final String COLUMN_NAME_ESTATUS = "estatus";
        static final String COLUMN_NAME_LAST_UPDATE = "last_update";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String LONG_TYPE = " LONG";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_PAPELLIDO + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_SAPELLIDO + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_PARENTESCO + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_AVATAR + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ESTATUS + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_LAST_UPDATE + LONG_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    /*Clase para el helper de de solicitudes*/
    private static class SolicitudesBDHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        static final int DATABASE_VERSION = 1;
        //para poder dejar una base de datos por cada usaurio iniciado la sesion
        static final String DATABASE_NAME = "Solicitudes.db_"+StaticConfig.UID;

        SolicitudesBDHelper(Context context) {
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
