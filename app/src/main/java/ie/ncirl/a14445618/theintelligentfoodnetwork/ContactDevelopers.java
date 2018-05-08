package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class ContactDevelopers extends AppCompatActivity {

    //Firebase Authentication
    private FirebaseAuth mAuth;
    private String userId;

    private EditText subjectEt;
    private EditText detailsEt;

    private String subject;
    private String details;

    private String developerEmail = "leonmul96@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.contact_developers_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_contact_developers);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        subjectEt = findViewById(R.id.subjectEt);
        detailsEt = findViewById(R.id.detailsEt);

    }

    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //Android Snackbar From: https://spin.atomicobject.com/2017/07/10/android-snackbar-tutorial/
    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    public void sendData(View view){
        subject = subjectEt.getText().toString();
        details = detailsEt.getText().toString();

        if(subject.isEmpty() || details.isEmpty()){
            View thisView = findViewById(R.id.contactDevelopersActivity);
            String message = "Please fill in the Required Fields";
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(thisView, message, duration);
        }

        else{
            //Android Send email intent From: https://stackoverflow.com/questions/8701634/send-email-intent
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{developerEmail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, details);
            startActivity(Intent.createChooser(emailIntent, "Please choose default email client"));
        }

    }
}
