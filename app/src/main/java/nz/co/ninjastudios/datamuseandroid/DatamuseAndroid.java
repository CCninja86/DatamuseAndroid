package nz.co.ninjastudios.datamuseandroid;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatamuseAndroid {

    private static DatamuseAndroidResultsListener datamuseAndroidResultsListener;
    private static Gson gson;

    private static String requestUrl = "https://api.datamuse.com/words?";
    private static String metadataParam = "md=s";

    private static Map<String, String> validMetadataFlagsMap;
    private static String validMetadataFlagsString = "";

    private boolean resetUrlOnRequest;

    public DatamuseAndroid(boolean resetUrlOnRequest) {
        this.resetUrlOnRequest = resetUrlOnRequest;

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

        validMetadataFlagsString = validMetadataFlagsBuilder.toString();
        validMetadataFlagsString = validMetadataFlagsString.substring(0, validMetadataFlagsString.length() - 2);
    }

    /**
     * Specify the instance of DatamuseAndroidResultsListener to use.
     * @param resultsListener the instance of DatamuseAndroidResultsListener to use
     * @return
     */
    public DatamuseAndroid withResultsListener(DatamuseAndroidResultsListener resultsListener){
        datamuseAndroidResultsListener = resultsListener;

        return this;
    }

    /**
     * Return words with a meaning like the specified word
     * @param word results will have a meaning related to this word
     * @return
     */
    public DatamuseAndroid meaningLike(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "ml=" + word;
        }

        return this;
    }

    /**
     * Return words that sound like the specified word
     * @param word results will be pronounced similarly to this string of characters
     * @return
     */
    public DatamuseAndroid soundsLike(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "sl=" + word;
        }

        return this;
    }

    /**
     * Return words that are spelled like the specified word or pattern (supports wildcards)
     * Supported wildcards are: * (match any number of characters), ? (match exactly one character)
     * @param pattern results will be spelled like this word or pattern
     * @return
     */
    public DatamuseAndroid spelledLike(String pattern){
        if(pattern != null && !pattern.isEmpty()){
            checkForMultipleParams();

            requestUrl += "sp=" + pattern;
        }

        return this;
    }

    /**
     * Return results that are popular nouns modified by the given adjective, e.g. gradual - increase, long - time, etc.
     * @param adjective the adjective
     * @return
     */
    public DatamuseAndroid nounsModifiedByAdjective(String adjective){
        if(adjective != null && !adjective.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_jja=" + adjective;
        }

        return this;
    }

    /**
     * Return results that are popular adjectives used to modify the given noun, e.g. beach - sandy, tree - tall, etc.
     * @param noun the noun
     * @return
     */
    public DatamuseAndroid adjectivesUsedToModifyNoun(String noun){
        if(noun != null && !noun.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_jjb=" + noun;
        }

        return this;
    }

    /**
     * Return words that are synonyms of the specified word
     * @param word results will be synonyms of this word, e.g. ocean - sea
     * @return
     */
    public DatamuseAndroid synonymsOf(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_syn=" + word;
        }

        return this;
    }

    /**
     * Return words that are triggered by the specified word
     * @param word results will be triggered by this word, e.g. cow - milking
     * @return
     */
    public DatamuseAndroid triggeredBy(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_trg=" + word;
        }

        return this;
    }

    /**
     * Return words that are antonyms of the specified word
     * @param word results will be antonyms of this word, e.g. late - early
     * @return
     */
    public DatamuseAndroid antonymsOf(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_ant=" + word;
        }

        return this;
    }

    /**
     * Return results that are direct hypernyms of the specified word, e.g. gondola - boat, because a gondola *is a kind/type of* boat
     * @param word results will be direct hypernyms of this word
     * @return
     */
    public DatamuseAndroid isAKindOf(String word){
        
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_spc=" + word;
        }

        return this;
    }

    /**
     * Return results that are direct hyponyms of the specified word, e.g. boat - gondola, because boat is *more genral than* gondola
     * Other examples:
     * bird - parrot
     * fish - salmon
     * @param word the general term, e.g. boat, bird, fish
     * @return
     */
    public DatamuseAndroid isMoreGeneralThan(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_gen=" + word;
        }

        return this;
    }

    /**
     * Return results that are direct holonyms of the specified word, e.g. car - accelerator, face - eye, etc.
     * @param word The specified word
     * @return
     */
    public DatamuseAndroid comprises(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_com=" + word;
        }

        return this;
    }

    /**
     * Return results that are direct meronyms of the specified word, e.g. trunk - tree, eye - face, etc.
     * @param word The specified word
     * @return
     */
    public DatamuseAndroid partOf(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_par=" + word;
        }

        return this;
    }

    /**
     * Return results that are frequent followers of the specified word, e.g. wreak - havoc, digital - camera, etc.
     * @param word results will be words that frequently appear after this word
     * @return
     */
    public DatamuseAndroid frequentFollowersOf(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_bga=" + word;
        }

        return this;
    }

    /**
     * Return results that are frequent predecessors of the specified word, e.g. havoc - wreak, oomputer - desktop, etc.
     * @param word results will be words that frequently appear before this word
     * @return
     */
    public DatamuseAndroid frequentPredecessorsOf(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_bgb=" + word;
        }

        return this;
    }

    /**
     * Return words that rhyme with the specified word
     * @param word results will rhyme with this word, e.g. spade - aid
     * @return
     */
    public DatamuseAndroid rhymesWith(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_rhy=" + word;
        }

        return this;
    }

    /**
     * Return words that approximately rhyme with the specified word
     * @param word results will approximately rhyme with this word, e.g. forest - chorus
     * @return
     */
    public DatamuseAndroid approximatelyRhymesWith(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_nry=" + word;
        }

        return this;
    }

    /**
     * Return words that sound like the specified word
     * @param word results will sound like this word, e.g. course - coarse
     * @return
     */
    public DatamuseAndroid homophonesOf(String word){
        if(word != null & !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_hom=" + word;
        }

        return this;
    }

    /**
     * Return words that match consonants of the specified word
     * @param word results will match the consonants of the specified word, e.g. sample - simple, semple, sam paul
     * @return
     */
    public DatamuseAndroid consonantMatch(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rel_cns=" + word;
        }

        return this;
    }

    /**
     * Return words that are skewed towards the specified topic words
     * @param topicWords An array of up to 5 topic words to skew the results towards
     * @return
     */
    public DatamuseAndroid topicWords(String[] topicWords){
        if(topicWords.length > 5 || topicWords.length == 0){
            throw new IllegalArgumentException("You must provide at least 1, and no more than 5, topic words");
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
    public DatamuseAndroid leftContext(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "lc=" + word;
        }

        return this;
    }

    /**
     * An optional hint about the word that appears immediately to the right of the target word in a sentence
     * @param word the word that appears immediately to the right of the target word in a sentence
     * @return
     */
    public DatamuseAndroid rightContext(String word){
        if(word != null && !word.isEmpty()){
            checkForMultipleParams();

            requestUrl += "rc=" + word;
        }

        return this;
    }

    /**
     * The maximimum number of results to return. Must not exceed 1000. Default is 100.
     * @param numResults the maximum number of results to return
     * @return
     */
    public DatamuseAndroid maxResults(int numResults){
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
    public DatamuseAndroid setMetadataFlags(String[] flags){
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

        if(metadataParam.contains("r")){
            requestUrl += "&ipa=1";
        }

        return this;
    }

    /**
     * Execute the query. The results will be returned as an ArrayList of Word objects.
     */
    public void get(){
        new ExecuteAPIQueryTask(requestUrl, resetUrlOnRequest).execute();
    }

    private void checkForMultipleParams(){
        if(!requestUrl.substring(requestUrl.length()  - 1).equals("?")){
            requestUrl += "&";
        }
    }

    private static class ExecuteAPIQueryTask extends AsyncTask<Void, Void, Void> {

        private String apiRequestUrl;
        private ArrayList<Word> words;

        private boolean resetUrlOnRequest;

        private ExecuteAPIQueryTask(String url, boolean resetUrlOnRequest){
            this.apiRequestUrl = url;
            this.words = new ArrayList<>();
            this.resetUrlOnRequest = resetUrlOnRequest;
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
            if(resetUrlOnRequest){
                requestUrl = "https://api.datamuse.com/words?";
            }

            datamuseAndroidResultsListener.onResultsSuccess(words);
        }
    }

    public static DatamuseAndroidResultsListener getDatamuseAndroidResultsListener() {
        return datamuseAndroidResultsListener;
    }

    public static void setDatamuseAndroidResultsListener(DatamuseAndroidResultsListener datamuseAndroidResultsListener) {
        DatamuseAndroid.datamuseAndroidResultsListener = datamuseAndroidResultsListener;
    }

    public static String getRequestUrl() {
        return requestUrl;
    }

    public static void setRequestUrl(String requestUrl) {
        DatamuseAndroid.requestUrl = requestUrl;
    }

    public static String getMetadataParam() {
        return metadataParam;
    }

    public static void setMetadataParam(String metadataParam) {
        DatamuseAndroid.metadataParam = metadataParam;
    }

    public static Map<String, String> getValidMetadataFlagsMap() {
        return validMetadataFlagsMap;
    }

    public static void setValidMetadataFlagsMap(Map<String, String> validMetadataFlagsMap) {
        DatamuseAndroid.validMetadataFlagsMap = validMetadataFlagsMap;
    }

    public static String getValidMetadataFlagsString() {
        return validMetadataFlagsString;
    }

    public static void setValidMetadataFlagsString(String validMetadataFlagsString) {
        DatamuseAndroid.validMetadataFlagsString = validMetadataFlagsString;
    }
}
