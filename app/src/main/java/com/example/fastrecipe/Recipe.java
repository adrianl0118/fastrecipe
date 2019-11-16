package com.example.fastrecipe;

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
}
