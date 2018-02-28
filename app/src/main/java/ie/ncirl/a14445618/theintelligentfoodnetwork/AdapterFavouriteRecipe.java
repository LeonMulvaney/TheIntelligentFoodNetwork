package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Leon on 27/02/2018.
 */

public class AdapterFavouriteRecipe extends BaseAdapter {
    Context context;
    ArrayList<ModelFavouriteRecipe> favouriteRecipeList;
    LayoutInflater inflter;

    public AdapterFavouriteRecipe(Context applicationContext, ArrayList<ModelFavouriteRecipe> favouriteRecipeList) {
        this.context = applicationContext;
        this.favouriteRecipeList = favouriteRecipeList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return favouriteRecipeList.size();
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
        view = inflter.inflate(R.layout.adapter_favourite_recipes_layout, null);

        // Lookup view for data population
        final TextView tvFavouriteRecipeTitle = view.findViewById(R.id.favouriteRecipeTitleTv);
        ImageView imgFavouriteRecipe = view.findViewById(R.id.favouriteRecipeImg);

        // Populate the data into the template view using the data object
        tvFavouriteRecipeTitle.setText(favouriteRecipeList.get(i).getRecipeTitle());
        Picasso.with(context).load(favouriteRecipeList.get(i).getRecipeImgUrl()).into(imgFavouriteRecipe);


        return view;
    }
}
