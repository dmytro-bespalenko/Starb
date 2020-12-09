package com.hfad.starbuzz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    public static final String DATABASE_NAME = "starbuzz";

    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_FOOD_TABLE =
            "create table FOOD (_id integer primary key autoincrement, "
                    + FoodDBAdapter.NAME + " TEXT,"
                    + FoodDBAdapter.FOOD_ID + " TEXT,"
                    + FoodDBAdapter.IMAGE_RESOURCE_ID + " TEXT" + ")";


    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DBAdapter(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(this.context);
    }


    static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FOOD_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

    public DBAdapter open() throws SQLiteException {
        this.sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.sqLiteDatabase.close();
    }
}

