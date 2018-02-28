package ie.ncirl.a14445618.theintelligentfoodnetwork;

/**
 * Created by Leon on 14/02/2018.
 */

public class ModelRecipeFromIngredient {
    public int id;
    public String title;
    public String imageUrl;

    public ModelRecipeFromIngredient() {
    }

    public ModelRecipeFromIngredient(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
