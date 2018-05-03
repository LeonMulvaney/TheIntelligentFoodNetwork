package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
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
        Intent intent = new Intent(this,NutrientSearch.class);
        startActivity(intent);
    }

    public void openAccount(View view){
        Intent intent = new Intent(this,UserAccount.class);
        startActivity(intent);

    }

    public void logout(View view){
        Intent intent = new Intent(Home.this, Login.class);
        mAuth.getInstance().signOut();
        startActivity(intent);
        finish();

    }





}
