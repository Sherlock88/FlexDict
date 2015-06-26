package in.dipanjan.flexdict;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import com.nononsenseapps.filepicker.FilePickerActivity;

public class AppPreference extends PreferenceActivity {

    private static final int FILE_CODE = 11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* http://rominirani.com/android-preferences-tutorial */
        addPreferencesFromResource(R.xml.preferences);

        /*
        * http://stackoverflow.com/questions/13669154/how-to-call-activity-from-preferenceactivity
        * http://stackoverflow.com/questions/5298370/how-to-add-a-button-to-a-preferencescreen-android
        * http://www.tutorialspoint.com/android/android_shared_preferences.htm
        */
        Preference dbChooser = (Preference) findPreference(getString(R.string.pref_filechooser));
        dbChooser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent filePicker = new Intent(getApplicationContext(), FilePickerActivity.class);
                startActivityForResult(filePicker, FILE_CODE);
                return true;
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String strUserName = preferences.getString("username", "NA"); //Log.v("FlexDict", strUserName);
        boolean bAppUpdates = preferences.getBoolean("showSplashScreen", false); //Log.v("FlexDict", Boolean.toString(bAppUpdates));
        String downloadType = preferences.getString("studyMode", "1"); //Log.v("FlexDict", downloadType);
        String dbPath = preferences.getString("@string/pref_dbpath", "--- Not set ---");
        Preference pref_dbPath = (Preference) findPreference(getString(R.string.pref_dbpath));
        pref_dbPath.setSummary(dbPath);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            // Do something with the URI
                        }
                    }
                    // For Ice Cream Sandwich
                }
            } else {
                Uri uri = data.getData();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().putString("@string/pref_dbpath", uri.getPath()).commit();
                Preference dbPath = (Preference) findPreference(getString(R.string.pref_dbpath));
                dbPath.setSummary(uri.getPath());
            }
        }
    }
}
