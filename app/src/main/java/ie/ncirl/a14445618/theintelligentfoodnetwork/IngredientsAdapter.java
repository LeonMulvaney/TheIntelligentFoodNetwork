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
 * Created by Leon on 24/02/2018.
 */

//Custom Adapter  From: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
//Custom BaseAdapter From: http://abhiandroid.com/ui/baseadapter-tutorial-example.html
public class IngredientsAdapter extends BaseAdapter {
    Context context;
    ArrayList<IngredientModel> ingredientList;
    LayoutInflater inflter;

    public IngredientsAdapter(Context applicationContext, ArrayList<IngredientModel> ingredientList) {
        this.context = applicationContext;
        this.ingredientList = ingredientList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return ingredientList.size();
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
        view = inflter.inflate(R.layout.ingredients_adapter_layout, null);

        // Lookup view for data population
        TextView tvIngredient = view.findViewById(R.id.ingredientTv);


        // Populate the data into the template view using the data object
        //tvIngredient.setText("" + ingredientList.get(i).getAmount() + " " + ingredientList.get(i).getUnit() + " " + ingredientList.get(i).getIngredientName());
        tvIngredient.setText(ingredientList.get(i).getOriginalString());

        return view;
    }
}
