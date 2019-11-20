package com.example.fastrecipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

import xdroid.toaster.Toaster;

public class RecipeDBHandler extends SQLiteOpenHelper {

    //Information about the database and its columns
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RecipeDB.db";
    public static final String TABLE_NAME = "Recipes";
    public static final String COLUMN_RECIPENAME = "Recipe_Name";
    public static final String COLUMN_MAININGREDIENT = "Main_Ingredient_1";
    public static final String COLUMN_COOKTIME = "Cook_Time";
    public static final String COLUMN_SPICY = "Spicy";
    public static final String COLUMN_WEBSITE = "Website";


    //Constructor
    public RecipeDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Database created: this is where all the info about the columns goes in via a long string
        //String's name is always CREATE_TABLE and it also starts with this declaration
        // TABLE_NAME (COLUMN_ID [Data Type + Primary Key AutoincreasedID], COLUMN_NAME [Text],...etc.)
        //               all columnname + datatype go into the declaration above with commas in between

        //Note to self: may need to add a primary key integer column at the front
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_RECIPENAME +
                " TEXT PRIMARY KEY AUTOINCREMENT, " + COLUMN_MAININGREDIENT + " TEXT, " +
                COLUMN_COOKTIME + " INTEGER, " + COLUMN_SPICY + " TEXT, " + COLUMN_WEBSITE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    //Upgrades DB to new version. This won't be used but is a virtual method from SQLiteOpenHelper() class
    //that must be overridden for this subclass to compile and function.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //In the main activity of this app we only need addhandler (for the reading recipes from csv to sql) and
    //loadhandler for when the user is querying the database

    //Add a data record to the SQLite data table
    public void addHandler(Recipe recipe) {

        //Contentvalues object is a vehicle for putting the Recipe object's data into the table
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPENAME, recipe.getRecipe_name());
        values.put(COLUMN_MAININGREDIENT, recipe.getMain_ingredient());
        values.put(COLUMN_COOKTIME, recipe.getCook_time());
        values.put(COLUMN_SPICY, recipe.getSpicy());
        values.put(COLUMN_WEBSITE, recipe.getWebsite());

        //SQLitedatabase object (writable db) does the data manipulation, in this case adding ContentValues
        //close db afterwards
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //show a datapoint
    public SpannableStringBuilder loadHandler() {

        //the string where the data will be captured
        SpannableStringBuilder result = new SpannableStringBuilder("");

        //table name identifier string to feed into the SQLiteDatabase query
        String query = "Select*FROM " + TABLE_NAME;

        //SQLiteDatabase object (writable DB) will query based on the table name identifier
        //returns a cursor to the row in the SQLite table we are interested in
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //in this case, return cursor which is first column, the recipe name
        //also grab coinciding information in following columns and append into a string
        //add to the result string and clear the line, then close the cursor/db and return the string
        while (cursor.moveToNext()) {

            String result_0 = cursor.getString(0);
            int result_2 = cursor.getInt(2);
            String result_4 = cursor.getString(4);
            SpannableString temp = new SpannableString( result_0 + " (" + String.valueOf(result_2) +
                    " minutes)" + "\n" + result_4 + "\n" + "\n");

            ClickableSpan clickableTerms = new ClickableSpan() {
                @Override
                public void onClick(View textView) {

                    //Show toast, transient message to the viewer when the link is clicked
                    Toaster.toast("YEAHHH!");
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(true);
                }
            };

            //coding the URL
            int digi = 0;
            if (String.valueOf(result_2).length() < 10){
                digi = 1;
            } else {
                digi = 2;
            }
            int startpos = result_0.length() + 2 + digi + 10;
            int endpos = startpos+result_4.length();

            temp.setSpan(new URLSpan(result_4), startpos, endpos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            temp.setSpan(clickableTerms, startpos, endpos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            result.append(temp);
        }
        cursor.close();
        db.close();


        return result;
    }

}
