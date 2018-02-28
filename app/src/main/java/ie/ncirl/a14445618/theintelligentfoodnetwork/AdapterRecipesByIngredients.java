package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Leon on 14/02/2018.
 */

//Custom BaseAdapter From: http://abhiandroid.com/ui/baseadapter-tutorial-example.html
public class AdapterRecipesByIngredients extends BaseAdapter {
    Context context;
    ArrayList<ModelRecipeFromIngredient> recipesList;
    LayoutInflater inflter;

    public AdapterRecipesByIngredients(Context applicationContext, ArrayList<ModelRecipeFromIngredient> recipesList) {
        this.context = applicationContext;
        this.recipesList = recipesList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return recipesList.size();
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
        view = inflter.inflate(R.layout.adapter_recipes_by_ingredient_layout, null);
        TextView title = (TextView) view.findViewById(R.id.tvRecipeTitle);
        ImageView image = (ImageView) view.findViewById(R.id.recipeImage);
        title.setText((CharSequence) recipesList.get(i).getTitle());
        String imageUrl = recipesList.get(i).getImageUrl();
        //image.setImageResource(Integer.parseInt(recipesList.get(i).getImageUrl()));
        Picasso.with(context).load(imageUrl).into(image); //Use picasso library to load images instead of setImageResource
        return view;
    }
}
