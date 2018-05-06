package com.example.jamesfra.datamuseapiwrapper;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatamuseAPI {

    private static DatamuseAPIResultsListener datamuseAPIResultsListener;
    private static Gson gson;

    private static String requestUrl = "https://api.datamuse.com/words?";
    private static String metadataParam = "md=s";

    private static Map<String, String> validMetadataFlagsMap;
    private static String validMetadataFlagsString = "";

    public DatamuseAPI() {
        gson = new Gson();
        validMetadataFlagsMap = new HashMap<>();
        validMetadataFlagsMap.put("d", "Definitions");
        validMetadataFlagsMap.put("p", "Parts of speech");
        validMetadataFlagsMap.put("r", "Pronunciation");
        validMetadataFlagsMap.put("f", "Word frequency");

        StringBuilder validMetadataFlagsBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : validMetadataFlagsMap.entrySet()){
            validMetadataFlagsBuilder.append(entry.getKey()).append(" (").append(entry.getValue()).append("), ");
        }

        validMetadataFlagsString = validMetadataFlagsString.substring(0, validMetadataFlagsString.length() - 2);
    }

    /**
     * Specify the instance of DatamuseAPIResultsListener to use.
     * @param resultsListener the instance of DatamuseAPIResultsListener to use
     * @return
     */
    public DatamuseAPI withResultsListener(DatamuseAPIResultsListener resultsListener){
        datamuseAPIResultsListener = resultsListener;

        return this;
    }

    /**
     * Return words with a meaning like the specified word
     * @param word results will have a meaning related to this word
     * @return
     */
    public DatamuseAPI meaningLike(String word){
        checkForMultipleParams();

        requestUrl += "ml=" + word;

        return this;
    }

    /**
     * Return words that sound like the specified word
     * @param word results will be pronounced similarly to this string of characters
     * @return
     */
    public DatamuseAPI soundsLike(String word){
        checkForMultipleParams();

        requestUrl += "sl=" + word;

        return this;
    }

    /**
     * Return words that are spelled like the specified word or pattern (supports wildcards)
     * Supported wildcards are: * (match any number of characters), ? (match exactly one character)
     * @param pattern results will be spelled like this word or pattern
     * @return
     */
    public DatamuseAPI spelledLike(String pattern){
        checkForMultipleParams();

        requestUrl += "sp=" + pattern;

        return this;
    }

    /**
     * Return words that are synonyms of the specified word
     * @param word results will be synonyms of this word, e.g. ocean -> sea
     * @return
     */
    public DatamuseAPI synonymsOf(String word){
        checkForMultipleParams();

        requestUrl += "rel_syn=" + word;

        return this;
    }

    /**
     * Return words that are triggered by the specified word
     * @param word results will be triggered by this word, e.g. cow -> milking
     * @return
     */
    public DatamuseAPI triggeredBy(String word){
        checkForMultipleParams();

        requestUrl += "rel_trg=" + word;

        return this;
    }

    /**
     * Return words that are antonyms of the specified word
     * @param word results will be antonyms of this word, e.g. late -> early
     * @return
     */
    public DatamuseAPI antonymsOf(String word){
        checkForMultipleParams();

        requestUrl += "rel_ant=" + word;

        return this;
    }

    /**
     * Return words that rhyme with the specified word
     * @param word results will rhyme with this word, e.g. spade -> aid
     * @return
     */
    public DatamuseAPI rhymesWith(String word){
        checkForMultipleParams();

        requestUrl += "rel_rhy=" + word;

        return this;
    }

    /**
     * Return words that approximately rhyme with the specified word
     * @param word results will approximately rhyme with this word, e.g. forest -> chorus
     * @return
     */
    public DatamuseAPI approximatelyRhymesWith(String word){
        checkForMultipleParams();

        requestUrl += "rel_nry=" + word;

        return this;
    }

    /**
     * Return words that sound like the specified word
     * @param word results will sound like this word, e.g. course -> coarse
     * @return
     */
    public DatamuseAPI homophonesOf(String word){
        checkForMultipleParams();

        requestUrl += "rel_hom=" + word;

        return this;
    }

    /**
     * Return words that match consonants of the specified word
     * @param word results will match the consonants of the specified word, e.g. sample -> simple, semple, sam paul
     * @return
     */
    public DatamuseAPI consonantMatch(String word){
        checkForMultipleParams();

        requestUrl += "rel_cns=" + word;

        return this;
    }

    /**
     * Return words that are skewed towards the specified topic words
     * @param topicWords An array of up to 5 topic words to skew the results towards
     * @return
     */
    public DatamuseAPI topicWords(String[] topicWords){
        if(topicWords.length > 5){
            throw new IllegalArgumentException("You must provide no more than 5 topic words");
        }

        checkForMultipleParams();

        requestUrl += "topics=";

        for (String word : topicWords){
            requestUrl += word + ",";
        }

        requestUrl = requestUrl.substring(0, requestUrl.length() - 1);

        return this;
    }

    /**
     * An optional hint about the word that appears immediately to the left of the target word in a sentence
     * @param word the word that appears immediately to the left of the target word in a sentence
     * @return
     */
    public DatamuseAPI leftContext(String word){
        checkForMultipleParams();

        requestUrl += "lc=" + word;

        return this;
    }

    /**
     * An optional hint about the word that appears immediately to the right of the target word in a sentence
     * @param word the word that appears immediately to the right of the target word in a sentence
     * @return
     */
    public DatamuseAPI rightContext(String word){
        checkForMultipleParams();

        requestUrl += "rc=" + word;

        return this;
    }

    /**
     * The maximimum number of results to return. Must not exceed 1000. Default is 100.
     * @param numResults the maximum number of results to return
     * @return
     */
    public DatamuseAPI maxResults(int numResults){
        if(numResults > 1000){
            throw new IllegalArgumentException("Maximum number of results must not exceed 1000");
        }

        checkForMultipleParams();

        requestUrl += "max=" + numResults;

        return this;
    }

    /**
     * A list of single-letter codes requesting that extra lexical knowledge be included with the results. Valid codes are:
     * d (Definitions)
     * p (Parts of speech)
     * r (Pronunciation)
     * f (Word frequency)
     * There is also a fifth code, s (Syllable count), but this is included by default for consistency as some query parameters include syllable count by default
     * @param flags the list of single-letter codes
     * @return
     */
    public DatamuseAPI setMetadataFlags(String[] flags){
        boolean validFlags = true;

        for (String flag : flags){
            if(!validMetadataFlagsMap.containsKey(flag)){
                validFlags = false;

                break;
            }
        }

        if(!validFlags){
            String validFlagString = "";

            throw new IllegalArgumentException("Invalid flag characters detected. Valid characters are: " + validMetadataFlagsString);
        } else if (flags.length > 4) {
            throw new IllegalArgumentException("Too many metadata flags provided. You must provide a maximum of four metatdata flags from this list: " + validMetadataFlagsString);
        }

        checkForMultipleParams();

        for (String flag : flags){
            metadataParam += flag;
        }

        requestUrl += metadataParam;

        return this;
    }

    /**
     * Execute the query. The results will be returned as an ArrayList of Word objects.
     */
    public void get(){
        new ExecuteAPIQueryTask(requestUrl).execute();
    }

    private void checkForMultipleParams(){
        if(!requestUrl.substring(requestUrl.length()  - 1).equals("?")){
            requestUrl += "&";
        }
    }

    private static class ExecuteAPIQueryTask extends AsyncTask<Void, Void, Void> {

        private String apiRequestUrl;
        private ArrayList<Word> words;

        private ExecuteAPIQueryTask(String url){
            this.apiRequestUrl = url;
            this.words = new ArrayList<>();
        }

        @Override
        protected void onPreExecute(){
            apiRequestUrl += "&md=s";
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(apiRequestUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuilder jsonBuilder = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
                    jsonBuilder.append(line);
                }

                String json = jsonBuilder.toString();

                if(!json.equals("")){
                    words = gson.fromJson(json, new TypeToken<ArrayList<Word>>(){}.getType());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            datamuseAPIResultsListener.onResultsSuccess(words);
        }
    }

}
