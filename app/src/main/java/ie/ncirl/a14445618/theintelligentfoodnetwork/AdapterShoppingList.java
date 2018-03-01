package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Leon on 01/03/2018.
 */

public class AdapterShoppingList extends BaseAdapter {
    Context context;
    ArrayList<ModelShoppingListItem> shoppingList;
    LayoutInflater inflter;

    public AdapterShoppingList(Context applicationContext, ArrayList<ModelShoppingListItem> shoppingList) {
        this.context = applicationContext;
        this.shoppingList = shoppingList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return shoppingList.size();
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
        view = inflter.inflate(R.layout.adapter_shopping_list, null);

        // Lookup view for data population
        TextView shoppingListItemTitle = view.findViewById(R.id.shoppingListItemTv);
        ImageView shoppingListItemImage = view.findViewById(R.id.shoppingListItemImg);

        /*  To set the images in the shopping list I am using a url which points to the Spoonacular Website -
            The Api does not support grabbing ingredient images on their own, only images linked to a recipe  -
            This would have meant that I would have to get a recipe for every item/ingredient in a users shopping list
            I comprised a solution to this issue by URL string manipulation
            Java Replace Characters in String From: https://stackoverflow.com/questions/5262554/replace-space-to-hyphen
        */
        String itemToLowerCase = StringUtils.lowerCase(shoppingList.get(i).getShoppingListItemTitle()); //Grab the Item from the list and parse to lower case
        String itemWithRemovedSpaces = itemToLowerCase.replace(" ","-"); //Once the item is in lower case, replace spaces with dashes (To suit the URL)
        // Populate the data into the template view using the data object
        shoppingListItemTitle.setText(shoppingList.get(i).getShoppingListItemTitle());
        Picasso.with(context).load("https://spoonacular.com/cdn/ingredients_100x100/" + itemWithRemovedSpaces + ".jpg").into(shoppingListItemImage); //Combine URL and load image into Views
        return view;
    }
}
