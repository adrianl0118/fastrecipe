package com.example.fastrecipe;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

import xdroid.toaster.Toaster;

//Class for the records of our recipe ArrayList, to be preloaded at runtime from a CSV file
//This enables us to store a motley assortment of datatypes in the ArrayList
class Recipe {
    private String recipe_name;
    private String main_ingredient;
    private int cook_time;
    private String spicy;
    private String website;

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getMain_ingredient() {
        return main_ingredient;
    }

    public void setMain_ingredient(String main_ingredient) {
        this.main_ingredient = main_ingredient;
    }

    public int getCook_time() {
        return cook_time;
    }

    public void setCook_time(int cook_time) {
        this.cook_time = cook_time;
    }

    public String getSpicy() {
        return spicy;
    }

    public void setSpicy(String spicy) {
        this.spicy = spicy;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipe_name='" + recipe_name + '\'' +
                ", main_ingredient='" + main_ingredient + '\'' +
                ", cook_time=" + cook_time +
                ", spicy='" + spicy + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

    public SpannableString toRecord(){

        //Return a Spannablestring with recipe information (presentation format) and a hyperlink URL
        //Use a Spannablestring in which segments can be made into URLs

        SpannableString r_info = new SpannableString(recipe_name + " (" + cook_time + " minutes)" + "\n" + website + "\n");

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

        //calculate start and end position of URL, set into Spannablestring
        int temp = 0;
        if (getCook_time() < 10){
            temp = 1;
        } else {
            temp = 2;
        }
        int startpos = recipe_name.length()+ 2 + temp + 10;
        int endpos = startpos+website.length();

        //attach URL
        String url = getWebsite();

        r_info.setSpan(new URLSpan(url), startpos, endpos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        r_info.setSpan(clickableTerms, startpos, endpos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return r_info;
    }
}
