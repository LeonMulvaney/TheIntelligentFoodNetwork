package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

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

    ArrayList<String> foodList;
    ArrayList<String> shoppingList;


    public AdapterIngredients(Context applicationContext, ArrayList<ModelIngredient> ingredientList, ArrayList<String> foodList,ArrayList<String>shoppingList) {
        this.context = applicationContext;
        this.ingredientList = ingredientList;
        this.foodList = foodList;
        this.shoppingList = shoppingList;
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
        ImageView alreadyInShoppingListIcon = view.findViewById(R.id.alreadyInShoppingListIcon);

        // Populate the data into the template view using the data object
        //Android Set color programmatically From: https://stackoverflow.com/questions/4602902/how-to-set-the-text-color-of-textview-in-code
        if(foodList.contains(ingredientList.get(i).getIngredientName().toLowerCase().trim())){
            //If match, highlight in Green
            tvIngredient.setTextColor(Color.parseColor("#159B4A"));
            tvIngredient.setText(ingredientList.get(i).getOriginalString());
        }
        else{
            //Just Parse the value
            tvIngredient.setText(ingredientList.get(i).getOriginalString());
        }

        if(shoppingList.contains(ingredientList.get(i).getIngredientName())){
            //Set Image porgramatically
            alreadyInShoppingListIcon.setBackgroundResource(R.drawable.ic_shopping_cart);
        }
        else{
            //Do nothing
        }

        return view;
    }


}
