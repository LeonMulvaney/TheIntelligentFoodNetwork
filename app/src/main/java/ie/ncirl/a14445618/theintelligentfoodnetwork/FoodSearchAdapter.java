package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leon on 05/02/2018.
 */

public class FoodSearchAdapter extends ArrayAdapter<FoodSearchItem>{
    public FoodSearchAdapter(Context context, ArrayList<FoodSearchItem> item) {
        super(context, 0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FoodSearchItem foodSearchItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_search_item_adapter_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvFoodName = convertView.findViewById(R.id.tvFoodName);
        TextView tvServingQuantity = convertView.findViewById(R.id.tvServingQuantity);
        TextView tvServingUnit = convertView.findViewById(R.id.tvServingUnit);
        TextView tvServingWeight = convertView.findViewById(R.id.tvServingWeight);
        TextView tvCalories = convertView.findViewById(R.id.tvCalories);
        TextView tvTotalFat = convertView.findViewById(R.id.tvTotalFat);
        TextView tvSaturatedFat = convertView.findViewById(R.id.tvSaturatedFat);
        TextView tvCholesterol = convertView.findViewById(R.id.tvCholesterol);
        TextView tvSodium = convertView.findViewById(R.id.tvSodium);
        TextView tvCarbohydtrate = convertView.findViewById(R.id.tvCarbohydtrate);
        TextView tvFibre = convertView.findViewById(R.id.tvFibre);
        TextView tvSugars = convertView.findViewById(R.id.tvSugars);
        TextView tvProtein = convertView.findViewById(R.id.tvProtein);
        TextView tvPotassium = convertView.findViewById(R.id.tvPotassium);




        // Populate the data into the template view using the data object
        tvFoodName.setText(foodSearchItem.getFood_name());
        tvServingQuantity.setText("Serving Quantity: " + foodSearchItem.getServing_qty());
        tvServingUnit.setText("Serving Unit: " + foodSearchItem.getServing_unit());
        tvServingWeight.setText("Serving Weight: " + foodSearchItem.getServing_weight_grams());
        tvCalories.setText("Calories: " + foodSearchItem.getCalories());
        tvTotalFat.setText("Total Fat: " + foodSearchItem.getTotal_fat());
        tvSaturatedFat.setText("Saturated Fat: " + foodSearchItem.getSaturated_fat());
        tvCholesterol.setText("Cholesterol: " + foodSearchItem.getCholesterol());
        tvSodium.setText("Sodium: " + foodSearchItem.getSodium());
        tvCarbohydtrate.setText("Carbohydrates: " + foodSearchItem.getTotal_carbohydrate());
        tvFibre.setText("Fibre: " + foodSearchItem.getFibre());
        tvSugars.setText("Sugars: " + foodSearchItem.getSugars());
        tvProtein.setText("Protein: " + foodSearchItem.getProtein());
        tvPotassium.setText("Potassium: " + foodSearchItem.getPotassium());


        // Return the completed view to render on screen
        return convertView;
    }
}
