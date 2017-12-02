package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leon on 30/11/2017.
 */
//Custom Adapter  From: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {

    public FoodItemAdapter(Context context, ArrayList<FoodItem> item) {
        super(context, 0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FoodItem foodItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item_adapter_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvFoodType = convertView.findViewById(R.id.tvFoodType);
        TextView tvExpDate = convertView.findViewById(R.id.tvExpDate);
        TextView tvCalories = convertView.findViewById(R.id.tvCalories);
        TextView tvProtein = convertView.findViewById(R.id.tvProtein);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        //ImageView ivFood = convertView.findViewById(R.id.foodImage);

        // Populate the data into the template view using the data object
        tvFoodType.setText(foodItem.foodType);
        tvExpDate.setText("Expiry Date: " + foodItem.expiryDate);
        tvCalories.setText("Calories: " + foodItem.calories + " kcal");
        tvProtein.setText("Protein: " + foodItem.protein + "g");
        tvCategory.setText("Category: " + foodItem.category);


        // Return the completed view to render on screen
        return convertView;
    }


}
