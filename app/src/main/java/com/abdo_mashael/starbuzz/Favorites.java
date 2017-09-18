package com.abdo_mashael.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Favorites extends Activity {


    private SQLiteDatabase db;
    private Cursor drinkFavoriteCrusor,foodFavoriteCrusor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Populate the Fav from Cursor
        ListView drinkListFavorites = (ListView) findViewById(R.id.list_favorites_drinks);
        ListView foodListFavorites = (ListView) findViewById(R.id.list_favorites_foods);

        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            drinkFavoriteCrusor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);
            CursorAdapter drinkFavoriteAdapter = new SimpleCursorAdapter(Favorites.this,
                    android.R.layout.simple_list_item_1,
                    drinkFavoriteCrusor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            drinkListFavorites.setAdapter(drinkFavoriteAdapter);

            foodFavoriteCrusor = db.query("FOOD",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);
            CursorAdapter foodFavoriteAdapter = new SimpleCursorAdapter(Favorites.this,
                    android.R.layout.simple_list_item_1,
                    foodFavoriteCrusor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            foodListFavorites.setAdapter(foodFavoriteAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable ", Toast.LENGTH_SHORT);
            toast.show();
        }
        //Know which clicked
        drinkListFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id){
                Intent intent= new Intent(Favorites.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKNO,(int)id);
                startActivity(intent);
            }
        });

        foodListFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> listView,View v,int position,long id){
                Intent intent= new Intent(Favorites.this, FoodActivity.class);
                intent.putExtra(FoodActivity.EXTRA_FOODNO,(int)id);
                startActivity(intent);
            }
        });
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        drinkFavoriteCrusor.close();
        foodFavoriteCrusor.close();
        db.close();
    }

    public void onRestart(){
        super.onRestart();
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            drinkFavoriteCrusor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);

            ListView drinkListFavorites = (ListView) findViewById(R.id.list_favorites_drinks);
            CursorAdapter drinkFavoriteAdapter = new SimpleCursorAdapter(Favorites.this,
                    android.R.layout.simple_list_item_1,
                    drinkFavoriteCrusor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            drinkListFavorites.setAdapter(drinkFavoriteAdapter);

            foodFavoriteCrusor = db.query("FOOD",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);

            ListView foodListFavorites = (ListView) findViewById(R.id.list_favorites_foods);
            CursorAdapter foodFavoriteAdapter = new SimpleCursorAdapter(Favorites.this,
                    android.R.layout.simple_list_item_1,
                    foodFavoriteCrusor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            foodListFavorites.setAdapter(foodFavoriteAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable ", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
