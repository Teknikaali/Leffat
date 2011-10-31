package fi.leffat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

public class TimeChooserDialog extends Dialog
{
	public TimeChooserDialog(Context context) 
	{
		super(context);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timepickerdialog);
		
		TimePicker mPicker = (TimePicker) findViewById(R.id.timePicker);
		mPicker.setIs24HourView(true);
		//mPicker.setOnClickListener(new OnClick());
		
		Spinner ratingSpinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            getContext(), R.array.rating_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ratingSpinner.setAdapter(adapter);
		
		Spinner genreSpinner = (Spinner) findViewById(R.id.spinner2);
		adapter = ArrayAdapter.createFromResource(
	            getContext(), R.array.genre_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		genreSpinner.setAdapter(adapter);
		
	}
	
	/*public class OnClick implements OnClickListener, android.view.View.OnClickListener
	{
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}*/
}
