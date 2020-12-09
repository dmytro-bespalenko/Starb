package com.hfad.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FoodDBAdapter {

    public static final String FOOD_ID = "_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_RESOURCE_ID = "image_resource_id";

    public static final String DATABASE_TABLE = "FOOD";


    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private final Context context;

    public FoodDBAdapter(Context context) {
        this.context = context;
    }

    public FoodDBAdapter open() throws SQLiteException {
        this.databaseHelper = new DatabaseHelper(this.context);
        this.sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.databaseHelper.close();
    }

    public long createFood(String name, String description, String image_resource_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(IMAGE_RESOURCE_ID, image_resource_id);

        return this.sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public Cursor getAllFood() {

        return this.sqLiteDatabase.query(DATABASE_TABLE, new String[]{NAME, DESCRIPTION, IMAGE_RESOURCE_ID},
                null, null, null, null, null);
    }

    public Cursor getFood(long foodId) {
        Cursor cursor = this.sqLiteDatabase.query(true, DATABASE_TABLE,
                new String[]{NAME}, FOOD_ID + "=" + foodId,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}
