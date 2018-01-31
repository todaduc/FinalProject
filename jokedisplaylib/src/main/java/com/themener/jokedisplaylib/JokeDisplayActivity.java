package com.themener.jokedisplaylib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class JokeDisplayActivity extends AppCompatActivity {

    TextView jokeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);
        jokeTextView = (TextView) findViewById(R.id.tv_joke);

        if (getIntent().hasExtra("joke")){

            jokeTextView.setText( getIntent().getStringExtra("joke"));

        }
    }
}
