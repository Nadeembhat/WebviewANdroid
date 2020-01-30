package in.stc.ievincecms;

import android.content.Context;
import android.content.SharedPreferences;

public class CMSPreferenceManager{
    private static final String PREFS_FILE = "in.stc.ievincecms_pref";


    /** Shared preferences, used to persist rules. */
    private final SharedPreferences mPrefs;


    public CMSPreferenceManager(SharedPreferences defaultSharedPreferences) {
        this.mPrefs = defaultSharedPreferences;
    }


    public void saveUrl(String URL){
        mPrefs.edit().putString("SERVER_URL", URL).apply();
    }
    public String getServerUrl() {
        return mPrefs.getString("SERVER_URL", null);
    }
}
