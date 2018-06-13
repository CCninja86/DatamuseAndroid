[![Waffle.io - Columns and their card count](https://badge.waffle.io/CCninja86/DatamuseAndroid.png?columns=all)](https://waffle.io/CCninja86/DatamuseAndroid?utm_source=badge) [![Build Status](https://travis-ci.org/CCninja86/DatamuseAndroid.svg?branch=master)](https://travis-ci.org/CCninja86/DatamuseAndroid)

# DatamuseAndroid
A WIP Android Library for acessing the Datamuse Word API. https://www.datamuse.com/api/

### New in version 1.0.2
* Add option to either keep or reset request URL once request has been made. This allows for reuse of the same request in multiple places, i.e. build once, use anywhere.

### New in version 1.0.1
* Add ability to reuse the same DatamuseAndroid object

# How to use

## Add Gradle dependency

`implementation 'nz.co.ninjastudios.datamuseandroid:datamuse-android:1.0.8'`

## Implement Interface

`public class MainActivity extends AppCompatActivity implements DatamuseAndroidResultsListener`

```
@Override
public void onResultsSucess(ArrayList<Word> words) {
    // Your code here
}
```

## To make an API call
```
boolean resetUrl = true;
DatamuseAndroid datamuseAndroid = new DatamuseAndroid(resetUrl)
datamuseAndroid.setResultsListener(myResultsListener);
datamuseAndroid.synonymsOf("practical")
datamuseAndroid.get();
```

# Bugs and Feature Requests

To report a bug or request a feature, open an issue. You can track progress on the waffle.io board. Just click on the badge at the top of this README.
