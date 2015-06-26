package in.dipanjan.flexdict;


import android.os.Bundle;
import java.util.ArrayList;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.GestureDetector;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


public class ShowList extends AppCompatActivity {
    private float startX, endX;
    private int wordListID, wordID = 1, wordCount;
    private String dbTable;
    static final int MIN_DISTANCE = 150;
    static final int REQ_SELECTED_WORD_ID = 1;
    private DatabaseHandler databaseHandler;
    GestureDetector scrollViewGestureDetector;
    View.OnTouchListener scrollViewGestureListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Intent localIntent = getIntent();
        Bundle params = localIntent.getExtras();
        wordListID = params.getInt("WordList");
        setTitle("WordList " + (wordListID + 1));

        // Get database path
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String dbPath = preferences.getString("@string/pref_dbpath", "--- Not set ---");

        // Get word list
        databaseHandler = new DatabaseHandler(this, dbPath);
        ArrayList<String> dbTables = databaseHandler.getTableNames();
        dbTable = dbTables.get(wordListID);
        wordCount = databaseHandler.getWordCount(dbTable);

        // Show the first word
        displayWord(wordID);

        // Set swipe listener to switch words
        scrollViewGestureDetector = new GestureDetector(new ScrollViewGestureListener());
        scrollViewGestureListener = new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if (scrollViewGestureDetector.onTouchEvent(event))
                {
                    return true;
                }
                else{
                    return false;
                }
            }
        };

        ScrollView scrollview_show_list = (ScrollView)findViewById(R.id.scrollview_show_list);
        scrollview_show_list.setOnTouchListener(scrollViewGestureListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQ_SELECTED_WORD_ID:
                if(data != null && resultCode == RESULT_OK)
                {
                    int selectedWordID = data.getIntExtra("SeletedWordID", 0);
                    wordID = selectedWordID;
                    displayWord(selectedWordID);
                }
                break;
        }
    }


    /*
    * Without ScrollView: http://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    * With ScrollView   : http://stackoverflow.com/questions/8330187/gesture-detection-and-scrollview-issue
    */
    class ScrollViewGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            final int SWIPE_MIN_DISTANCE = 120;
            final int SWIPE_MAX_OFF_PATH = 250;
            final int SWIPE_THRESHOLD_VELOCITY = 200;

            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // Show previous word
                if(wordID == 1)
                    return false;
                else
                {
                    wordID--;
                    displayWord(wordID);
                }

            }

            else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // Show next word
                if(wordID == wordCount)
                    return false;
                else
                {
                    wordID++;
                    displayWord(wordID);
                }
            }

            return false;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return scrollViewGestureDetector.onTouchEvent(ev);
    }


    private void displayWord(int wordID) {
        // Fetch word
        String[] strPartsOfSpeech = new String[]{"Noun", "Pronoun", "Adjective", "Verb", "Adverb", "Preposition", "Conjunction", "Interjection"};
        Word word = databaseHandler.getWord(dbTable, wordID);

        // Display word
        TextView txtWord = (TextView)findViewById(R.id.txtWord);
        txtWord.setText(word.strWord + " (" + wordID + "/" + wordCount + ")");
        TextView txtDefinition = (TextView)findViewById(R.id.txtDefinition);
        txtDefinition.setText(word.strDefinition);
        TextView txtPartsOfSpeech = (TextView)findViewById(R.id.txtPartsOfSpeech);
        txtPartsOfSpeech.setText(strPartsOfSpeech[word.partsOfSpeech - 1]);
        TextView txtUsage = (TextView)findViewById(R.id.txtUsage);
        txtUsage.setText(word.strUsage);
        TextView txtSynonym = (TextView)findViewById(R.id.txtSynonym);
        txtSynonym.setText(word.strSynonym);
        TextView txtAntonym = (TextView)findViewById(R.id.txtAntonym);
        txtAntonym.setText(word.strAntonym);
        ImageView imgVisual = (ImageView)findViewById(R.id.imgVisual);
        imgVisual.setImageBitmap(word.getVisual());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_all_words, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_list_all_words) {
            Intent intent = new Intent(this, AllWordsList.class);
            Bundle params = new Bundle();
            ArrayList<String> allWords = databaseHandler.getAllWordsOnly(dbTable);
            params.putStringArrayList("AllWords", allWords);
            intent.putExtras(params);
            startActivityForResult(intent, REQ_SELECTED_WORD_ID);
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}
