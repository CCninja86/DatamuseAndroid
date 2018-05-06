package com.example.jamesfra.datamuseapiwrapper;

public class Word {

    private String word;
    private int score;
    private String[] tags;
    private int numSyllables;

    public Word(){
        // Don't want array to be null when Gson parses the JSON string
        tags = new String[]{};
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getNumSyllables() {
        return numSyllables;
    }

    public void setNumSyllables(int numSyllables) {
        this.numSyllables = numSyllables;
    }
}
