package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leon on 30/11/2017.
 */
//Custom Adapter  From: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
//Custom BaseAdapter From: http://abhiandroid.com/ui/baseadapter-tutorial-example.html
public class AdapterFoodItem extends BaseAdapter {
    Context context;
    ArrayList<ModelFoodItem> foodList;
    LayoutInflater inflter;

    public AdapterFoodItem(Context applicationContext, ArrayList<ModelFoodItem> foodList) {
        this.context = applicationContext;
        this.foodList = foodList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return foodList.size();
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
        view = inflter.inflate(R.layout.adapter_food_item_layout, null);

        // Lookup view for data population
        TextView tvFoodType = view.findViewById(R.id.tvFoodType);
        TextView tvExpDate = view.findViewById(R.id.tvExpDate);
        TextView tvCalories = view.findViewById(R.id.tvCalories);
        TextView tvProtein = view.findViewById(R.id.tvProtein);
        TextView tvCategory = view.findViewById(R.id.tvCategory);
        ImageView image = view.findViewById(R.id.foodImage);

        // Populate the data into the template view using the data object
        tvFoodType.setText(foodList.get(i).foodType);
        tvExpDate.setText("Expiry Date: " + foodList.get(i).expiryDate);
        tvCalories.setText("Calories: " + foodList.get(i).calories + " kcal");
        tvProtein.setText("Protein: " + foodList.get(i).protein + "g");
        tvCategory.setText("Category: " + foodList.get(i).category);

        //Setting an Image Programmatically From : https://stackoverflow.com/questions/16906528/change-image-of-imageview-programmatically-android
        if(foodList.get(i).category.equals("Poultry")){
            image.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.poultry));
        }

        else if(foodList.get(i).category.equals("Fruit and Veg")){
            image.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.veg));
        }

        else if(foodList.get(i).category.equals("Dairy")){
            image.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.dairy));
        }

        else{
            image.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.misc));
        }

        //image.setImageResource(Integer.parseInt(recipesList.get(i).getImageUrl()));
        //Picasso.with(context).load(imageUrl).into(image); //Use picasso library to load images instead of setImageResource
        return view;
    }
}
