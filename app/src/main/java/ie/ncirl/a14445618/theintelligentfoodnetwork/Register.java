package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Android Firebase Authentication From: https://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference usersRef;

    String name;
    String weight;
    String email;
    String joined;
    String password;

    EditText nameEt;
    EditText weightEt;
    EditText emailEt;
    EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Android Hide Action Bar From: https://stackoverflow.com/questions/19545370/android-how-to-hide-actionbar-on-certain-activities
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        usersRef = databaseReference.child("Users");

        nameEt = findViewById(R.id.nameEt);
        weightEt = findViewById(R.id.weightEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);

        //Java Getting Current Date From: https://www.javatpoint.com/java-get-current-date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        joined = formatter.format(currentDate).toString();
    }

    public void viewLogin(View view){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    public void register(View view){
        name = nameEt.getText().toString().trim();
        weight = weightEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();

        if(email.equals("") || password.equals("")){
            Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
        }

        else if(!(email.contains("@"))){
            Toast.makeText(this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
        }

        else if(password.length()<6){
            Toast.makeText(this, "Password is too short. Minimum of 6 Characters.", Toast.LENGTH_SHORT).show();
        }

        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this, "Registration failed. \n" + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, "Successfully Registered. Welcome \n " + email, Toast.LENGTH_SHORT).show();

                                //Pushing Data to Firebase From: https://firebase.google.com/docs/database/admin/save-data
                                String userId = mAuth.getUid().toString();
                                ModelUser newUser = new ModelUser(name,weight,email,joined);

                                Map<String, ModelUser> newUserDetails = new HashMap<>();

                                newUserDetails.put("accountDetails", newUser);
                                usersRef.child(userId).setValue(newUserDetails);
//                                usersRef.child(userId).setValue("Favourites");
//                                usersRef.child(userId).setValue("FridgeItems");
//                                usersRef.child(userId).setValue("ShoppingList");

                                startActivity(new Intent(Register.this, Home.class));
                                finish();
                            }
                        }
                    });
        }
    }


}
