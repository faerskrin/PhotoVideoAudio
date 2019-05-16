package com.example.wsapp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import static java.lang.Thread.sleep;

public class Splash extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread disp = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent intent = new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }
        });

        disp.start();
    }
}
