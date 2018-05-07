package com.example.jamesfra.datamuseandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DatamuseAndroidResultsListener {

    private DatamuseAndroidResultsListener datamuseAndroidResultsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.datamuseAndroidResultsListener = this;

        Button buttonTestAPICall = (Button) findViewById(R.id.buttonTestAPICall);

        buttonTestAPICall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatamuseAndroid datamuseAndroid = new DatamuseAndroid();
                datamuseAndroid.withResultsListener(datamuseAndroidResultsListener).meaningLike("test").soundsLike("ample").get();
            }
        });
    }

    @Override
    public void onResultsSuccess(ArrayList<Word> words) {
        if(words.size() > 0){
            System.out.println("");
        }
    }
}
