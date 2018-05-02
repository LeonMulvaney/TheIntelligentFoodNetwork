package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Leon on 24/02/2018.
 */

//Custom Adapter  From: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
//Custom BaseAdapter From: http://abhiandroid.com/ui/baseadapter-tutorial-example.html
public class AdapterIngredients extends BaseAdapter {
    Context context;
    ArrayList<ModelIngredient> ingredientList;
    LayoutInflater inflter;

    public AdapterIngredients(Context applicationContext, ArrayList<ModelIngredient> ingredientList) {
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
        view = inflter.inflate(R.layout.adapter_ingredients_layout, null);

        // Lookup view for data population
        TextView tvIngredient = view.findViewById(R.id.ingredientTv);


        // Populate the data into the template view using the data object
        tvIngredient.setText(ingredientList.get(i).getOriginalString());

        return view;
    }
}
