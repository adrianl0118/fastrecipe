package com.example.fastrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //data repository will be an array of Recipe objects
    private List<Recipe> recipes = new ArrayList<>();

    //Dropdown menus for user input and textview for showing results
    private Spinner i_spinner;
    private Spinner t_spinner;
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Link spinners and textviews to xml
        i_spinner = (Spinner) findViewById(R.id.i_spinner);
        t_spinner = (Spinner) findViewById(R.id.t_spinner);
        show = (TextView) findViewById(R.id.show);

        //Set dropdown content of spinners - may be unecessary
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

        //Read recipe data to Array list
        readRecipeData();

        //The rest of the happenings occur as the "Find Recipes" button on the Main activity's GUI is clicked
        //as shown in the layout xml file.
    }

    private void readRecipeData(){
        InputStream is = getResources().openRawResource(R.raw.recipedb);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";

        //While reading the file line by line
        try {
            //Step over headers
            reader.readLine();

            while ((line = reader.readLine()) != null){
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

    //method set to on-click of "get recipes" button in xml -- Spannable strings for URLs
    public void getRecipes(View view){
        String ingr = i_spinner.getSelectedItem().toString();
        int time = (int) t_spinner.getSelectedItem();

        SpannableStringBuilder recipelist = new SpannableStringBuilder("");

        //Cycling through all available recipes in the Arraylist "database"
        for (int i = 0; i < recipes.size(); i++){

            //if the ingredient and cook time of the record matches what was selected in spinners
            if (recipes.get(i).getMain_ingredient().equals(ingr) && recipes.get(i).getCook_time() >= (time - 5) && recipes.get(i).getCook_time() <= (time + 5)) {

                //add its record form to the resultant string
                recipelist.append(recipes.get(i).toRecord());

                //double space for ease of reading
                recipelist.append("\n");
                recipelist.append("\n");
            }
        }

        //display everything in the show text view and set URLs to active
        show.setText(recipelist);
        show.setMovementMethod(LinkMovementMethod.getInstance());
        show.setHighlightColor(Color.TRANSPARENT);
    }
}
