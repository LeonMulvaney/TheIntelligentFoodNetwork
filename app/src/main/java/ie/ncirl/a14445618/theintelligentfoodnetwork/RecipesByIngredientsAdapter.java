package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Leon on 14/02/2018.
 */

//Custom BaseAdapter From: http://abhiandroid.com/ui/baseadapter-tutorial-example.html
public class RecipesByIngredientsAdapter extends BaseAdapter {
    Context context;
    ArrayList<RecipeFromIngredientModel> recipesList;
    LayoutInflater inflter;

    public RecipesByIngredientsAdapter(Context applicationContext, ArrayList<RecipeFromIngredientModel> recipesList) {
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
        view = inflter.inflate(R.layout.recipes_by_ingredient_adapter_layout, null);
        TextView title = (TextView) view.findViewById(R.id.tvRecipeTitle);
        ImageView image = (ImageView) view.findViewById(R.id.recipeImage);
        title.setText((CharSequence) recipesList.get(i).getTitle());
        String imageUrl = recipesList.get(i).getImageUrl();
        //image.setImageResource(Integer.parseInt(recipesList.get(i).getImageUrl()));
        Picasso.with(context).load(imageUrl).into(image); //Use picasso library to load images instead of setImageResource
        return view;
    }
}
