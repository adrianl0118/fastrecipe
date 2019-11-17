package com.example.fastrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String mainingredient;
    private int time;
    private String spicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dropdown menus for user input
        Spinner i_spinner = (Spinner) findViewById(R.id.i_spinner);
        Spinner t_spinner = (Spinner) findViewById(R.id.t_spinner);

        // Set content of spinners
        ArrayAdapter<CharSequence> i_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_ingredient, android.R.layout.simple_spinner_item);
        i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        i_spinner.setAdapter(i_adapter);
        ArrayAdapter<CharSequence> t_adapter = ArrayAdapter.createFromResource(this,
                R.array.cook_time, android.R.layout.simple_spinner_item);
        t_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t_spinner.setAdapter(t_adapter);

        //Read recipe data to Array list
        readRecipeData();

    }

    //data repository will be an array of Recipe objects
    private List<Recipe> recipes = new ArrayList<>();

    private void readRecipeData(){
        InputStream is = getResources().openRawResource(R.raw.RecipeDB);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";

        //While reading the file line by line
        while(true){
            try {
                //Step over headers
                reader.readLine();

                if (!((line = reader.readLine()) != null)){
                    Log.d("MainActivity","Line:"+line);

                    //Split by commas
                    String[] tokens = line.split(",");


                    //Read the data
                    Recipe r1 = new Recipe();
                    r1.setRecipe_name(tokens[0]);
                    r1.setMain_ingredient(tokens[1]);
                    if(tokens[2].length()>0){      //don't process blank int, string ok
                        r1.setCook_time(Integer.parseInt(tokens[2]));
                    } else {
                        r1.setCook_time(0);
                    }
                    r1.setSpicy(tokens[3]);
                    if(tokens.length >= 4){       //don't process if last entry is blank (won't happen)
                        r1.setWebsite(tokens[4]);
                    }
                    recipes.add(r1);

                    Log.d("MainActivity","Just created: "+r1);
                }
            } catch (IOException e) {

                //If there is an error reading the line, throw exception and print msg to see where error is
                Log.d("MainActivity", "Error reading data file on line " + line, e);
                e.printStackTrace();
            }

        }
    }

    public void filter(String ingredient, int cooktime, String spicy){

    }
}
