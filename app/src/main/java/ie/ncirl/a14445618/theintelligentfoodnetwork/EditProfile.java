package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String userId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference usersRef;

    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView profileIv;
    Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

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

        //Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profileIv = findViewById(R.id.profileIv);

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
        //Android Getting Image From Firebase Storage From: https://stackoverflow.com/questions/38424203/firebase-storage-getting-image-url
        storageReference.child("profileImages/"+userId+"/profileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("-----------------------------------Successfully Downloaded Image");
                Picasso.with(getApplicationContext()).load(uri).into(profileIv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                String uploadTipImage = "https://firebasestorage.googleapis.com/v0/b/theintelligentfoodnetwork.appspot.com/o/profileImages%2Fuploadtip.png?alt=media&token=a97bae0f-2bb3-4b2f-bbb3-adaed98fb5c7";
                Picasso.with(getApplicationContext()).load(uploadTipImage).into(profileIv);
            }
        });

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
    //Android Uploading Profile Images From: https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
    public void updateProfile(View view){
        if(filePath != null)
        {
            StorageReference ref = storageReference.child("profileImages/"+userId+"/profileImage".toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditProfile.this, "Profile Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, "Image Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }

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

    //Method which allows user to access device Gallery to upload a Profile Image
    public void chooseImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Result for ImageUpload
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileIv.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
