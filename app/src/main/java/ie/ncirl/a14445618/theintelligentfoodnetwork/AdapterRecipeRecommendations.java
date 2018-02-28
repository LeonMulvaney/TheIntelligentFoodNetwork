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
 * Created by Leon on 28/02/2018.
 */

public class AdapterRecipeRecommendations extends BaseAdapter {
    Context context;
    ArrayList<ModelRecipeFromIngredient> recommendationList;
    LayoutInflater inflter;

    public AdapterRecipeRecommendations(Context applicationContext, ArrayList<ModelRecipeFromIngredient> recommendationList) {
        this.context = applicationContext;
        this.recommendationList = recommendationList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return recommendationList.size();
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
        view = inflter.inflate(R.layout.adapter_recipe_recommendations, null);
        TextView title = (TextView) view.findViewById(R.id.recommendationTitleTv);
        ImageView image = (ImageView) view.findViewById(R.id.recommendationImg);

        title.setText((CharSequence) recommendationList.get(i).getTitle());
        String imageUrl = recommendationList.get(i).getImageUrl();
        //image.setImageResource(Integer.parseInt(recipesList.get(i).getImageUrl()));
        Picasso.with(context).load(imageUrl).into(image); //Use picasso library to load images instead of setImageResource
        return view;
    }
}
