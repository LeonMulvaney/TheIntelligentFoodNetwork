package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void openMyFoodNetwork(View view){
        Intent intent = new Intent(this,FoodContents.class);
        startActivity(intent);
    }

    public void openShopping(View view){
        Intent intent = new Intent(this,Shopping.class);
        startActivity(intent);
    }

    public void openRecipes(View view){
        Intent intent = new Intent(this,Recipes.class);
        startActivity(intent);
    }

    public void openFoodSearch(View view){
        Intent intent = new Intent(this,FoodSearch.class);
        startActivity(intent);
    }





}
