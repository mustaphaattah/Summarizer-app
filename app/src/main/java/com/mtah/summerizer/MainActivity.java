package com.mtah.summerizer;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(new Runnable (){
            @Override
            public void run() {
                try {
                    sleep(1500);
                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                } catch (Exception e){
                    Log.e(TAG, "run: ", e);
                }
            }
        });
        thread.start();

    }
}
