package fi.leffat;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

/**
 * Preferences activity for the user to control AREA_CODE, filters etc.
 * 
 * @author Teknikaali
 *
 */
public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	/**
	 * Listener for OnSharedPreferenceChangeListener()
	 */
	OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener(){
	    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
	    	  
		    // Check if AREA_CODE has been changed and commit the changes to the SharedPreferences-file
		    if (key.equals("AREA_CODE")) {
				SharedPreferences leffaPrefs = getSharedPreferences("fi.leffat_preferences", MODE_WORLD_READABLE);
				final SharedPreferences.Editor prefsEditor = leffaPrefs.edit();
				prefsEditor.putBoolean("AREA_CODE_CHANGED", true);
				prefsEditor.commit();
			}
		    else if (key.equals("FILTER_ACTIVE_SHOWN_SHOWS")) {
		    	SharedPreferences leffaPrefs = getSharedPreferences("fi.leffat_preferences", MODE_WORLD_READABLE);
				final SharedPreferences.Editor prefsEditor = leffaPrefs.edit();
				prefsEditor.putBoolean("AREA_CODE_CHANGED", true);
				prefsEditor.commit();
		    }
		    // Update the view with changed information
		    updatePrefSummary(findPreference(key));
	    }
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(PreferencesActivity.this, R.xml.preferences, false);

        for(int i=0;i<getPreferenceScreen().getPreferenceCount();i++){
        	initSummary(getPreferenceScreen().getPreference(i));
        }
    }

    @Override 
    protected void onResume(){
        super.onResume();
        // Set up a listener whenever a key changes             
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override 
    protected void onPause() { 
        super.onPause();
        // Unregister the listener whenever a key changes             
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);     
    } 

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p){
       if (p instanceof PreferenceCategory){
            PreferenceCategory pCat = (PreferenceCategory)p;
            for(int i=0;i<pCat.getPreferenceCount();i++){
                initSummary(pCat.getPreference(i));
            }
        }else{
            updatePrefSummary(p);
        }

    }

    private void updatePrefSummary(Preference p){
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p; 
            p.setSummary(listPref.getEntry()); 
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p; 
            p.setSummary(editTextPref.getText()); 
        }

    }
} 