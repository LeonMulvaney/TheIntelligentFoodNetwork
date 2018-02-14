package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Leon on 14/02/2018.
 */

public class RecipesByIngredientsAdapter extends ArrayAdapter<RecipeFromIngredientModel> {
    public RecipesByIngredientsAdapter(Context context, ArrayList<RecipeFromIngredientModel> item) {
        super(context, 0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RecipeFromIngredientModel recipeFromIngredientModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipes_by_ingredient_adapter_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvRecipeTitle = convertView.findViewById(R.id.tvRecipeTitle);

        // Populate the data into the template view using the data object
        tvRecipeTitle.setText(recipeFromIngredientModel.getTitle());
        // Return the completed view to render on screen
        return convertView;
    }
}
