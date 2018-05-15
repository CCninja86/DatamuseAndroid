# DatamuseAndroid
A WIP Android Library for acessing the Datamuse Word API. https://www.datamuse.com/api/

# How to use

## Add Gradle dependency

`implementation 'nz.co.ninjastudios.datamuseandroid:datamuse-android:1.0.1'`

## Implement Interface

`public class MainActivity extends AppCompatActivity implements DatamuseAndroidResultsListener`

```
@Override
public void onResultsSucess(ArrayList<Word> words) {
    // Your code here
}
```

## To make an API call (example for getting synonyms of a word)

### Option 1

`new DatamuseAndroid().withResultsListener(this).synonymsOf("practical").get();`

### Option 2 (WIP)

This option will be available shortly in the next version, which will allow reuse of the same DatamuseAndroid object
