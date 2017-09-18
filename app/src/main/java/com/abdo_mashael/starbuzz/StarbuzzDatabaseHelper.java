package com.abdo_mashael.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PrivateKey;

/**
 * Created by Abdo Mashael on 9/2/2017.
 */

class StarbuzzDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "starbuzz";
    private static final int DB_VERSION = 2;


    StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            //Drinks table
            db.execSQL("CREATE TABLE DRINK("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);"
            );

            db.execSQL("CREATE TABLE FOOD("
                            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "NAME TEXT, "
                            + "DESCRIPTION TEXT, "
                            + "IMAGE_RESOURCE_ID INTEGER);"
            );

            insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte);
            insertDrink(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam", R.drawable.cappuccino);
            insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter);

            //Foods Table

            insertFood(db, "Caramel Apple Cheesecake", "This is so good and always goes over well when we have company over. It's also very easy to make!", R.drawable.cheese_cake);
            insertFood(db, "Cherry Almond Creamy Cheese Pie", "This is spectacular enough for company and a great time-saving dessert! A winner all the way!", R.drawable.pie);
            insertFood(db, "Pumpkin Creme Caramel", "This was delicious! We love the flavor of pumpkin and caramel, so having them together was like heaven!", R.drawable.creme_caramel);

        }

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
            db.execSQL("ALTER TABLE FOOD ADD COLUMN FAVORITE NUMERIC;");
        }
    }

    private static void insertDrink(SQLiteDatabase db, String name, String desc, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", desc);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
    }

    private static void insertFood(SQLiteDatabase db, String name, String desc, int resourceId) {
        ContentValues foodValues = new ContentValues();
        foodValues.put("NAME", name);
        foodValues.put("DESCRIPTION", desc);
        foodValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("FOOD", null, foodValues);
    }
}
