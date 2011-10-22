package fi.justjoo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Main activity of the application, responsible for creating the list
 * of movies and handling the data by calling for {@link FileLoader} and
 * {@link XmlParser}.
 */
@SuppressWarnings("unused")
public class MainActivity extends Activity
{
    /**
     * Maximum number of days (default = 7)
     * 0 = monday, 6 = sunday
     */
    private static final int numberOfDays = 7;
    
    /**
     * Maximum number of shows per day (default = 60).
     */
    private static final int numberOfShows = 5;
    
    /**
     * ArrayLists for weekdays and movies shown on those days
     */
	private ArrayList<String> 						groups;
	private ArrayList<ArrayList<ArrayList<String>>> childs;
    
    /**
     * Current day ("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
     */
    private String currentDay;
    
    /**
     * Current AREA_CODE
     */
    public static String areacode = null;
    
    /**
     * Application context. Stored here as a static variable to be used
     * by FileLoaderResult (enum) in FileLoader.
     */
    public static Context context;
    
    public static XmlParser parser;
    
    private myExpandableAdapter adapter;
    
    private ExpandableListView movieExpendableList;
    
    public int areacodeSelectionPosition;
    
    private Dialog configDialog;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	isDone = false;

        setContentView(R.layout.main);
    }
    
    private static boolean isDone;
    
    /**
     * Checks all data and reloads them if necessary.<br><br>
     * 
     * Called automatically by Android when the application is started or it resumes
     * from sleep.
     */
	@Override
    public void onResume()
    {
        super.onResume();
        
        context = this;
        
        if (parser == null) {
        	parser = new XmlParser();
        }
        
        /**
         * Preference-editor
         */
        SharedPreferences leffaPrefs = context.getSharedPreferences("leffaPrefs", MODE_WORLD_READABLE);
        final SharedPreferences.Editor prefsEditor = leffaPrefs.edit();
        
        /**
         * Configuration dialog
         */
        
        // Set up the base dialog
    	configDialog = new Dialog(MainActivity.this);
    	configDialog.setContentView(R.layout.customdialog);
    	configDialog.setTitle(R.string.settings_title);
    	configDialog.setCancelable(false);
    	
    	// Set up a spinner selector for areacodes
    	Spinner areacodeSpinner = (Spinner) configDialog.findViewById(R.id.areacodeSpinner);
    	ArrayAdapter<CharSequence> areaAdapter = ArrayAdapter.createFromResource(MainActivity.this,
    																		 R.array.areacode_array,
    																		 android.R.layout.simple_spinner_item);
    	areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	areacodeSpinner.setAdapter(areaAdapter);
    	areacodeSpinner.setOnItemSelectedListener(new OnAreaCodeSelectedListener());
    	
    	// Check if a selection has been done already
    	if (leffaPrefs.getString("AREA_CODE", null) != null)
    	{
    		areacodeSpinner.setSelection(areacodeSelectionPosition);
    	}

    	// Set up Accept-button
    	Button acceptButton = (Button)configDialog.findViewById(R.id.accept);
    	acceptButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			
    			// Reformat selected spinner text to an areacode
    			while (!areacode.matches("\\d+"))
    			{
    				if (areacode.equals("Pääkaupunkiseutu")) {
    					areacode = "1014";
    				}
    				else if (areacode.equals("Espoo")) {
    					areacode = "1012";
    				}
    				else if (areacode.equals("Helsinki")) {
    					areacode = "1002";
    				}
    				else if (areacode.equals("Helsinki :: Kinopalatsi")) {
    					areacode = "1031";
    				}
    				else if (areacode.equals("Helsinki :: Maxim")) {
    					areacode = "1012";
    				}
    				else if (areacode.equals("Jyväskylä")) {
    					areacode = "1015";
    				}
    				else if (areacode.equals("Kuopio")) {
    					areacode = "1016";
    				}
    				else if (areacode.equals("Lahti")) {
    					areacode = "1017";
    				}
    				else if (areacode.equals("Oulu")) {
    					areacode = "1018";
    				}
    				else if (areacode.equals("Pori")) {
    					areacode = "1019";
    				}
    				else if (areacode.equals("Rovaniemi")) {
    					areacode = "1020";
    				}
    				else if (areacode.equals("Tampere")) {
    					areacode = "1021";
    				}
    				else if (areacode.equals("Tampere :: Cine Atlas")) {
    					areacode = "1034";
    				}
    				else if (areacode.equals("Tampere :: Plevna")) {
    					areacode = "1035";
    				}
    				else if (areacode.equals("Turku")) {
    					areacode = "1022";
    				}
    				else if (areacode.equals("Vantaa")) {
    					areacode = "1013";
    				}
    			}
    			
    			// Save the settings
                prefsEditor.putString("AREA_CODE", areacode);
    	        prefsEditor.putBoolean("AREA_CODE_CHANGED", true);
                prefsEditor.commit();

    			Toast.makeText(context, "The area code is " + areacode, Toast.LENGTH_LONG).show();
                
    			configDialog.dismiss();
    			configDialog.hide();
    		}
    	});
    	
    	// Set up Cancel-button
    	Button cancelButton = (Button)configDialog.findViewById(R.id.cancel);
    	cancelButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			configDialog.dismiss();
    			configDialog.hide();
    		}
    	});
        
        /**
         * End of Configuration dialog
         */
        
//        if (leffaPrefs.getString("AREA_CODE", null) == null) {
//            configDialog.show();
//        }
		
		// Create listener for refresh-button
		ImageButton refresh = (ImageButton) findViewById(R.id.refresh_button);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
        		Intent intent = new Intent(context, LoadActivity.class);
        		startActivityForResult(intent, 0);
			}
		});
		
		// Create listener for config-button
		ImageButton config = (ImageButton) findViewById(R.id.config_button);
		config.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
            	configDialog.show();
			}
		});
        
        if (!isDone) {
        	Intent intent = new Intent(this, LoadActivity.class);
        	startActivityForResult(intent, 0);
        	isDone = true;
        }
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK || resultCode == -2) {
			generateView();

	    	adapter = new myExpandableAdapter(this, groups, childs);
			movieExpendableList.setAdapter(adapter);
		}
		else if (resultCode == 0) {
			// Configure dialog
			configDialog.show();
			//Toast.makeText(context, "NÄYTÄ CONFIG-DIALOGI", Toast.LENGTH_LONG).show();
		}
	}
    
    /**
     * Initializes the groups and childs
     */
    private void generateView()
    {
    	// Get a list of movies
		ArrayList<ArrayList<Movie>> movies = parser.getList();
        
		// Get current date
        Date anotherCurDate = new Date();  
        DateFormat formatter = new SimpleDateFormat("E", Locale.ENGLISH);
        String currentDay = formatter.format(anotherCurDate);

        movieExpendableList = (ExpandableListView) findViewById(R.id.ExpandableListView_leffalista);
        groups = new ArrayList<String>();
    	childs = new ArrayList<ArrayList<ArrayList<String>>>();

        movieExpendableList.setOnChildClickListener(new OnChildClick());
    	
    	// Table to recognize days of week
    	final String dayTable[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    	
    	// Day number the list is going to start from
    	byte startDay = 0;
    	
    	// Count of days added to the list already
    	byte dayCount = 0;
    	
    	// Create groups for 7 days
    	
    	// Find and set startDay
    	for (byte index = 0; index < 7; index++) {
    		
    		// List's first day found
    		if (currentDay.equals(dayTable[index])) {
    			startDay = index;
    			break;
    		}
    	}
    	
    	// Start adding days to the list
    	while (dayCount < 7) {
    		
    		if (dayCount == 0) {
    			groups.add(getApplicationContext().getString(R.string.day + startDay));
    			dayCount++;
    		}
    		
    		// Days after this day
    		if (dayCount + startDay < 7) {
    			groups.add(getApplicationContext().getString(R.string.day + startDay + dayCount));
    			dayCount++;
    		}
    		
    		// Days before this day
    		else {
    			if (dayTable[startDay + dayCount - 7].equals("Mon")) {
    				groups.add(getApplicationContext().getString(R.string.day));
    				dayCount++;
    			}
    			else {
    				groups.add(getApplicationContext().getString(R.string.day + (startDay + dayCount - 7)));
    				dayCount++;
    			}
    		}
    	}
    	
    	// Initialize children
    	dayCount = 0;
    	
    	while (dayCount < 7) {
        	
	        childs.add(new ArrayList<ArrayList<String>>());
	        
	        for (int index = 0; index < movies.get(dayCount).size(); index++) {
	        	childs.get(dayCount).add(new ArrayList<String>());
		        
	        	String text = "(" + movies.get(dayCount).get(index).showStartTimeH + ":" + movies.get(dayCount).get(index).showStartTimeM + ") " + movies.get(dayCount).get(index).title;
	        	
	        	childs.get(dayCount).get(index).add(text);
	        }
	        
	        dayCount++;
    	}
    }
    
    /**
     * Listener for a spinner
     * 
     * @author Teknikaali
     *
     */
    public class OnAreaCodeSelectedListener implements OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> parent,
				View view, int pos, long id) {
			
			// Save the selection to a variable
			areacode = parent.getItemAtPosition(pos).toString();
			areacodeSelectionPosition = pos;
		}
		
		@SuppressWarnings("rawtypes")
		public void onNothingSelected(AdapterView parent) {
			// Do nothing ...
		}
	}
    
    /**
     * Listener for a ExpandableList child listener
     * 
     * @author trashed
     *
     */
    public class OnChildClick implements OnChildClickListener
    {
    	public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
    		
    		// Variables for a selected movie to pass forward in a Bundle 
    		String originalName = parser.getMovie(groupPosition, childPosition).originalTitle;
    		String translatedName = parser.getMovie(groupPosition, childPosition).title;
    		String genres = parser.getMovie(groupPosition, childPosition).getGenres();
    		String productionYear = parser.getMovie(groupPosition, childPosition).productionYear;
    		String ratingImage = parser.getMovie(groupPosition, childPosition).ratingImageUrl;
    		String movieThumbnail = parser.getMovie(groupPosition, childPosition).largeImageUrl;
    		String url = parser.getMovie(groupPosition, childPosition).showUrl;
    		
			//Send information in Bundle
    		Bundle movieBundle = new Bundle();
    		movieBundle.putString("originalName", originalName);
    		movieBundle.putString("translatedName", translatedName);
    		movieBundle.putString("genres", genres);
    		movieBundle.putString("productionYear", productionYear);
    		movieBundle.putString("ratingImage", ratingImage);
    		movieBundle.putString("movieThumbnail", movieThumbnail);
    		movieBundle.putString("url", url);
    		
    		Intent popupIntent = new Intent(context, PopupActivity.class);
    		popupIntent.putExtras(movieBundle);
    		startActivity(popupIntent);
			return false;
		}
    }
}
