package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String userId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference usersRef;

    String name;
    String email;
    String weight;
    String joined;

    TextView nameTv;
    TextView emailTv;
    TextView weightTv;
    TextView joinedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.account_action_bar_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_account);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        usersRef = databaseReference.child("Users/"+userId);

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        weightTv = findViewById(R.id.weightTv);
        joinedTv = findViewById(R.id.joinedTv);

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
                     weight = ds.child("weight").getValue().toString();
                     joined = ds.child("joined").getValue().toString();
                }

                nameTv.setText(name);
                emailTv.setText(email);
                weightTv.setText(weight + " Kg");
                joinedTv.setText(joined);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
