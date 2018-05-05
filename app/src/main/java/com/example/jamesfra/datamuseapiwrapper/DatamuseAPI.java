package com.example.jamesfra.datamuseapiwrapper;

import android.content.Context;
import android.os.AsyncTask;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class DatamuseAPI {

    private Context context;
    private DatamuseAPIResultsListener datamuseAPIResultsListener;

    private static String requestUrl = "https://api.datamuse.com/words?";

    public DatamuseAPI() {

    }
    
    public static void meaningLike(String word){
        checkForMultipleParams();

        requestUrl += "ml=" + word;
    }

    public static void soundsLike(String word){
        checkForMultipleParams();

        requestUrl += "sl=" + word;
    }

    public static void spelledLike(String pattern){
        checkForMultipleParams();

        requestUrl += "sp=" + pattern;
    }

    public static void synonymsOf(String word){
        checkForMultipleParams();

        requestUrl += "rel_syn=" + word;
    }

    public static void triggeredBy(String word){
        checkForMultipleParams();

        requestUrl += "rel_trg=" + word;
    }

    public static void antonymsOf(String word){
        checkForMultipleParams();

        requestUrl += "rel_ant=" + word;
    }

    public static void rhymesWith(String word){
        checkForMultipleParams();

        requestUrl += "rel_rhy=" + word;
    }

    public static void approximatelyRhymesWith(String word){
        checkForMultipleParams();

        requestUrl += "rel_nry=" + word;
    }

    public static void homophonesOf(String word){
        checkForMultipleParams();

        requestUrl += "rel_hom=" + word;
    }

    public static void consonantMatch(String word){
        checkForMultipleParams();

        requestUrl += "rel_cns=" + word;
    }

    public static void topicWords(ArrayList<String> topicWords){
        if(topicWords.size() > 5){
            throw new IllegalArgumentException("You must provide no more than 5 topic words");
        }

        checkForMultipleParams();

        requestUrl += "topics=";

        for (String word : topicWords){
            requestUrl += word + ",";
        }

        requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
    }

    public static void leftContext(String word){
        checkForMultipleParams();

        requestUrl += "lc=" + word;
    }

    public static void rightContext(String word){
        checkForMultipleParams();

        requestUrl += "rc=" + word;
    }

    public static void maxResults(int numResults){
        checkForMultipleParams();

        requestUrl += "max=" + numResults;
    }

    public static void get(){
        new ExecuteAPIQueryTask(requestUrl).execute();
    }

    private static void checkForMultipleParams(){
        if(!requestUrl.substring(requestUrl.length()  - 1).equals("?")){
            requestUrl += "&";
        }
    }


    private static class ExecuteAPIQueryTask extends AsyncTask<Void, Void, Void> {

        private String url;

        private ExecuteAPIQueryTask(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }

        @Override
        protected void onPostExecute(Void result){

        }
    }

}
