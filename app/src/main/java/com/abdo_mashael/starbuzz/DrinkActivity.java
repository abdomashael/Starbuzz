package com.abdo_mashael.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //Get the drink from the intent
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);
        /*Drink drink = Drink.drinks[drinkNo];

        //Populate the drink image
        ImageView photo = (ImageView)findViewById(R.id.photo);
        photo.setImageResource(drink.getImageResourceId());
        photo.setContentDescription(drink.getName());

        //Populate the drink name
        TextView name = (TextView)findViewById(R.id.name);
        name.setText(drink.getName());

        //Populate the drink Description
        TextView description=(TextView)findViewById(R.id.description);
        description.setText(drink.getDescription());*/

        //Create a cursor
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkNo)},
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
        int drinkNo = (Integer) getIntent().getExtras().get("drinkNo");
        new UpdateDrinkTask().execute(drinkNo);

        /*CheckBox favroite = (CheckBox)findViewById(R.id.favorite);
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("FAVORITE",favroite.isChecked());

        SQLiteOpenHelper starbuzzDatabaseHelper =new StarbuzzDatabaseHelper(DrinkActivity.this);
        try {
            SQLiteDatabase db =starbuzzDatabaseHelper.getWritableDatabase();
            db.update("DRINK",drinkValues,"_id = ?",new String[]{Integer.toString(drinkNo)});
            db.close();
            Toast toast = Toast.makeText(this,"Database changed",Toast.LENGTH_SHORT);
            toast.show();
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this,"Database unavailable ",Toast.LENGTH_SHORT);
            toast.show();
        }*/

    }

    //Inner class to update
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues drinkValues;

        protected void onPreExecute() {
            CheckBox favroite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favroite.isChecked());
        }

        protected Boolean doInBackground(Integer... drinks) {
            int drinkNo = drinks[0];
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkNo)});
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast toast = Toast.makeText(DrinkActivity.this, "Database changed", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this, "Database unavailable ", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
