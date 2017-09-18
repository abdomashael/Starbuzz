package com.abdo_mashael.starbuzz;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
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

public class FoodActivity extends Activity {

    public static final String EXTRA_FOODNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Get the drink from the intent
        int foodNo = (Integer) getIntent().getExtras().get(EXTRA_FOODNO);

        //Create a cursor
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("FOOD",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(foodNo)},
                    null, null, null);

            //Move to first record
            if (cursor.moveToFirst()) {

                //get details of the drink
                String nameText = cursor.getString(0);
                String descText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                TextView desc = (TextView) findViewById(R.id.description);
                desc.setText(descText);

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    //Update the database
    public void onFavoriteClicked(View view) {
        int foodNo = (Integer) getIntent().getExtras().get("foodNo");
        new FoodActivity.UpdateFoodTask().execute(foodNo);

    }

    //Inner class to update
    private class UpdateFoodTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues foodValues;

        protected void onPreExecute() {
            CheckBox favroite = (CheckBox) findViewById(R.id.favorite);
            foodValues = new ContentValues();
            foodValues.put("FAVORITE", favroite.isChecked());
        }

        protected Boolean doInBackground(Integer... foods) {
            int foodNo = foods[0];
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(FoodActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("FOOD", foodValues, "_id = ?", new String[]{Integer.toString(foodNo)});
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast toast = Toast.makeText(FoodActivity.this, "Database changed", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!success) {
                Toast toast = Toast.makeText(FoodActivity.this, "Database unavailable ", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}