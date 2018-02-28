package ie.ncirl.a14445618.theintelligentfoodnetwork;

/**
 * Created by Leon on 27/02/2018.
 */

public class ModelFavouriteRecipe {
    private String recipeTitle;
    private String recipeId;
    private String recipeImgUrl;

    public ModelFavouriteRecipe() {
    }

    public ModelFavouriteRecipe(String recipeTitle, String recipeId, String recipeImgUrl) {
        this.recipeTitle = recipeTitle;
        this.recipeId = recipeId;
        this.recipeImgUrl = recipeImgUrl;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeImgUrl() {
        return recipeImgUrl;
    }

    public void setRecipeImgUrl(String recipeImgUrl) {
        this.recipeImgUrl = recipeImgUrl;
    }
}
