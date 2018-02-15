package ie.ncirl.a14445618.theintelligentfoodnetwork;

/**
 * Created by leonm on 15/02/2018.
 */

public class RecipeDetailsModel {
    public String title;
    public String instructions;
    public String imageUrl;
    public String spoonacularSourceUrl;

    public RecipeDetailsModel() {
    }

    public RecipeDetailsModel(String title, String instructions, String imageUrl,String spoonacularSourceUrl) {
        this.title = title;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }

    public void setSpoonacularSourceUrl(String spoonacularSourceUrl) {
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }
}
