package com.creation.apps.mada.noteo.Persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Andrianina_pc on 28/08/2018.
 */

public class NoteSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "note";
    public static final String COLUMN_ID = "idnote";
    public static final String COLUMN_NAME = "namenote";
    public static final String COLUMN_CONTENT = "contentnote";
    public static final String COLUMN_THEME = "themenote";

    private static final String DATABASE_NAME = "noteo.db";
    private static final int DATABASE_VERSION = 1;


    // Commande sql pour la création de la base de données
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_CONTENT + " text not null, "
            + COLUMN_THEME + " text not null);";

    public NoteSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("persitence",
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
