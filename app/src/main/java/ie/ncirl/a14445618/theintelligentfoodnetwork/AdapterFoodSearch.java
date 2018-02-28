package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Leon on 05/02/2018.
 */

public class AdapterFoodSearch extends ArrayAdapter<FoodSearchItem>{
    public AdapterFoodSearch(Context context, ArrayList<FoodSearchItem> item) {
        super(context, 0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FoodSearchItem foodSearchItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_food_search_item_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvFoodName = convertView.findViewById(R.id.tvFoodName);
        TextView tvServingAmount = convertView.findViewById(R.id.tvServingAmount);
        TextView tvServingQuantity = convertView.findViewById(R.id.valueServingQuantity);
        TextView tvServingUnit = convertView.findViewById(R.id.valueServingUnit);
        TextView tvServingWeight = convertView.findViewById(R.id.valueServingWeight);
        TextView tvCalories = convertView.findViewById(R.id.valueCalories);
        TextView tvTotalFat = convertView.findViewById(R.id.valueTotalFat);
        TextView tvSaturatedFat = convertView.findViewById(R.id.valueSaturatedFat);
        TextView tvCholesterol = convertView.findViewById(R.id.valueCholesterol);
        TextView tvSodium = convertView.findViewById(R.id.valueSodium);
        TextView tvCarbohydtrate = convertView.findViewById(R.id.valueCarbohydrate);
        TextView tvFibre = convertView.findViewById(R.id.valueFibre);
        TextView tvSugars = convertView.findViewById(R.id.valueSugars);
        TextView tvProtein = convertView.findViewById(R.id.valueProtein);
        TextView tvPotassium = convertView.findViewById(R.id.valuePotassium);




        // Populate the data into the template view using the data object
        /*tvFoodName.setText(foodSearchItem.getFood_name());
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
        tvPotassium.setText("Potassium: " + foodSearchItem.getPotassium());*/

        tvFoodName.setText(StringUtils.capitalize(foodSearchItem.getFood_name())); //First Letter Capital From: https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
        tvServingAmount.setText("" + foodSearchItem.getServing_qty() + " " + foodSearchItem.getServing_unit() + " - " + foodSearchItem.getServing_weight_grams() + " g" );
        tvServingQuantity.setText("" + foodSearchItem.getServing_qty());
        tvServingUnit.setText("" + foodSearchItem.getServing_unit());
        tvServingWeight.setText("" + foodSearchItem.getServing_weight_grams() +" g");
        tvCalories.setText("" + foodSearchItem.getCalories() + " kcal");
        tvTotalFat.setText("" + foodSearchItem.getTotal_fat() + " g");
        tvSaturatedFat.setText("" + foodSearchItem.getSaturated_fat() + " g");
        tvCholesterol.setText("" + foodSearchItem.getCholesterol() + " mg");
        tvSodium.setText("" + foodSearchItem.getSodium() + " mg");
        tvCarbohydtrate.setText("" + foodSearchItem.getTotal_carbohydrate() + " g");
        tvFibre.setText("" + foodSearchItem.getFibre() +" g");
        tvSugars.setText("" + foodSearchItem.getSugars() + " g");
        tvProtein.setText("" + foodSearchItem.getProtein() + " g");
        tvPotassium.setText("" + foodSearchItem.getPotassium() + " mg");


        // Return the completed view to render on screen
        return convertView;
    }
}