package fi.leffat;

import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressWarnings("unused")
public class TimeChooserDialog extends Dialog
{
	private String ratingItem;
	private String genreItem;
	private String timeItem;
	private int startHour;
	
	private Spinner ratingSpinner;
	private Spinner genreSpinner;
	private Spinner timeSpinner;
	private ArrayAdapter<CharSequence> adapter;
	private Calendar cal;
	
	
	public TimeChooserDialog(Context context) 
	{
		super(context);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timepickerdialog);
		
		cal = Calendar.getInstance();
		
		
		
		
		
		
		ratingSpinner = (Spinner) findViewById(R.id.spinner1);
		adapter = ArrayAdapter.createFromResource(
	            getContext(), R.array.rating_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ratingSpinner.setAdapter(adapter);
		
		
		
		genreSpinner = (Spinner) findViewById(R.id.spinner2);
		adapter = ArrayAdapter.createFromResource(
	            getContext(), R.array.genre_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		genreSpinner.setAdapter(adapter);
		
		
		timeSpinner = (Spinner) findViewById(R.id.spinner3);
		adapter = ArrayAdapter.createFromResource(
	            getContext(), R.array.time_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeSpinner.setAdapter(adapter);
		ratingSpinner.setOnItemSelectedListener(new MyOnItemClickListener());
		genreSpinner.setOnItemSelectedListener(new MyOnItemClickListener());
		timeSpinner.setOnItemSelectedListener(new MyOnItemClickListener());
	}
	
	
	
	
	
	/*
	 * Listener for items in spinner
	 * 
	 * Author: trashed
	 */
	public class MyOnItemClickListener implements OnItemSelectedListener 
	{

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	// TODO: Create function to get data out of spinner item
	    	
	    	if(parent.getId() == R.id.spinner1) {
		    	ratingItem = parent.getItemAtPosition(pos).toString();
	    	}
	    	if (parent.getId() == R.id.spinner2) {
	    		genreItem = parent.getItemAtPosition(pos).toString();
	    	}
	    	if(parent.getId() == R.id.spinner3) {
	    		timeItem = parent.getItemAtPosition(pos).toString();
	    		//MainActivity.startHour = Integer.valueOf(timeItem.substring(0, 2));
	    	}
	    }

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	/*
	 * Return a rating value as a string
	 */
	public String getMovieRating()
	{
		return ratingItem;
	}
	
	/*
	 * Return the start hour as an integer
	 */
	public int getMovieStartHour()
	{
		return startHour;
	}
}
