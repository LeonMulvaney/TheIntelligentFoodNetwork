package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
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

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    String email;
    String password;

    EditText emailEt;
    EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Android Hide Action Bar From: https://stackoverflow.com/questions/19545370/android-how-to-hide-actionbar-on-certain-activities
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
    }

    public void viewRegister(View view){
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }

    public void viewHome(View view){
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }

    public void login(View view){
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
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public static final String TAG = "";

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "Successful Login. \n Welcome " + email);
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                System.out.println("--------------------------------------------------" + task.toString());
                                System.out.println("signInWithEmail:failure" + task.getException().toString());
                            }

                        }
                    });
        }
    }



}
