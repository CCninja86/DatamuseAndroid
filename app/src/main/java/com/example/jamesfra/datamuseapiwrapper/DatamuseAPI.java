package com.example.jamesfra.datamuseapiwrapper;

import android.content.Context;
import android.os.AsyncTask;

public class DatamuseAPI {

    private Context context;
    private DatamuseAPIResultsListener datamuseAPIResultsListener;

    private String requestUrl = "https://api.datamuse.com/words?";

    public DatamuseAPI(Context context, DatamuseAPIResultsListener datamuseAPIResultsListener) {
        this.context = context;
        this.datamuseAPIResultsListener = datamuseAPIResultsListener;
    }

    public void meaningLike(String word){
        checkForMultipleParams();

        requestUrl += "ml=" + word;
    }

    public void soundsLike(String word){
        checkForMultipleParams();

        requestUrl += "sl=" + word;
    }

    public void execute(){

    }

    private void checkForMultipleParams(){
        if(!requestUrl.substring(requestUrl.length()  - 1).equals("?")){
            requestUrl += "&";
        }
    }


    private class ExecuteAPIQueryTask extends AsyncTask<Void, Void, Void> {


        public ExecuteAPIQueryTask(){

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
