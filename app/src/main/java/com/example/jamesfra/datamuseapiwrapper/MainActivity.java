package com.example.jamesfra.datamuseapiwrapper;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DatamuseAPIResultsListener {

    private DatamuseAPIResultsListener datamuseAPIResultsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.datamuseAPIResultsListener = this;

        Button buttonTestAPICall = (Button) findViewById(R.id.buttonTestAPICall);

        buttonTestAPICall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatamuseAPI datamuseAPI = new DatamuseAPI();
                datamuseAPI.withResultsListener(datamuseAPIResultsListener).meaningLike("test").get();
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
