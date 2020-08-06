package com.example.szymon.mypong;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button playButton=findViewById(R.id.playButton);
        playButton.setOnClickListener(e->openPlayactivity());
        Button close=findViewById(R.id.closeButton);
        close.setOnClickListener(e->exit());
    }
    private void  openPlayactivity(){
        startActivity(new Intent(MainActivity.this,PlayActivity.class));
    }
    private void exit(){
    finish();
    System.exit(0);
    }
}
