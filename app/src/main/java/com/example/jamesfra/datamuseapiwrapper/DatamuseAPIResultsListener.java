package com.example.jamesfra.datamuseapiwrapper;

import java.util.ArrayList;

public interface DatamuseAPIResultsListener {
    public void onResultsSuccess(ArrayList<Word> words);
}
