package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String userId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference usersRef;

    EditText nameEt;
    EditText phoneEt;
    EditText weightEt;

    String name;
    String email;
    String phone;
    String weight;
    String joined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.edit_profile_action_bar_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_profile);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        usersRef = databaseReference.child("Users/"+userId+"/accountDetails");

        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        weightEt = findViewById(R.id.weightEt);

        getUserData();
    }
    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getUserData() {
        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    name = ds.child("name").getValue().toString();
                    email = ds.child("email").getValue().toString();
                    phone = ds.child("phone").getValue().toString();
                    weight = ds.child("weight").getValue().toString();
                    joined = ds.child("joined").getValue().toString();
                }

                nameEt.setText(name);
                phoneEt.setText(phone);
                weightEt.setText(weight);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void updateProfile(View view){
        //Pushing Data to Firebase From: https://firebase.google.com/docs/database/admin/save-data
        name = nameEt.getText().toString();
        phone = phoneEt.getText().toString();
        weight = weightEt.getText().toString();

        ModelUser updatedUser = new ModelUser(name,weight,email,joined,phone);//Create new User Model when new user is created

        Map<String, ModelUser> updatedUserDetails = new HashMap<>(); //create new Hashmap of type user model

        updatedUserDetails.put("accountData", updatedUser); //Define title and values of Hashmap
        usersRef.setValue(updatedUserDetails); //Push the created Hashmap to the Database (Using the Declared Reference to the Users Table)
        Toast.makeText(getApplicationContext(),"User Account Successfully Updated",Toast.LENGTH_LONG).show();
        finish(); //Finish/Close the Edit Profile
        startActivity(new Intent(EditProfile.this, UserAccount.class)); //Open the Home Activity (As user has successfully signed in)
    }
}
