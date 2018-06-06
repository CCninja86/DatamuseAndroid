package nz.co.ninjastudios.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jamesfra.datamuseandroid.DatamuseAndroid;
import com.example.jamesfra.datamuseandroid.DatamuseAndroidResultsListener;
import com.example.jamesfra.datamuseandroid.Word;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DatamuseAndroidResultsListener {

    DatamuseAndroid datamuseAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datamuseAndroid = new DatamuseAndroid(true);
        datamuseAndroid.setResultsListener(this);
        datamuseAndroid.synonymsOf("practical");
        datamuseAndroid.get();
    }

    @Override
    public void onResultsSuccess(ArrayList<Word> words) {
        for(Word word : words){
            String wordString = word.getWord();
            int wordScore = word.getScore();
            String[] wordTags = word.getTags();
            int numSyllabled = word.getNumSyllables();
        }
    }
}
