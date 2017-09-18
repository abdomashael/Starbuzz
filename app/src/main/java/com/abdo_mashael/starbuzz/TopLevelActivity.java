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

public class TopLevelActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor favoriteCrusor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        //create an OnItemListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        if (position == 0) {
                            Intent intent = new Intent(TopLevelActivity.this,
                                    DrinkCategoryAcrivity.class);
                            startActivity(intent);
                        }else if (position == 1){
                            Intent intent = new Intent(TopLevelActivity.this,
                                    FoodCategoryAcrivity.class);
                            startActivity(intent);

                        }
                    }
                };
        //Add the listener to the listview
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);

        //Populate the Fav from Cursor
        ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            favoriteCrusor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);

            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(TopLevelActivity.this,
                    android.R.layout.simple_list_item_1,
                    favoriteCrusor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable ", Toast.LENGTH_SHORT);
            toast.show();
        }
        //Know which clicked
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> listView,View v,int position,long id){
                Intent intent= new Intent(TopLevelActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKNO,(int)id);
                startActivity(intent);
            }
        });
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        favoriteCrusor.close();
        db.close();
    }

    public void onRestart(){
        super.onRestart();
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            favoriteCrusor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);


            ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(TopLevelActivity.this,
                    android.R.layout.simple_list_item_1,
                    favoriteCrusor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable ", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}