package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //Получение напитка из интента
        int drinkId = (int) getIntent().getExtras().get(EXTRA_DRINKID);

        SQLiteOpenHelper starbuzzDataHelper = new StarbuzzDatabaseHelper(this);
        try (SQLiteDatabase db = starbuzzDataHelper.getReadableDatabase()) {

//Создать курсор для получения из таблицы DRINK столбцов NAME, DESCRIPTION и IMAGE_RESOURCEID тех записей
//у которых значение _id равно drinkId.
            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null
            );
            //Переход к первой записи в курсоре
            if (cursor.moveToFirst()) {
                //Получение данных напитка из курсора
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = cursor.getInt(3) == 1;

                //Заполнение названия напитка
                TextView name = findViewById(R.id.name);
                name.setText(nameText);
                //Заполнение описания напитка
                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);
                //Заполнение изображения напитка
                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
                //Заполнение флажка любимого напитка
                CheckBox favorite = findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);

            }
            cursor.close();
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    //Новая версия метода onFavoritesClicked() не содержит кода обновления столбца FAVORITE.
//Вместо этого она вызывает код AsyncTask, выполняющий обновление в фоновом режиме.
    public void onFavoriteClicked(View view) {
        int drinkId = (int) Objects.requireNonNull(getIntent().getExtras()).get(EXTRA_DRINKID);
        new UpdateDrinkTask().execute(drinkId);//Выполнить AsyncTask и передать идентификатор напитка.
    }

    //Внутренний класс для обновления напитка
    //Заменяется на Integer в соответствии с параметром метода doInBackground().
    //Заменяется на Boolean в соответствии с возвращаемым типом метода doInBackground().
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        //Мы определили drinkValues как приватную переменную,
        // так как она используется только в методах onExecute() и doInBackground().
        private ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
//Перед выполнением кода базы данных значение флажка помещается в объект drinkValues типа ContentValues.
            //получить значение флажка любимого напитка.
            CheckBox favorite = findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        //Код базы данных выполняется в фоновом потоке.
        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];
            SQLiteOpenHelper starbuzzHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
            try (SQLiteDatabase db = starbuzzHelper.getWritableDatabase()) {
                db.update("DRINK",
                        drinkValues,
                        "_id = ?",
                        new String[]{Integer.toString(drinkId)});
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }
        //После выполнения кода базы данных в фоновом режиме следует проверить, успешно ли он был выполнен.
// Если при выполнении произошла ошибка, выводится сообщение об ошибке.
        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
//Код вывода сообщения включается в метод onPostExecute(),
//так как он должен выполняться в основном потоке событий для обновления экрана.
            }
        }
    }


}