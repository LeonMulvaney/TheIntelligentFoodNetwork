package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int DELAY_MILLISECONDS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //From https://stackoverflow.com/questions/8456835/how-to-disable-action-bar-permanently
        //Hide Action Bar On Splash Screen --------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //SplashScreen from Lecture Notes Dr Anu Sahni--------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        }, DELAY_MILLISECONDS);


    }
}
