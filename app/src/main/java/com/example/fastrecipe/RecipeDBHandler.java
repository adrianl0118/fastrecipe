package com.example.fastrecipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

import xdroid.toaster.Toaster;

public class RecipeDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "NULL";
    public static final String TABLE_NAME = "Recipes";

    public static final String COLUMN_KEY = "_Key";
    public static final String COLUMN_RECIPENAME = "_Recipe_Name";
    public static final String COLUMN_MAININGREDIENT = "_Main_Ingredient_1";
    public static final String COLUMN_COOKTIME = "_Cook_Time";
    public static final String COLUMN_SPICY = "_Spicy";
    public static final String COLUMN_WEBSITE = "_Website";

    public RecipeDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_KEY + " INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, " + COLUMN_RECIPENAME + " TEXT, " + COLUMN_MAININGREDIENT + " TEXT, " +
                COLUMN_COOKTIME + " INTEGER, " + COLUMN_SPICY + " TEXT, " + COLUMN_WEBSITE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addHandler(Recipe recipe) {

        Log.d("addHandler","Adding: "+recipe.toString());

        ContentValues values = new ContentValues();

        values.put(COLUMN_RECIPENAME, recipe.getRecipe_name());
        values.put(COLUMN_MAININGREDIENT, recipe.getMain_ingredient());
        values.put(COLUMN_COOKTIME, recipe.getCook_time());
        values.put(COLUMN_SPICY, recipe.getSpicy());
        values.put(COLUMN_WEBSITE, recipe.getWebsite());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();

        Log.d("addHandler", "Added: "+recipe.toString());
    }

    public SpannableStringBuilder searchHandler(String ingredient, int cooktime) {

        SpannableStringBuilder result = new SpannableStringBuilder("");

        String query = "SELECT DISTINCT * FROM " + TABLE_NAME + " " +
                "WHERE " + COLUMN_MAININGREDIENT + " = '" + ingredient +
                "' AND " + COLUMN_COOKTIME + " BETWEEN '" + Integer.toString(cooktime-5) + "' AND '" +
                Integer.toString(cooktime+5)+
                "' ORDER BY " + COLUMN_COOKTIME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {

            Log.d("searchHandler","Looking at "+cursor.getString(1));

            String result_1 = cursor.getString(1);
            int result_3 = cursor.getInt(3);
            String result_5 = cursor.getString(5);
            SpannableString temp = new SpannableString( result_1 + " (" + String.valueOf(result_3) +
                    " minutes)" + "\n" + result_5 + "\n" + "\n");

            ClickableSpan clickableTerms = new ClickableSpan() {
                @Override
                public void onClick(View textView) {

                    Toaster.toast("YEAHHH!");
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(true);
                }
            };

            int digi = 0;
            if (String.valueOf(result_3).length() < 10){
                digi = 1;
            } else {
                digi = 2;
            }
            int startpos = result_1.length() + 2 + digi + 10;
            int endpos = startpos+result_5.length();

            temp.setSpan(new URLSpan(result_5), startpos, endpos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            temp.setSpan(clickableTerms, startpos, endpos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            result.append(temp);
        }
        cursor.close();
        db.close();


        return result;
    }


    public static void deleteDatabase(Context mContext){

        mContext.deleteDatabase(DATABASE_NAME);
    }
}
