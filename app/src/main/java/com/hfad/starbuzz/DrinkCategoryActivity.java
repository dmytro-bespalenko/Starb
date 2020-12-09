package com.hfad.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DrinkCategoryActivity extends AppCompatActivity {

    private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_category);
        new UpdateDrinkCategory().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    private class UpdateDrinkCategory extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            ListView listDrinks = findViewById(R.id.list_drinks);
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkCategoryActivity.this);
            try {
                db = starbuzzDatabaseHelper.getReadableDatabase();

                cursor = db.query("DRINK",
                        new String[]{"_id", "NAME"},
                        null, null,
                        null, null, null
                );

                SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(DrinkCategoryActivity.this, //Обычно это текущая активность.
                        android.R.layout.simple_list_item_1,//Как должны выводиться данные
                        cursor,//Созданный вами курсор. Он должен включать столбец _id и данные,
                        // которые должны выводиться в списковом представлении
                        new String[]{"NAME"}, //Соответствие между столбцами
                        new int[]{android.R.id.text1},// курсора и представлениями
                        0);//Используется для определения поведения курсора.

                listDrinks.setAdapter(listAdapter);
                AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(DrinkCategoryActivity.this, DrinkActivity.class);
                        intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                        startActivity(intent);
                    }
                };
                listDrinks.setOnItemClickListener(itemClickListener);
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DrinkCategoryActivity.this,
                        "Database unavailable",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}