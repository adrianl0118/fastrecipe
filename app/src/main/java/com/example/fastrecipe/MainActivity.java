package com.example.fastrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private Spinner i_spinner;
    private Spinner t_spinner;
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        i_spinner = (Spinner) findViewById(R.id.i_spinner);
        t_spinner = (Spinner) findViewById(R.id.t_spinner);
        show = (TextView) findViewById(R.id.show);

        String[] s1 = new String[]{"Chicken","Beef","Pork"};
        ArrayAdapter<String> i_adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,s1);
        i_adapter.setDropDownViewResource(R.layout.spinner_item);
        i_spinner.setAdapter(i_adapter);
        i_spinner.setPrompt("Please select");

        Integer[] s2 = new Integer[]{5,10,15,20,25};
        ArrayAdapter<Integer> t_adapter = new ArrayAdapter<Integer>(this,R.layout.spinner_item, s2);
        t_adapter.setDropDownViewResource(R.layout.spinner_item);
        t_spinner.setAdapter(t_adapter);
        t_spinner.setPrompt("Please select");

        readRecipeData();

    }

    private void readRecipeData(){
        InputStream is = getResources().openRawResource(R.raw.recipedb);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";

        RecipeDBHandler recipes = new RecipeDBHandler(this, null, null, 1);

        try {

            reader.readLine();

            while ((line = reader.readLine()) != null){
                Log.d("MainActivity","Line:"+line);

                String[] tokens = line.split(",");

                Recipe r1 = new Recipe();
                r1.setRecipe_name(tokens[1]);
                r1.setMain_ingredient(tokens[2]);
                if(tokens[3].length()>0){
                    r1.setCook_time(Integer.parseInt(tokens[3]));
                } else {
                    r1.setCook_time(0);
                }
                r1.setSpicy(tokens[4]);
                if(tokens.length >= 4){
                    r1.setWebsite(tokens[5]);
                }
                recipes.addHandler(r1);

                Log.d("MainActivity","Just created: "+r1);
            }
        } catch (IOException e) {

            Log.d("MainActivity", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }

    }

    public void getRecipes(View view){

        String ingredient = i_spinner.getSelectedItem().toString();
        int cooktime = (int) t_spinner.getSelectedItem();

        RecipeDBHandler recipes = new RecipeDBHandler(this, null, null, 1);
        show.setText(recipes.searchHandler(ingredient, cooktime));

        show.setMovementMethod(LinkMovementMethod.getInstance());
        show.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    protected void onStop(){

        RecipeDBHandler console = new RecipeDBHandler(this,null,null,1);
        console.deleteDatabase(this);

        super.onStop();
    }
}
