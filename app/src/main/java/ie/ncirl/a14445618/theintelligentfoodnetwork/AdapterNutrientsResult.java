package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Leon on 05/02/2018.
 */

public class AdapterNutrientsResult extends BaseAdapter {
    Context context;
    ArrayList<ModelNutrientsResult> nutrientsList;
    LayoutInflater inflter;

    public AdapterNutrientsResult(Context applicationContext, ArrayList<ModelNutrientsResult> nutrientsList) {
        this.context = applicationContext;
        this.nutrientsList = nutrientsList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return nutrientsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.adapter_nutrients_result, null);

        // Lookup view for data population
        ImageView imgNutritionalValues = view.findViewById(R.id.imgNutritionalValues);
        TextView tvCaloriesTop = view.findViewById(R.id.tvCaloriesTop);
        TextView tvProteinTop = view.findViewById(R.id.tvProteinTop);
        TextView tvSugarsTop = view.findViewById(R.id.tvSugarsTop);
        TextView tvFoodName = view.findViewById(R.id.tvFoodName);
        TextView tvServingAmount = view.findViewById(R.id.tvServingAmount);
        TextView tvServingQuantity = view.findViewById(R.id.valueServingQuantity);
        TextView tvServingUnit = view.findViewById(R.id.valueServingUnit);
        TextView tvServingWeight = view.findViewById(R.id.valueServingWeight);
        TextView tvCalories = view.findViewById(R.id.valueCalories);
        TextView tvTotalFat = view.findViewById(R.id.valueTotalFat);
        TextView tvSaturatedFat = view.findViewById(R.id.valueSaturatedFat);
        TextView tvCholesterol = view.findViewById(R.id.valueCholesterol);
        TextView tvSodium = view.findViewById(R.id.valueSodium);
        TextView tvCarbohydtrate = view.findViewById(R.id.valueCarbohydrate);
        TextView tvFibre = view.findViewById(R.id.valueFibre);
        TextView tvSugars = view.findViewById(R.id.valueSugars);
        TextView tvProtein = view.findViewById(R.id.valueProtein);
        TextView tvPotassium = view.findViewById(R.id.valuePotassium);



        // Populate the data into the template view using the data object
        tvFoodName.setText(StringUtils.capitalize(nutrientsList.get(i).getFood_name())); //First Letter Capital From: https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
        tvServingAmount.setText("" + (int) nutrientsList.get(i).getServing_qty() + " " + nutrientsList.get(i).getServing_unit() + " - " + (int) nutrientsList.get(i).getServing_weight_grams() + " g" );
        tvCaloriesTop.setText("" + (int) nutrientsList.get(i).getCalories());
        tvProteinTop.setText("" + (int) nutrientsList.get(i).getProtein() + " g");
        tvSugarsTop.setText("" + (int) nutrientsList.get(i).getSugars() + " g");
        tvServingQuantity.setText("" + (int) nutrientsList.get(i).getServing_qty());
        tvServingUnit.setText("" + nutrientsList.get(i).getServing_unit());
        tvServingWeight.setText("" + (int) nutrientsList.get(i).getServing_weight_grams() +" g");
        tvCalories.setText("" + (int) nutrientsList.get(i).getCalories() + " kcal");
        tvTotalFat.setText("" + (int) nutrientsList.get(i).getTotal_fat() + " g");
        tvSaturatedFat.setText("" + (int) nutrientsList.get(i).getSaturated_fat() + " g");
        tvCholesterol.setText("" + (int) nutrientsList.get(i).getCholesterol() + " mg");
        tvSodium.setText("" + (int) nutrientsList.get(i).getSodium() + " mg");
        tvCarbohydtrate.setText("" + (int) nutrientsList.get(i).getTotal_carbohydrate() + " g");
        tvFibre.setText("" + (int) nutrientsList.get(i).getFibre() +" g");
        tvSugars.setText("" + (int) nutrientsList.get(i).getSugars() + " g");
        tvProtein.setText("" + (int) nutrientsList.get(i).getProtein() + " g");
        tvPotassium.setText("" + (int) nutrientsList.get(i).getPotassium() + " mg");

        Picasso.with(context).load(nutrientsList.get(i).getImgUrl()).into(imgNutritionalValues);

        return view;
    }
}
