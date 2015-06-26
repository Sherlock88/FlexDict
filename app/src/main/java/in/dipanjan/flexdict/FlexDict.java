package in.dipanjan.flexdict;


import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.graphics.Color;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.graphics.PixelFormat;
import mehdi.sakout.fancybuttons.FancyButton;
import android.support.v7.app.ActionBarActivity;
import java.util.ArrayList;


public class FlexDict extends ActionBarActivity implements View.OnClickListener {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int listCount, wordListCount;

        // Turn screen on
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        /* http://stackoverflow.com/questions/19078461/android-null-pointer-exception-findviewbyid */
        setContentView(R.layout.activity_flex_dict);
        LinearLayout container = (LinearLayout)findViewById(R.id.container);

        // Check if database path is set
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String dbPath = preferences.getString("@string/pref_dbpath", "--- Not set ---");
        if(dbPath.compareTo("--- Not set ---") == 0)
            openPreferences();

        // Database path is set
        dbPath = preferences.getString("@string/pref_dbpath", "--- Not set ---");
        DatabaseHandler databasehandler = new DatabaseHandler(this, dbPath);
        ArrayList<String> tables = databasehandler.getTableNames();
        wordListCount = tables.size();

        /* http://stackoverflow.com/questions/30287268/adjust-size-and-gap-between-fancybutton-placed-on-linearlayout */
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 2;
        //lp.gravity = Gravity.CENTER;

        for(listCount = 0; listCount < (wordListCount - 1); listCount++)
        {
            /* https://github.com/medyo/fancybuttons */
            FancyButton btnWordList = new FancyButton(this);
            btnWordList.setId(listCount);
            btnWordList.setText("WordList " + (listCount + 1));
            btnWordList.setTextSize(20);
            btnWordList.setTextAlignment(FancyButton.TEXT_ALIGNMENT_CENTER);
            //btnWordList.setGravity(Gravity.CENTER);
            btnWordList.setBackgroundColor(Color.parseColor("#3b5998"));
            btnWordList.setFocusBackgroundColor(Color.parseColor("#5474b8"));
            btnWordList.setIconResource("\uf04b");
            btnWordList.setRadius(10);
            btnWordList.setLayoutParams(lp);
            btnWordList.setOnClickListener(this);
            container.addView(btnWordList);
        }
    }

    private void openPreferences() {
        Intent intent = new Intent(this, AppPreference.class);
        startActivityForResult(intent, 1);
        return;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        /*
        * http://www.java-samples.com/showtutorial.php?tutorialid=1525
        * http://stackoverflow.com/questions/7980627/pressing-back-button-did-not-go-back-to-previous-activity-android
        */
        Intent intent = new Intent(this, ShowList.class);
        Bundle params = new Bundle();
        params.putInt("WordList", id);
        intent.putExtras(params);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings)
            openPreferences();

        return super.onOptionsItemSelected(item);
    }
}
